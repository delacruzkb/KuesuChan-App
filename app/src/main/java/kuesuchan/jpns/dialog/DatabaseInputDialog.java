package kuesuchan.jpns.dialog;

import android.animation.TypeConverter;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.HashSet;
import java.util.Set;

import kuesuchan.jpns.R;
import kuesuchan.jpns.database.AppDatabase;
import kuesuchan.jpns.database.Converters;
import kuesuchan.jpns.database.dao.helper.KanjiWritingDaoHelper;
import kuesuchan.jpns.database.dao.helper.SourceDaoHelper;
import kuesuchan.jpns.database.dao.helper.VocabularyDaoHelper;
import kuesuchan.jpns.database.entity.KanjiWriting;
import kuesuchan.jpns.database.entity.Vocabulary;
import kuesuchan.jpns.database.tuple.SourceTuple;
import kuesuchan.jpns.util.KuesuChanUtil;

public class DatabaseInputDialog {

    private TextView columnLabel1, columnLabel2, columnLabel3, columnLabel4,
            columnLabel5, columnLabel6, columnLabel7;
    private EditText columnEditText1, columnEditText2, columnEditText3, columnEditText4,
            columnEditText5, columnEditText6, columnEditText7;

    private AlertDialog dialog;

    private AppDatabase.SearchableTable table;

    private Context context;

    private Object selectedObject;

    private SourceDaoHelper sourceDaoHelper;

    public DatabaseInputDialog(@NonNull Context context, Object selectedObject, @NonNull AppDatabase.SearchableTable table) {
        this.table = table;
        this.context = context;
        this.selectedObject = selectedObject;

        AppDatabase db = AppDatabase.getInstance(context);
        sourceDaoHelper = db.getSourceDaoHelper();

        View dialogFormView = LayoutInflater.from(context).inflate(R.layout.dialog_database_row_edit, null);
        initializeView(dialogFormView);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setView(dialogFormView)
                .setCancelable(false)
                .setNeutralButton("Cancel", (dialogInterface, i) -> dialogInterface.cancel())
                .setPositiveButton("Insert", (dialogInterface, i) -> {
                    AlertDialog.Builder innerBuilder = new AlertDialog.Builder(context);
                    innerBuilder.setTitle("Do you want to Update?");
                    innerBuilder.setMessage(getInput().toString());
                    innerBuilder.setPositiveButton("Confirm", (dialogInterface12, i12) -> {
                        //TODO: based on which table, Update
                        Toast.makeText(context, "UPDATED", Toast.LENGTH_SHORT).show();
                        insert();
                    });
                    innerBuilder.setNegativeButton("Cancel", (dialogInterface1, i1) -> dialogInterface1.cancel());
                    innerBuilder.create().show();
                });
        if (selectedObject != null) {
            builder.setNegativeButton("Delete", (dialogInterface, i) -> {
                AlertDialog.Builder innerBuilder = new AlertDialog.Builder(context);
                innerBuilder.setTitle("Do you want to Delete?");
                innerBuilder.setMessage(selectedObject.toString());
                innerBuilder.setPositiveButton("Confirm", (dialogInterface14, i14) -> {
                    Toast.makeText(context, "DELETED", Toast.LENGTH_SHORT).show();
                    switch (table) {
                        case Vocabulary:
                            AppDatabase.getInstance(context).getVocabularyDaoHelper().delete((Vocabulary) selectedObject);
                            break;
                        case Kanji_Writing:
                            AppDatabase.getInstance(context).getKanjiWritingDaoHelper().delete((KanjiWriting) selectedObject);
                            break;
                    }
                });
                innerBuilder.setNegativeButton("Cancel", (dialogInterface13, i13) -> dialogInterface13.cancel());
                innerBuilder.create().show();
            });
        }
        dialog = builder.create();
    }

    public void show() {
        dialog.show();
    }

    private void initializeView(View view) {
        columnLabel1 = view.findViewById(R.id.columnLabel1);
        columnLabel2 = view.findViewById(R.id.columnLabel2);
        columnLabel3 = view.findViewById(R.id.columnLabel3);
        columnLabel4 = view.findViewById(R.id.columnLabel4);
        columnLabel5 = view.findViewById(R.id.columnLabel5);
        columnLabel6 = view.findViewById(R.id.columnLabel6);
        columnLabel7 = view.findViewById(R.id.columnLabel7);
        columnEditText1 = view.findViewById(R.id.columnEditText1);
        columnEditText2 = view.findViewById(R.id.columnEditText2);
        columnEditText3 = view.findViewById(R.id.columnEditText3);
        columnEditText4 = view.findViewById(R.id.columnEditText4);
        columnEditText5 = view.findViewById(R.id.columnEditText5);
        columnEditText6 = view.findViewById(R.id.columnEditText6);
        columnEditText7 = view.findViewById(R.id.columnEditText7);

        switch (table) {
            case Vocabulary:
                loadVocabularyData((Vocabulary) selectedObject);
                break;
            case Kanji_Writing:
                loadKanjiWritingData((KanjiWriting) selectedObject);
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    private void loadVocabularyData(Vocabulary vocabulary) {
        columnLabel1.setText(Vocabulary.Columns.english.name() + "*");
        columnLabel2.setText(Vocabulary.Columns.kana.name() + "*");
        columnLabel3.setText(Vocabulary.Columns.kanji.name());
        columnLabel4.setText(Vocabulary.Columns.help_text.name());
        columnLabel5.setText("Sources (Source"+ KuesuChanUtil.SOURCE_TUPLE_DELIM +"Section)*");
        columnLabel6.setVisibility(View.GONE);
        columnEditText6.setVisibility(View.GONE);
        columnLabel7.setVisibility(View.GONE);
        columnEditText7.setVisibility(View.GONE);

        if (vocabulary != null) {
            columnEditText1.setText(vocabulary.getEnglish());
            columnEditText2.setText(vocabulary.getKana());
            columnEditText3.setText(vocabulary.getKanji());
            columnEditText4.setText(vocabulary.getHelp_text());
            columnEditText5.setText(Converters.sourceTupleSetToString(new HashSet<>(sourceDaoHelper.getSourceTuplesBySourceId(vocabulary.getSource_id()))));
        }
    }

    @SuppressLint("SetTextI18n")
    private void loadKanjiWritingData(KanjiWriting kanjiWriting) {
        columnLabel1.setText(KanjiWriting.Columns.kanji.name() + "*");
        columnLabel2.setText(KanjiWriting.Columns.japanese_reading.name());
        columnLabel3.setText(KanjiWriting.Columns.phonetic_reading.name());
        columnLabel4.setText(KanjiWriting.Columns.strokes.name());
        columnEditText4.setInputType(InputType.TYPE_CLASS_NUMBER);
        columnLabel5.setText(KanjiWriting.Columns.meaning.name());
        columnLabel6.setText("Sources (Source"+ KuesuChanUtil.SOURCE_TUPLE_DELIM +"Section)*");
        columnLabel7.setVisibility(View.GONE);
        columnEditText7.setVisibility(View.GONE);

        if (kanjiWriting != null) {
            columnEditText1.setText(kanjiWriting.getKanji());
            columnEditText2.setText(kanjiWriting.getJapanese_reading());
            columnEditText3.setText(kanjiWriting.getPhonetic_reading());
            columnEditText4.setText(Integer.toString(kanjiWriting.getStrokes()));
            columnEditText5.setText(kanjiWriting.getMeaning());
            columnEditText6.setText(Converters.sourceTupleSetToString(new HashSet<>(sourceDaoHelper.getSourceTuplesBySourceId(kanjiWriting.getKanji()))));
        }

    }

    private Object getInput() {
        switch (table) {
            case Vocabulary:
                return getVocabularyInput();
            case Kanji_Writing:
                return getKanjiWritingInput();
        }
        return null;
    }

    private void insert() {
        switch (table) {
            case Vocabulary:
                insertVocabulary();
                break;
            case Kanji_Writing:
                insertKanjiWriting();
                break;
        }
    }

    private void insertVocabulary(){
        VocabularyDaoHelper vocabularyDaoHelper = AppDatabase.getInstance(context).getVocabularyDaoHelper();
        Vocabulary original = (Vocabulary) selectedObject;
        Vocabulary newItem = getVocabularyInput();
        Vocabulary conflictItem = vocabularyDaoHelper.getVocabulary(newItem.getEnglish(), newItem.getKana());
        Set<SourceTuple> sourceTupleSet = getSourceInput(columnEditText5);

        if (original == null && conflictItem == null) { // insert with no conflicts
            vocabularyDaoHelper.insert(newItem, sourceTupleSet);
        } else if (original == null && conflictItem != null) { // insert with conflicts
            handleVocabularyConflict(vocabularyDaoHelper, original, newItem, conflictItem, sourceTupleSet);
        } else if (original != null && original.equals(newItem)) { // update Non-PK
            vocabularyDaoHelper.update(newItem, sourceTupleSet);
        } else if (original != null && !original.equals(newItem) && !newItem.equals(conflictItem)) { // update PK with no conflict
            vocabularyDaoHelper.delete(original);
            vocabularyDaoHelper.insert(newItem, sourceTupleSet);
        } else if (original != null && !original.equals(newItem) && newItem.equals(conflictItem)) {  // update PK with conflicts
            handleVocabularyConflict(vocabularyDaoHelper, original, newItem, conflictItem, sourceTupleSet);
        }

    }

    private void insertKanjiWriting(){
        KanjiWritingDaoHelper kanjiWritingDaoHelper = AppDatabase.getInstance(context).getKanjiWritingDaoHelper();
        KanjiWriting original= (KanjiWriting) selectedObject;
        KanjiWriting newItem = getKanjiWritingInput();
        KanjiWriting conflictItem = kanjiWritingDaoHelper.getKanjiWriting(newItem.getKanji());
        Set<SourceTuple> sourceTupleSet = getSourceInput(columnEditText6);

        if (original == null && conflictItem == null) { // insert with no conflicts
            kanjiWritingDaoHelper.insert(newItem, sourceTupleSet);
        } else if (original == null && conflictItem != null) { // insert with conflicts
            handleKanjiWritingConflict(kanjiWritingDaoHelper, original, newItem, conflictItem, sourceTupleSet);
        } else if (original != null && original.equals(newItem)) { // update Non-PK
            kanjiWritingDaoHelper.update(newItem, sourceTupleSet);
        } else if (original != null && !original.equals(newItem) && !newItem.equals(conflictItem)) { // update PK with no conflict
            kanjiWritingDaoHelper.delete(original);
            kanjiWritingDaoHelper.insert(newItem, sourceTupleSet);
        } else if (original != null && !original.equals(newItem) && newItem.equals(conflictItem)) {  // update PK with conflicts
            handleKanjiWritingConflict(kanjiWritingDaoHelper, original, newItem, conflictItem, sourceTupleSet);
        }
    }

    private void handleVocabularyConflict(VocabularyDaoHelper vocabularyDaoHelper, Vocabulary original, Vocabulary newItem, Vocabulary conflictItem, Set<SourceTuple> newSourceTupleSet) {
        StringBuilder conflictStringBuilder = new StringBuilder();
        conflictStringBuilder.append("English : " + conflictItem.getEnglish())
                .append("\nKana : " + conflictItem.getKana());
        if (!newItem.getKanji().equals(conflictItem.getKanji())) {
            conflictStringBuilder.append("\nKanji: " + conflictItem.getKanji() + " -> " + newItem.getKanji());
        }
        if (!newItem.getHelp_text().equals(conflictItem.getHelp_text())) {
            conflictStringBuilder.append("\nHelp Text: " + conflictItem.getHelp_text() + " -> " + newItem.getHelp_text());
        }
        String conflictSourceString = Converters.sourceTupleSetToString(new HashSet<>( sourceDaoHelper.getSourceTuplesBySourceId(conflictItem.getSource_id())));
        String newSourceString = Converters.sourceTupleSetToString(newSourceTupleSet);
        if (!newSourceString.equals(conflictSourceString)) {
            conflictStringBuilder.append("\nSources: " + conflictSourceString + " -> " + newSourceString);
        }

        AlertDialog.Builder conflictAlertBuilder = new AlertDialog.Builder(context);
        conflictAlertBuilder.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.cancel())
                .setPositiveButton("Overwrite", ((dialogInterface, i) -> {
                    if (original != null) {
                        vocabularyDaoHelper.delete(original);
                    }
                    vocabularyDaoHelper.insert(newItem, newSourceTupleSet);
                }));
        conflictAlertBuilder.show();
    }

    private void handleKanjiWritingConflict(KanjiWritingDaoHelper kanjiWritingDaoHelper, KanjiWriting original, KanjiWriting newItem, KanjiWriting conflictItem, Set<SourceTuple> newSourceTupleSet) {
        StringBuilder conflictStringBuilder = new StringBuilder();
        conflictStringBuilder.append("Kanji : " + conflictItem.getKanji());
        if (!newItem.getJapanese_reading().equals(newItem.getJapanese_reading())) {
            conflictStringBuilder.append("\nJapanese Reading: " + conflictItem.getJapanese_reading() + " -> " + newItem.getJapanese_reading());
        }
        if (!newItem.getPhonetic_reading().equals(conflictItem.getPhonetic_reading())) {
            conflictStringBuilder.append("\nPhonetic Reading: " + conflictItem.getPhonetic_reading() + " -> " + newItem.getPhonetic_reading());
        }
        if (newItem.getStrokes() != conflictItem.getStrokes()) {
            conflictStringBuilder.append("\nStrokes: " + conflictItem.getStrokes() + " -> " + newItem.getStrokes());
        }
        if (!newItem.getMeaning().equals(conflictItem.getMeaning())) {
            conflictStringBuilder.append("\nMeaning: " + conflictItem.getMeaning() + " -> " + newItem.getMeaning());
        }
        String conflictSourceString = Converters.sourceTupleSetToString(new HashSet<>( sourceDaoHelper.getSourceTuplesBySourceId(conflictItem.getKanji())));
        String newSourceString = Converters.sourceTupleSetToString(newSourceTupleSet);
        if (!newSourceString.equals(conflictSourceString)) {
            conflictStringBuilder.append("\nSources: " + conflictSourceString + " -> " + newSourceString);
        }
        AlertDialog.Builder conflictAlertBuilder = new AlertDialog.Builder(context);
        conflictAlertBuilder.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.cancel())
                .setPositiveButton("Overwrite", ((dialogInterface, i) -> {
                    if (original != null) {
                        kanjiWritingDaoHelper.delete(original);
                    }
                    kanjiWritingDaoHelper.insert(newItem, newSourceTupleSet);
                }));
    }

    private Vocabulary getVocabularyInput() {
        String english = columnEditText1.getText().toString();
        String kana = columnEditText2.getText().toString();
        if (english.trim().length() == 0 || kana.trim().length() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Missing Data")
                    .setMessage("English and Kana are required fields");
        }
        return new Vocabulary(english,
                kana,
                columnEditText3.getText().toString(),
                columnEditText4.getText().toString());
    }

    private KanjiWriting getKanjiWritingInput() {
        String kanji = columnEditText1.getText().toString();

        if (kanji.trim().length() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Missing Data")
                    .setMessage("Kanji is a required field");
        }
        return new KanjiWriting(kanji,
                columnEditText2.getText().toString(),
                columnEditText3.getText().toString(),
                Integer.parseInt(columnEditText4.getText().toString()),
                columnEditText5.getText().toString());
    }


    private Set<SourceTuple> getSourceInput(EditText editText){
        return Converters.fromStringToSourceTupleSet(editText.getText().toString());
    }
}
