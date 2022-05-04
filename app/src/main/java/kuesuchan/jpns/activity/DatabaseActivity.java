package kuesuchan.jpns.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
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
import java.util.Set;
import java.util.stream.Collectors;

import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;
import kuesuchan.jpns.adapter.DatabaseRecycleViewAdapter;
import kuesuchan.jpns.R;
import kuesuchan.jpns.database.AppDatabase;
import kuesuchan.jpns.database.dao.helper.KanjiWritingDaoHelper;
import kuesuchan.jpns.database.dao.helper.SourceDaoHelper;
import kuesuchan.jpns.database.dao.helper.VocabularyDaoHelper;
import kuesuchan.jpns.database.entity.KanjiWriting;
import kuesuchan.jpns.database.entity.Source;
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
    private SourceDaoHelper sourceDaoHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
        setupViews();
        db = AppDatabase.getInstance(getApplicationContext());
        vocabularyDaoHelper = db.getVocabularyDaoHelper();
        kanjiWritingDaoHelper = db.getKanjiWritingDaoHelper();
        sourceDaoHelper = db.getSourceDaoHelper();
    }

    private void setupViews() {
        searchTypeSpinner = findViewById(R.id.searchTypeSpinner);
        databaseSpinner = findViewById(R.id.databaseSpinner);
        databaseSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Arrays.stream(AppDatabase.Table.values()).collect(Collectors.toList())));
        databaseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                List<String> columnList = new ArrayList<>();
                switch ((AppDatabase.Table) adapterView.getItemAtPosition(i)){
                    case Vocabulary:
                        columnList = VocabularyDaoHelper.getColumnList();
                        break;
                    case Kanji_Writing:
                        columnList = KanjiWritingDaoHelper.getColumnList();
                        break;
                    case Source:
                        columnList = SourceDaoHelper.getColumnList();
                    default:
                        break;
                }

                searchTypeSpinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, columnList));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                }
        });

        searchBarEditText = findViewById(R.id.searchBarEditText);
        searchRecyclerView = findViewById(R.id.searchRecyclerView);

        searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(view -> {
            hideKeyBoard();
            search();
        });
        resetButton = findViewById(R.id.resetDataButton);
        resetButton.setOnClickListener(view -> resetDatabase());
        addButton = findViewById(R.id.addToDatabaseButton);
        addButton.setOnClickListener(view -> addToDatabase());


    }

    private void hideKeyBoard() {
        View view1 = this.getCurrentFocus();
        if(view1 != null){
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
        }
    }


    private void search(){
        String searchText = searchBarEditText.getText().toString();
        String searchColumn = searchTypeSpinner.getSelectedItem().toString();
        List<? extends  Object> databaseObjects = new ArrayList<>();
        switch ((AppDatabase.Table) databaseSpinner.getSelectedItem()){
            case Vocabulary:
                databaseObjects = vocabularyDaoHelper.search(VocabularyDaoHelper.Columns.valueOf(searchColumn), searchText);
                break;
            case Kanji_Writing:
                databaseObjects = kanjiWritingDaoHelper.search(KanjiWritingDaoHelper.Columns.valueOf(searchColumn), searchText);
                break;
            case Source:
                databaseObjects = sourceDaoHelper.search(SourceDaoHelper.Columns.valueOf(searchColumn), searchText);
                break;
        }
        DatabaseRecycleViewAdapter adapter = new DatabaseRecycleViewAdapter(this);
        adapter.setObjects(databaseObjects);
        searchRecyclerView.setAdapter(adapter);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    private void addToDatabase(){
        DatabaseInputDialog dialog = new DatabaseInputDialog(this, null, (AppDatabase.Table) databaseSpinner.getSelectedItem());
        dialog.show();
    }

    private void resetDatabase(){
        //TODO: Prompt and loading dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm");
        builder.setMessage("Are you sure you want to reset to default data?");
        builder.setPositiveButton("Reset", ((dialogInterface, i) -> {
            LoadingDialog dialog = new LoadingDialog(DatabaseActivity.this);
            dialog.startLoadingDialog();
            Completable.create(emitter -> db.clearAllTables()).subscribeOn(Schedulers.io()).subscribe();
            loadVocabulary(vocabularyDaoHelper, "KuesuChan-Data - N5-Vocabulary.csv", "N5");
            loadKanjiWriting(kanjiWritingDaoHelper, "KuesuChan-Data - N5-Kanji.csv", "N5");
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
                            .map(data ->{
                                String english = data[0];
                                String kana = data[1];
                                String kanji = data[2];
                                String help_text = data[3];
                                String source = data[4];
                                int section = Integer.parseInt(data[5]);
                                Source dbSource = sourceDaoHelper.getSource(data[4]);
                                if(dbSource != null && dbSource.getSectionCount() < section){
                                    dbSource.setSectionCount(section);
                                    sourceDaoHelper.update(dbSource);
                                }
                                return new Vocabulary(english, kana, kanji, help_text, SourceDaoHelper.toSourceSting(source,section));
                            }).collect(Collectors.toList());
            for ( int i =0; i< vocabularyList.size(); i++){
                Vocabulary newVocab = vocabularyList.get(i);
                Vocabulary dbVocab = dao.getVocabulary(newVocab.getEnglish(), newVocab.getKana());
                if(dbVocab !=null){
                    if(newVocab.getKanji().equals("") || newVocab.getKanji().equalsIgnoreCase(dbVocab.getKanji())) {
                        newVocab.setKanji(dbVocab.getKanji());
                    } else if(!dbVocab.getKanji().equals("")) {
                        throw new Exception("Kanji Conflict for " + newVocab.toString() + " \n and " +dbVocab.toString());
                    }

                    if(newVocab.getHelp_text().equals("") || newVocab.getHelp_text().equalsIgnoreCase(dbVocab.getHelp_text())) {
                        newVocab.setHelp_text(dbVocab.getHelp_text());
                    } else if(!dbVocab.getHelp_text().equals("")) {
                        throw new Exception("Help_Text Conflict for " + newVocab.toString() + " \n and " +dbVocab.toString());
                    }

                    newVocab.addSources(dbVocab.getSources());
                    dao.update(newVocab);
                } else if(newVocab.getEnglish().equals("") || newVocab.getKana().equals("")){
                    throw new Exception("PK was empty");
                } else {
                    dao.insert(newVocab);
                }
            }
        } catch(Exception e){
            Toast.makeText(this, "Error loading "+ name +" Vocabulary", Toast.LENGTH_SHORT).show();
            System.out.println("ERROR ERROR " + e.getMessage());
            System.out.println(e.getStackTrace());
        }
    }

    //TODO: improve logging for csv errors
    private void loadKanjiWriting(KanjiWritingDaoHelper dao, String fileName, String name){
            try{
                CSVReader reader =
                        new CSVReaderBuilder(new InputStreamReader(getApplicationContext().getAssets().open(fileName)))
                                .withSkipLines(1) // skip header
                                .build();
                List<KanjiWriting> kanjiWritingsList =
                        reader
                                .readAll()
                                .stream()
                                .map(data -> {
                                    String kanji = data[0];

                                    String japanese_reading = data[1];

                                    String phonetic_reading = data[2];

                                    int strokes = Integer.parseInt(data[3]);

                                    String meaning = data[4];

                                    String source = data[5];
                                    int section = Integer.parseInt(data[6]);
                                    Source dbSource = sourceDaoHelper.getSource(source);
                                    if (dbSource != null && dbSource.getSectionCount() < section) {
                                        dbSource.setSectionCount(section);
                                        sourceDaoHelper.update(dbSource);
                                    }
                                    return new KanjiWriting(kanji,japanese_reading,phonetic_reading,strokes, meaning, SourceDaoHelper.toSourceSting(source, section));
                                }).collect(Collectors.toList());
                for ( int i =0; i< kanjiWritingsList.size(); i++){

                    KanjiWriting newKanjiWriting = kanjiWritingsList.get(i);
                    KanjiWriting dbKanjiWriting = dao.getKanjiWriting(newKanjiWriting.getKanji());
                    if(dbKanjiWriting !=null){
                        if(newKanjiWriting.getJapanese_reading().equals("") || newKanjiWriting.getJapanese_reading().equalsIgnoreCase(dbKanjiWriting.getJapanese_reading())) {
                            newKanjiWriting.setJapanese_reading(dbKanjiWriting.getJapanese_reading());
                        } else if(!dbKanjiWriting.getJapanese_reading().equals("")) {
                            throw new Exception("Japanese Reading Conflict for " + newKanjiWriting.toString() + " \n and " +dbKanjiWriting.toString());
                        }

                        if(newKanjiWriting.getPhonetic_reading().equals("") || newKanjiWriting.getPhonetic_reading().equalsIgnoreCase(dbKanjiWriting.getPhonetic_reading())) {
                            newKanjiWriting.setPhonetic_reading(dbKanjiWriting.getPhonetic_reading());
                        } else if(!dbKanjiWriting.getPhonetic_reading().equals("")) {
                            throw new Exception("Phonetic ReadingConflict for " + newKanjiWriting.toString() + " \n and " +dbKanjiWriting.toString());
                        }


                        if(newKanjiWriting.getStrokes() != 0 || newKanjiWriting.getStrokes() == dbKanjiWriting.getStrokes()) {
                            newKanjiWriting.setStrokes(dbKanjiWriting.getStrokes());
                        } else if(dbKanjiWriting.getStrokes() !=0) {
                            throw new Exception("Strokes Conflict for " + newKanjiWriting.toString() + " \n and " +dbKanjiWriting.toString());
                        }

                        if(newKanjiWriting.getMeaning().equals("") || newKanjiWriting.getMeaning().equalsIgnoreCase(dbKanjiWriting.getMeaning())) {
                            newKanjiWriting.setMeaning(dbKanjiWriting.getMeaning());
                        } else if(!dbKanjiWriting.getMeaning().equals("")) {
                            throw new Exception("Meaning Conflict for " + newKanjiWriting.toString() + " \n and " +dbKanjiWriting.toString());
                        }

                        newKanjiWriting.addSources(dbKanjiWriting.getSources());
                        dao.update(newKanjiWriting);
                    } else {
                        dao.insert(newKanjiWriting);
                    }

                }
            } catch(Exception e){
                Toast.makeText(this, "Error loading "+ name +" KanjiWriting", Toast.LENGTH_SHORT).show();
                System.out.println("ERROR ERROR " + e.getMessage());
                System.out.println(e.getStackTrace());
            }
    }

}