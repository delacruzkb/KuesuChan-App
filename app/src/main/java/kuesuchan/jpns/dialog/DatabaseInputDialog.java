package kuesuchan.jpns.dialog;

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

import kuesuchan.jpns.R;
import kuesuchan.jpns.database.AppDatabase;
import kuesuchan.jpns.database.Converters;
import kuesuchan.jpns.database.dao.helper.DaoHelper;
import kuesuchan.jpns.database.dao.helper.KanjiWritingDaoHelper;
import kuesuchan.jpns.database.dao.helper.VocabularyDaoHelper;
import kuesuchan.jpns.database.entity.KanjiWriting;
import kuesuchan.jpns.database.entity.Vocabulary;

public class DatabaseInputDialog {

    private TextView columnLabel1, columnLabel2, columnLabel3, columnLabel4,
            columnLabel5, columnLabel6, columnLabel7;
    private EditText columnEditText1, columnEditText2, columnEditText3, columnEditText4,
            columnEditText5, columnEditText6, columnEditText7;

    private AlertDialog dialog;

    private AppDatabase.Table table;

    private Context context;

    private Object selectedObject;

    public DatabaseInputDialog(@NonNull Context context, Object selectedObject, @NonNull AppDatabase.Table table) {
        this.table = table;
        this.context = context;
        this.selectedObject = selectedObject;

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
        columnLabel1.setText(VocabularyDaoHelper.Columns.english.name() + "*");
        columnLabel2.setText(VocabularyDaoHelper.Columns.kana.name() + "*");
        columnLabel3.setText(VocabularyDaoHelper.Columns.kanji.name());
        columnLabel4.setText(VocabularyDaoHelper.Columns.help_text.name());
        columnLabel5.setText(VocabularyDaoHelper.Columns.sources.name() + "(Source.Section, Source.Section, ...)");
        columnLabel6.setVisibility(View.GONE);
        columnEditText6.setVisibility(View.GONE);
        columnLabel7.setVisibility(View.GONE);
        columnEditText7.setVisibility(View.GONE);

        if (vocabulary != null) {
            columnEditText1.setText(vocabulary.getEnglish());
            columnEditText2.setText(vocabulary.getKana());
            columnEditText3.setText(vocabulary.getKanji());
            columnEditText4.setText(vocabulary.getHelp_text());
            columnEditText5.setText(vocabulary.getSourceString());
        }
    }

    @SuppressLint("SetTextI18n")
    private void loadKanjiWritingData(KanjiWriting kanjiWriting) {
        columnLabel1.setText(KanjiWritingDaoHelper.Columns.kanji.name() + "*");
        columnLabel2.setText(KanjiWritingDaoHelper.Columns.japanese_reading.name());
        columnLabel3.setText(KanjiWritingDaoHelper.Columns.phonetic_reading.name());
        columnLabel4.setText(KanjiWritingDaoHelper.Columns.strokes.name());
        columnEditText4.setInputType(InputType.TYPE_CLASS_NUMBER);
        columnLabel5.setText(KanjiWritingDaoHelper.Columns.meaning.name());
        columnLabel6.setText(KanjiWritingDaoHelper.Columns.sources.name() + "(Source.Section, Source.Section, ...)");
        columnLabel7.setVisibility(View.GONE);
        columnEditText7.setVisibility(View.GONE);

        if (kanjiWriting != null) {
            columnEditText1.setText(kanjiWriting.getKanji());
            columnEditText2.setText(kanjiWriting.getJapanese_reading());
            columnEditText3.setText(kanjiWriting.getPhonetic_reading());
            columnEditText4.setText(Integer.toString(kanjiWriting.getStrokes()));
            columnEditText5.setText(kanjiWriting.getMeaning());
            columnEditText6.setText(kanjiWriting.getSourceString());
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
                VocabularyDaoHelper vocabularyDaoHelper = AppDatabase.getInstance(context).getVocabularyDaoHelper();
                Vocabulary originalVocabulary = (Vocabulary) selectedObject;
                Vocabulary newItemVocabulary = getVocabularyInput();
                Vocabulary conflictItemVocabulary = vocabularyDaoHelper.getVocabulary(getVocabularyInput().getEnglish(), getVocabularyInput().getKana());
                if (insertElseConflict(vocabularyDaoHelper, originalVocabulary, newItemVocabulary, conflictItemVocabulary)) {
                    handleVocabularyConflict(vocabularyDaoHelper, originalVocabulary, newItemVocabulary, conflictItemVocabulary);
                }
                break;
            case Kanji_Writing:
                KanjiWritingDaoHelper kanjiWritingDaoHelper = AppDatabase.getInstance(context).getKanjiWritingDaoHelper();
                KanjiWriting originalKanjiWriting = (KanjiWriting) selectedObject;
                KanjiWriting newItemKanjiWriting = getKanjiWritingInput();
                KanjiWriting conflictItemKanjiWriting = kanjiWritingDaoHelper.getKanjiWriting(newItemKanjiWriting.getKanji());
                if (insertElseConflict(kanjiWritingDaoHelper, originalKanjiWriting, newItemKanjiWriting, conflictItemKanjiWriting)) {
                    handleKanjiWritingConflict(kanjiWritingDaoHelper, originalKanjiWriting, newItemKanjiWriting, conflictItemKanjiWriting);
                }
                break;
        }
    }

    private boolean insertElseConflict(DaoHelper daoHelper, Object original, Object newItem, Object conflictItem) {
        boolean conflict = false;

        //TODO: test all of these
        if (original == null && conflictItem == null) { // insert with no conflicts
            daoHelper.insert(newItem);
        } else if (original == null && conflictItem != null) { // insert with conflicts
            conflict = true;
        } else if (original != null && original.equals(newItem)) { // update Non-PK
            daoHelper.update(newItem);
        } else if (original != null && !original.equals(newItem) && !newItem.equals(conflictItem)) { // update PK with no conflict
            daoHelper.delete(original);
            daoHelper.insert(newItem);
        } else if (original != null && !original.equals(newItem) && newItem.equals(conflictItem)) {  // update PK with conflicts
            conflict = true;
        }
        return conflict;
    }


    private void handleVocabularyConflict(VocabularyDaoHelper vocabularyDaoHelper, Vocabulary original, Vocabulary newItem, Vocabulary conflictItem) {
        StringBuilder conflictStringBuilder = new StringBuilder();
        conflictStringBuilder.append("English : " + conflictItem.getEnglish())
                .append("\nKana : " + conflictItem.getKana());
        if (!newItem.getKanji().equals(conflictItem.getKanji())) {
            conflictStringBuilder.append("\nKanji: " + conflictItem.getKanji() + " -> " + newItem.getKanji());
        }
        if (!newItem.getHelp_text().equals(conflictItem.getHelp_text())) {
            conflictStringBuilder.append("\nHelp Text: " + conflictItem.getHelp_text() + " -> " + newItem.getHelp_text());
        }
        //TODO: make sure this match works even when new item has out of order string
        if (!newItem.getSources().containsAll(conflictItem.getSources())) {
            conflictStringBuilder.append("\nSource: " + conflictItem.getSourceString() + " -> " + newItem.getSourceString());
        }
        AlertDialog.Builder conflictAlertBuilder = new AlertDialog.Builder(context);
        conflictAlertBuilder.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.cancel())
                .setPositiveButton("Overwrite", ((dialogInterface, i) -> {
                    if (original != null) {
                        vocabularyDaoHelper.delete(original);
                    }
                    vocabularyDaoHelper.insert(newItem);
                }));
        conflictAlertBuilder.show();
    }

    private void handleKanjiWritingConflict(KanjiWritingDaoHelper kanjiWritingDaoHelper, KanjiWriting original, KanjiWriting newItem, KanjiWriting conflictItem) {
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
        //TODO: make sure this match works even when new item has out of order string
        if (!newItem.getSourceString().equals(conflictItem.getSourceString())) {
            conflictStringBuilder.append("\nSource: " + conflictItem.getSourceString() + " -> " + newItem.getSourceString());
        }
        AlertDialog.Builder conflictAlertBuilder = new AlertDialog.Builder(context);
        conflictAlertBuilder.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.cancel())
                .setPositiveButton("Overwrite", ((dialogInterface, i) -> {
                    if (original != null) {
                        kanjiWritingDaoHelper.delete(original);
                    }
                    kanjiWritingDaoHelper.insert(newItem);
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
                columnEditText4.getText().toString(),
                Converters.fromStringToStringSet(columnEditText5.getText().toString()));
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
                columnEditText5.getText().toString(),
                Converters.fromStringToStringSet(columnEditText6.getText().toString()));
    }
}
