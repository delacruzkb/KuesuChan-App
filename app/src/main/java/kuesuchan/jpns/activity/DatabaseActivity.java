package kuesuchan.jpns.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.schedulers.Schedulers;
import kuesuchan.jpns.adapter.DatabaseRecycleViewAdapter;
import kuesuchan.jpns.R;
import kuesuchan.jpns.database.AppDatabase;
import kuesuchan.jpns.database.dao.helper.KanjiWritingDaoHelper;
import kuesuchan.jpns.database.dao.helper.VocabularyDaoHelper;
import kuesuchan.jpns.database.entity.KanjiWriting;
import kuesuchan.jpns.database.entity.Vocabulary;
import kuesuchan.jpns.dialog.DatabaseInputDialog;
import kuesuchan.jpns.dialog.LoadingDialog;

public class DatabaseActivity extends AppCompatActivity{

    private Button searchButton, resetButton, addButton;
    private Spinner databaseSpinner, searchTypeSpinner;
    private EditText searchBarEditText;
    private RecyclerView searchRecyclerView;

    private AppDatabase db;
    private VocabularyDaoHelper vocabularyDaoHelper;
    private KanjiWritingDaoHelper kanjiWritingDaoHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
        setupViews();
        db = AppDatabase.getInstance(getApplicationContext());
        vocabularyDaoHelper = db.getVocabularyDaoHelper();
        kanjiWritingDaoHelper = db.getKanjiWritingDaoHelper();
    }

    private void setupViews() {
        searchTypeSpinner = findViewById(R.id.searchTypeSpinner);
        databaseSpinner = findViewById(R.id.databaseSpinner);
        databaseSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Arrays.stream(AppDatabase.Table.values()).collect(Collectors.toList())));
        databaseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch ((AppDatabase.Table) adapterView.getItemAtPosition(i)){
                    case Vocabulary:
                        searchTypeSpinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, Arrays.stream(VocabularyDaoHelper.Columns.values()).collect(Collectors.toList())));
                        break;
                    case Kanji_Writing:
                        searchTypeSpinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, Arrays.stream(KanjiWritingDaoHelper.Columns.values()).collect(Collectors.toList())));
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                switch ((AppDatabase.Table) adapterView.getSelectedItem()){
                    case Vocabulary:
                        searchTypeSpinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, Arrays.stream(VocabularyDaoHelper.Columns.values()).collect(Collectors.toList())));
                        break;
                    case Kanji_Writing:
                        searchTypeSpinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, Arrays.stream(KanjiWritingDaoHelper.Columns.values()).collect(Collectors.toList())));
                        break;
                    default:
                        break;
                }
            }
        });

        searchBarEditText = findViewById(R.id.searchBarEditText);
        searchRecyclerView = findViewById(R.id.searchRecyclerView);

        searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(view -> search());
        resetButton = findViewById(R.id.resetDataButton);
        resetButton.setOnClickListener(view -> resetDatabase());
        addButton = findViewById(R.id.addToDatabaseButton);
        addButton.setOnClickListener(view -> addToDatabase());


    }


    private void search(){
        String searchText = searchBarEditText.getText().toString();
        List<Object> objects = new ArrayList<>();
        DatabaseRecycleViewAdapter adapter = new DatabaseRecycleViewAdapter(this);
        searchRecyclerView.setAdapter(adapter);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        switch ((AppDatabase.Table) databaseSpinner.getSelectedItem()){
            case Vocabulary:
                adapter.setObjects(AppDatabase.getInstance(getApplicationContext())
                        .getVocabularyDaoHelper().search((VocabularyDaoHelper.Columns) searchTypeSpinner.getSelectedItem(), searchText));
                break;
            case Kanji_Writing:
                adapter.setObjects(AppDatabase.getInstance(getApplicationContext())
                        .getKanjiWritingDaoHelper().search((KanjiWritingDaoHelper.Columns) searchTypeSpinner.getSelectedItem(), searchText));
                break;
        }
    }


    private void addToDatabase(){
        DatabaseInputDialog dialog = new DatabaseInputDialog(getApplicationContext(), null, (AppDatabase.Table) databaseSpinner.getSelectedItem());
        dialog.show();
    }

    private void resetDatabase(){
        //TODO: Prompt and loading dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setTitle("Confirm");
        builder.setMessage("Are you sure you want to reset to default data?");
        builder.setPositiveButton("Reset", ((dialogInterface, i) -> {
            LoadingDialog dialog = new LoadingDialog(DatabaseActivity.this);
            dialog.startLoadingDialog();
            Completable.create(emitter -> db.clearAllTables()).subscribeOn(Schedulers.io()).subscribe();
            loadVocabulary(vocabularyDaoHelper, "data-n5-vocabulary.csv", "N5");
            loadKanjiWriting(kanjiWritingDaoHelper, "data-n5-kanji-writing.csv", "N5");
            dialog.dismissDialog();
        }));
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void loadVocabulary(VocabularyDaoHelper dao, String fileName, String name){
        try{
            CSVReader reader =
                    new CSVReaderBuilder(new InputStreamReader(getApplicationContext().getAssets().open(fileName)))
                            .withSkipLines(1) // skip header
                            .build();
            List<Vocabulary> vocabularyList =
                    reader
                            .readAll()
                            .stream()
                            .map(data ->
                                    new Vocabulary(data[0],data[1],data[2],data[3],data[4], data[5],data[6])).collect(Collectors.toList());
            for ( int i =0; i< vocabularyList.size(); i++){
                //TODO: some formatting for multi values, split-> trim > merge with ' / '
                //TODO: change section to string?
                //TODO: check for it in DB first or detect an abort somehow.
                Vocabulary newVocab = vocabularyList.get(i);
                Vocabulary dbVocab = dao.getVocabulary(newVocab.getEnglish(), newVocab.getKana());
                if(newVocab.getKanji().equals("")) {
                    newVocab.setKanji(dbVocab.getKanji());
                } else if(!dbVocab.getKanji().equals("")) {
                    throw new Exception("Conflict for " + newVocab.toString() + " \n and " +dbVocab.toString());
                }

                if(newVocab.getKanji().equals("")) {
                    newVocab.setKanji(dbVocab.getKanji());
                } else if(!dbVocab.getKanji().equals("")) {
                    throw new Exception("Conflict for " + newVocab.toString() + " \n and " +dbVocab.toString());
                }

                if(newVocab.getKanji().equals("")) {
                    newVocab.setKanji(dbVocab.getKanji());
                } else if(!dbVocab.getKanji().equals("")) {
                    throw new Exception("Conflict for " + newVocab.toString() + " \n and " +dbVocab.toString());
                }

                if(newVocab.getKanji().equals("")) {
                    newVocab.setKanji(dbVocab.getKanji());
                } else if(!dbVocab.getKanji().equals("")) {
                    throw new Exception("Conflict for " + newVocab.toString() + " \n and " +dbVocab.toString());
                }

                if(!newVocab.getSection().equals(dbVocab.getSection())){
                    List<String> sectionList =  Arrays.asList(dbVocab.getSection().split("/"));
                    sectionList.stream().forEach(sec -> newVocab.addSection(Integer.parseInt(sec)));
                }
                dao.insert(newVocab);
                // if field null, go with field with value.
                // if both fields have value, check if they are the same
                // if section, just add them together
                // otherwise it is a conflict


            }
        } catch(Exception e){
            Toast.makeText(this, "Error loading "+ name +" Vocabulary", Toast.LENGTH_SHORT).show();

        }
    }

    private void loadKanjiWriting(KanjiWritingDaoHelper dao, String fileName, String name){
        if(dao.getRowCount()==0){
            try{
                CSVReader reader =
                        new CSVReaderBuilder(new InputStreamReader(getApplicationContext().getAssets().open(fileName)))
                                .withSkipLines(1) // skip header
                                .build();
                List<KanjiWriting> kanjiWritingsList =
                        reader
                                .readAll()
                                .stream()
                                .map(data ->
                                        new KanjiWriting(data[0],data[1],data[2],Integer.parseInt(data[3]),data[4], data[5],data[6])).collect(Collectors.toList());
                for ( int i =0; i< kanjiWritingsList.size(); i++){
                    dao.insert(kanjiWritingsList.get(i));
                }
            } catch(Exception e){
                Toast.makeText(this, "Error loading "+ name +" KanjiWriting", Toast.LENGTH_SHORT).show();
            }
        }
    }

}