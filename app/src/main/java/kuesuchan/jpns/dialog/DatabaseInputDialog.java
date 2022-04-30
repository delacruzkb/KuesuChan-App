package kuesuchan.jpns.dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.room.util.TableInfo;

import java.util.Arrays;
import java.util.List;

import kuesuchan.jpns.R;
import kuesuchan.jpns.database.AppDatabase;
import kuesuchan.jpns.database.Converters;
import kuesuchan.jpns.database.dao.helper.KanjiWritingDaoHelper;
import kuesuchan.jpns.database.dao.helper.VocabularyDaoHelper;
import kuesuchan.jpns.database.entity.KanjiWriting;
import kuesuchan.jpns.database.entity.Vocabulary;

public class DatabaseInputDialog {

    private TextView columnLabel1,columnLabel2,columnLabel3,columnLabel4,
            columnLabel5,columnLabel6,columnLabel7;
    private EditText columnEditText1,columnEditText2,columnEditText3,columnEditText4,
            columnEditText5,columnEditText6,columnEditText7;

    private AlertDialog dialog;

    private AppDatabase.Table table;

    private  Context context;

    private Object selectedObject;

    public DatabaseInputDialog(@NonNull Context context, Object selectedObject, @NonNull AppDatabase.Table table) {
        this.table = table;
        this.context = context;
        this.selectedObject = selectedObject;

        View dialogFormView = LayoutInflater.from(context).inflate(R.layout.dialog_database_row_edit,null);
        initializeView(dialogFormView);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setView(dialogFormView)
                .setCancelable(false)
                .setNeutralButton("Cancel", (dialogInterface, i) -> dialogInterface.cancel())
                .setNegativeButton("Delete", (dialogInterface, i) -> {
                    AlertDialog.Builder innerBuilder = new AlertDialog.Builder(context);
                    innerBuilder.setTitle("Do you want to Delete?");
                    innerBuilder.setMessage(selectedObject.toString());
                    innerBuilder.setPositiveButton("Confirm", (dialogInterface14, i14) -> {
                        //TODO: based on which table, delete
                        Toast.makeText(context, "DELETED", Toast.LENGTH_SHORT).show();
//                        switch (table){
//                            case Vocabulary:
//                                AppDatabase.getInstance(context).getVocabularyDaoHelper().delete((Vocabulary) selectedObject);
//                            case Kanji_Writing:
//                                AppDatabase.getInstance(context).getKanjiWritingDaoHelper().delete((KanjiWriting) selectedObject);
//                        }
                    });
                    innerBuilder.setNegativeButton("Cancel", (dialogInterface13, i13) -> dialogInterface13.cancel());
                    innerBuilder.create().show();
                })
                .setPositiveButton("Insert", (dialogInterface, i) -> {
                    AlertDialog.Builder innerBuilder = new AlertDialog.Builder(context);
                    innerBuilder.setTitle("Do you want to Update?");
                    innerBuilder.setMessage(selectedObject.toString());
                    innerBuilder.setPositiveButton("Confirm", (dialogInterface12, i12) -> {
                        //TODO: based on which table, Update
                        Toast.makeText(context, "UPDATED", Toast.LENGTH_SHORT).show();
//                        insert();
                    });
                    innerBuilder.setNegativeButton("Cancel", (dialogInterface1, i1) -> dialogInterface1.cancel());
                    innerBuilder.create().show();
                });
        dialog = builder.create();
    }

    public void show(){
        dialog.show();
    }

    private void initializeView(View view){
        columnLabel1=view.findViewById(R.id.columnLabel1);
        columnLabel2=view.findViewById(R.id.columnLabel2);
        columnLabel3=view.findViewById(R.id.columnLabel3);
        columnLabel4=view.findViewById(R.id.columnLabel4);
        columnLabel5=view.findViewById(R.id.columnLabel5);
        columnLabel6=view.findViewById(R.id.columnLabel6);
        columnLabel7=view.findViewById(R.id.columnLabel7);
        columnEditText1=view.findViewById(R.id.columnEditText1);
        columnEditText2=view.findViewById(R.id.columnEditText2);
        columnEditText3=view.findViewById(R.id.columnEditText3);
        columnEditText4=view.findViewById(R.id.columnEditText4);
        columnEditText5=view.findViewById(R.id.columnEditText5);
        columnEditText6=view.findViewById(R.id.columnEditText6);
        columnEditText7=view.findViewById(R.id.columnEditText7);

        switch (table){
            case Vocabulary:
                loadVocabularyData((Vocabulary) selectedObject);
            case Kanji_Writing:
                loadKanjiWritingData((KanjiWriting) selectedObject);
        }
    }

    @SuppressLint("SetTextI18n")
    private void loadVocabularyData(Vocabulary vocabulary){
        columnLabel1.setText(VocabularyDaoHelper.Columns.english.name() + "*");
        columnLabel2.setText(VocabularyDaoHelper.Columns.kana.name() + "*");
        columnLabel3.setText(VocabularyDaoHelper.Columns.kanji.name());
        columnLabel4.setText(VocabularyDaoHelper.Columns.help_text.name());
        columnLabel5.setText(VocabularyDaoHelper.Columns.source.name() + "(Source.Section, Source.Section, ...)");
        columnLabel6.setVisibility(View.GONE);
        columnEditText6.setVisibility(View.GONE);
        columnLabel7.setVisibility(View.GONE);
        columnEditText7.setVisibility(View.GONE);

        if(vocabulary != null ){
            columnEditText1.setText(vocabulary.getEnglish());
            columnEditText2.setText(vocabulary.getKana());
            columnEditText3.setText(vocabulary.getKanji());
            columnEditText4.setText(vocabulary.getHelp_text());
            columnEditText5.setText(vocabulary.getSourceString());
        }
    }

    @SuppressLint("SetTextI18n")
    private void loadKanjiWritingData(KanjiWriting kanjiWriting){
        columnLabel1.setText( KanjiWritingDaoHelper.Columns.kanji.name() + "*");
        columnLabel2.setText(KanjiWritingDaoHelper.Columns.japanese_reading.name());
        columnLabel3.setText(KanjiWritingDaoHelper.Columns.phonetic_reading.name());
        columnLabel4.setText(KanjiWritingDaoHelper.Columns.strokes.name());
        columnEditText4.setInputType(InputType.TYPE_CLASS_NUMBER);
        columnLabel5.setText(KanjiWritingDaoHelper.Columns.meaning.name());
        columnLabel6.setText(KanjiWritingDaoHelper.Columns.source.name() + "(Source.Section, Source.Section, ...)");
        columnLabel7.setVisibility(View.GONE);
        columnEditText7.setVisibility(View.GONE);

        if(kanjiWriting !=null){
            columnEditText1.setText(kanjiWriting.getKanji());
            columnEditText2.setText(kanjiWriting.getJapanese_reading());
            columnEditText3.setText(kanjiWriting.getPhonetic_reading());
            columnEditText4.setText(kanjiWriting.getStrokes());
            columnEditText5.setText(kanjiWriting.getMeaning());
            columnEditText6.setText(kanjiWriting.getSourceString());
        }

    }

    private void insert(){
        switch (table){
            case Vocabulary:
                insertVocabulary();
            case Kanji_Writing:
                insertKanjiWriting();
        }
    }

    private void insertVocabulary(){
        VocabularyDaoHelper vocabularyDaoHelper = AppDatabase.getInstance(context).getVocabularyDaoHelper();
        String english = columnEditText1.getText().toString();
        String kana = columnEditText2.getText().toString();

        if(english.trim().length()==0 || kana.trim().length()==0){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Missing Data")
                    .setMessage("English and Kana are required fields");
        }
        Vocabulary original = (Vocabulary) selectedObject;
        Vocabulary newItem = new Vocabulary(english,
                kana,
                columnEditText3.getText().toString(),
                columnEditText4.getText().toString(),
                Converters.fromStringToStringSet(columnEditText5.getText().toString()));
        Vocabulary conflicItem = vocabularyDaoHelper.getVocabulary(english,kana);

        boolean conflict = false;

        if(original==null && conflicItem ==null){ // insert with no conflicts
            vocabularyDaoHelper.insert(newItem);
        } else if (original==null && conflicItem!=null){ // insert with conflicts
            conflict=true;
        } else if (original!=null && original.equals(newItem)){ // update Non-PK
            vocabularyDaoHelper.update(newItem);
        } else if (original!=null && !original.equals(newItem) && !newItem.equals(conflicItem)) { // update PK with no conflict
            vocabularyDaoHelper.delete(original);
            vocabularyDaoHelper.insert(newItem);
        } else if (original!=null && !original.equals(newItem) && newItem.equals(conflicItem)) {  // update PK with conflicts
            conflict=true;
        }


        if(conflict){
            StringBuilder conflictStringBuilder = new StringBuilder();
            conflictStringBuilder .append("English : " + conflicItem.getEnglish())
                    .append("\nKana : " + conflicItem.getKana());
            if (!newItem.getKanji().equals(conflicItem.getKanji())) {
                conflictStringBuilder .append("\nKanji: " + conflicItem.getKanji() + " -> " + newItem.getKanji());
            }
            if (!newItem.getHelp_text().equals(conflicItem.getHelp_text())) {
                conflictStringBuilder .append("\nHelp Text: " + conflicItem.getHelp_text() + " -> " + newItem.getHelp_text());
            }
            //TODO: make sure this match works even when new item has out of order string
            if (!newItem.getSourceString().equals(conflicItem.getSourceString())) {
                conflictStringBuilder .append("\nSource: " + conflicItem.getSourceString()+ " -> " + newItem.getSourceString());
            }
            AlertDialog.Builder conflictAlertBuilder = new AlertDialog.Builder(context);
            conflictAlertBuilder.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.cancel())
                    .setPositiveButton("Overwrite", ((dialogInterface, i) -> {
                        if(original!=null){
                            vocabularyDaoHelper.delete(original);
                        }
                        vocabularyDaoHelper.insert(newItem);
                    }));
        }
    }

    private void insertKanjiWriting() {
        KanjiWritingDaoHelper kanjiWritingDaoHelper = AppDatabase.getInstance(context).getKanjiWritingDaoHelper();
        String kanji = columnEditText1.getText().toString();

        if(kanji.trim().length()==0){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Missing Data")
                    .setMessage("Kanji is a required field");
        }
        KanjiWriting original = (KanjiWriting) selectedObject;
        KanjiWriting newItem = new KanjiWriting(kanji,
                columnEditText2.getText().toString(),
                columnEditText3.getText().toString(),
                Integer.parseInt(columnEditText4.getText().toString()),
                columnEditText5.getText().toString(),
                Converters.fromStringToStringSet(columnEditText6.getText().toString()));
        KanjiWriting conflicItem = kanjiWritingDaoHelper.getKanjiWriting(kanji);

        boolean conflict = false;

        if(original==null && conflicItem ==null){ // insert with no conflicts
            kanjiWritingDaoHelper.insert(newItem);
        } else if (original==null && conflicItem!=null){ // insert with conflicts
            conflict=true;
        } else if (original!=null && original.equals(newItem)){ // update Non-PK
            kanjiWritingDaoHelper.update(newItem);
        } else if (original!=null && !original.equals(newItem) && !newItem.equals(conflicItem)) { // update PK with no conflict
            kanjiWritingDaoHelper.delete(original);
            kanjiWritingDaoHelper.insert(newItem);
        } else if (original!=null && !original.equals(newItem) && newItem.equals(conflicItem)) {  // update PK with conflicts
            conflict=true;
        }


        if(conflict){
            StringBuilder conflictStringBuilder = new StringBuilder();
            conflictStringBuilder.append("Kanji : " + conflicItem.getKanji());
            if (!newItem.getJapanese_reading().equals(newItem.getJapanese_reading())) {
                conflictStringBuilder .append("\nJapanese Reading: " + conflicItem.getJapanese_reading() + " -> " + newItem.getJapanese_reading());
            }
            if (!newItem.getPhonetic_reading().equals(conflicItem.getPhonetic_reading())) {
                conflictStringBuilder .append("\nPhonetic Reading: " + conflicItem.getPhonetic_reading() + " -> " + newItem.getPhonetic_reading());
            }
            if (newItem.getStrokes() != conflicItem.getStrokes()) {
                conflictStringBuilder .append("\nStrokes: " + conflicItem.getStrokes() + " -> " + newItem.getStrokes());
            }
            if (!newItem.getMeaning().equals(conflicItem.getMeaning())) {
                conflictStringBuilder .append("\nMeaning: " + conflicItem.getMeaning() + " -> " + newItem.getMeaning());
            }
            //TODO: make sure this match works even when new item has out of order string
            if (!newItem.getSourceString().equals(conflicItem.getSourceString())) {
                conflictStringBuilder .append("\nSource: " + conflicItem.getSourceString()+ " -> " + newItem.getSourceString());
            }
            AlertDialog.Builder conflictAlertBuilder = new AlertDialog.Builder(context);
            conflictAlertBuilder.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.cancel())
                    .setPositiveButton("Overwrite", ((dialogInterface, i) -> {
                        if(original!=null){
                            kanjiWritingDaoHelper.delete(original);
                        }
                        kanjiWritingDaoHelper.insert(newItem);
                    }));
        }
    }
}
