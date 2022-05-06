package kuesuchan.jpns.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;

import kuesuchan.jpns.R;
import kuesuchan.jpns.database.dao.KanjiWritingDao;
import kuesuchan.jpns.database.dao.VocabularyDao;
import kuesuchan.jpns.database.AppDatabase;
import kuesuchan.jpns.database.dao.helper.KanjiWritingDaoHelper;
import kuesuchan.jpns.database.dao.helper.SourceDaoHelper;
import kuesuchan.jpns.database.dao.helper.VocabularyDaoHelper;
import kuesuchan.jpns.database.entity.Source;

public class SetupActivity extends AppCompatActivity{

    private CheckBox sectionCheckBox1,sectionCheckBox2,sectionCheckBox3,sectionCheckBox4,sectionCheckBox5,
            sectionCheckBox6, sectionCheckBox7, sectionCheckBox8, sectionCheckBox9, sectionCheckBox10,
            sectionCheckBox11, sectionCheckBox12, sectionCheckBox13, sectionCheckBox14, sectionCheckBox15,
            sectionCheckBox16, sectionCheckBox17, sectionCheckBox18, sectionCheckBox19, sectionCheckBox20,
            sectionCheckBox21,sectionCheckBox22,sectionCheckBox23, sectionCheckBox24, sectionCheckBoxAll,
            englishPromptCheckBox,kanaPromptCheckBox,kanjiPromptCheckBox, activityTypeMultipleChoiceCheckBox,
            activityTypeMatchingCheckBox, activityTypeKanjiWritingCheckBox, activityTypeTextAnswerCheckBox,
            gradedCheckBox;

    private TextView setupTitleTextView;

    private List<CheckBox> sectionCheckboxList;

    private ConstraintLayout activityChooserConstraintLayout;

    private Spinner sourceSpinner, amountSpinner;

    private Button proceedButton;
    private String mode;

    private AppDatabase db;
    private VocabularyDaoHelper vocabularyDaoHelper;
    private KanjiWritingDaoHelper kanjiWritingDaoHelper;
    private SourceDaoHelper sourceDaoHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, getString(R.string.db_name)).build();
        vocabularyDaoHelper = db.getVocabularyDaoHelper();
        kanjiWritingDaoHelper = db.getKanjiWritingDaoHelper();
        sourceDaoHelper = db.getSourceDaoHelper();
        Intent intent = getIntent();
        mode = intent.getStringExtra("mode");
        setupViews(mode);
    }

    private void setupViews(String mode) {
        setupTitleTextView = findViewById(R.id.setupTitleTextView);
        setupTitleTextView.setText(mode);
        proceedButton = findViewById(R.id.setupButton);
        proceedButton.setOnClickListener( view -> finishSetup());

        //instantiate all views
        if(mode.equals(getString(R.string.practice_label))){
            activityTypeMultipleChoiceCheckBox=findViewById(R.id.activityTypeMultipleChoiceCheckBox);
            activityTypeMatchingCheckBox=findViewById(R.id.activityTypeMatchingCheckBox);
            activityTypeKanjiWritingCheckBox=findViewById(R.id.activityTypeKanjiWritingCheckBox);
            activityTypeTextAnswerCheckBox=findViewById(R.id.activityTypeTextAnswerCheckBox);
            gradedCheckBox=findViewById(R.id.gradedCheckBox);
            //TODO: remove after statistics is set up
            gradedCheckBox.setVisibility(View.GONE);
        } else {
            activityChooserConstraintLayout = findViewById(R.id.activityChooserConstraintLayout);
            activityChooserConstraintLayout.setVisibility(View.GONE);
        }


        //TODO: maybe a list?
        englishPromptCheckBox=findViewById(R.id.englishPromptCheckBox);
        kanaPromptCheckBox=findViewById(R.id.kanaPromptCheckBox);
        kanjiPromptCheckBox=findViewById(R.id.kanjiPromptCheckBox);

        amountSpinner = findViewById(R.id.amountSpinner);

        sectionCheckBox1=findViewById(R.id.sectionCheckBox1);
        sectionCheckBox2=findViewById(R.id.sectionCheckBox2);
        sectionCheckBox3=findViewById(R.id.sectionCheckBox3);
        sectionCheckBox4=findViewById(R.id.sectionCheckBox4);
        sectionCheckBox5=findViewById(R.id.sectionCheckBox5);
        sectionCheckBox6=findViewById(R.id.sectionCheckBox6);
        sectionCheckBox7=findViewById(R.id.sectionCheckBox7);
        sectionCheckBox8=findViewById(R.id.sectionCheckBox8);
        sectionCheckBox9=findViewById(R.id.sectionCheckBox9);
        sectionCheckBox10=findViewById(R.id.sectionCheckBox10);
        sectionCheckBox11=findViewById(R.id.sectionCheckBox11);
        sectionCheckBox12=findViewById(R.id.sectionCheckBox12);
        sectionCheckBox13=findViewById(R.id.sectionCheckBox13);
        sectionCheckBox14=findViewById(R.id.sectionCheckBox14);
        sectionCheckBox15=findViewById(R.id.sectionCheckBox15);
        sectionCheckBox16=findViewById(R.id.sectionCheckBox16);
        sectionCheckBox17=findViewById(R.id.sectionCheckBox17);
        sectionCheckBox18=findViewById(R.id.sectionCheckBox18);
        sectionCheckBox19=findViewById(R.id.sectionCheckBox19);
        sectionCheckBox20=findViewById(R.id.sectionCheckBox20);
        sectionCheckBox21=findViewById(R.id.sectionCheckBox21);
        sectionCheckBox22=findViewById(R.id.sectionCheckBox22);
        sectionCheckBox23=findViewById(R.id.sectionCheckBox23);
        sectionCheckBox24=findViewById(R.id.sectionCheckBox24);

        sectionCheckboxList = new ArrayList<>();
        sectionCheckboxList.add(sectionCheckBox1);
        sectionCheckboxList.add(sectionCheckBox2);
        sectionCheckboxList.add(sectionCheckBox3);
        sectionCheckboxList.add(sectionCheckBox4);
        sectionCheckboxList.add(sectionCheckBox5);
        sectionCheckboxList.add(sectionCheckBox6);
        sectionCheckboxList.add(sectionCheckBox7);
        sectionCheckboxList.add(sectionCheckBox8);
        sectionCheckboxList.add(sectionCheckBox9);
        sectionCheckboxList.add(sectionCheckBox10);
        sectionCheckboxList.add(sectionCheckBox11);
        sectionCheckboxList.add(sectionCheckBox12);
        sectionCheckboxList.add(sectionCheckBox13);
        sectionCheckboxList.add(sectionCheckBox14);
        sectionCheckboxList.add(sectionCheckBox15);
        sectionCheckboxList.add(sectionCheckBox16);
        sectionCheckboxList.add(sectionCheckBox17);
        sectionCheckboxList.add(sectionCheckBox18);
        sectionCheckboxList.add(sectionCheckBox19);
        sectionCheckboxList.add(sectionCheckBox20);
        sectionCheckboxList.add(sectionCheckBox21);
        sectionCheckboxList.add(sectionCheckBox22);
        sectionCheckboxList.add(sectionCheckBox23);
        sectionCheckboxList.add(sectionCheckBox24);

        sectionCheckBoxAll=findViewById(R.id.sectionCheckBoxAll);
        sectionCheckBoxAll.setOnCheckedChangeListener( (compoundButton, checked) -> sectionCheckboxList.stream().forEach(checkbox -> checkbox.setChecked(false)));

        sourceSpinner = findViewById(R.id.sourceSpinner);
        sourceSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, sourceDaoHelper.getSourceNames()));
        sourceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int index, long l) {
                //TODO: Populate view with Sections
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void finishSetup(){

        // validate views
            // check section
            // save section
            // check prompt
            // save prompt
            // check activities/graded
            // save ^^

        //proceed

        // gather info
//        private List<String> writingStyles;
//        private List<String> sections;
//        private List<String> activities;
//        private String source;
//        private boolean allSections;
//        private boolean graded;
//        private int amount;

        List<String> activities = new ArrayList<>();
        boolean graded = gradedCheckBox.isChecked();

        List<String> writingStyles = new ArrayList<>();
        List<String> sections = new ArrayList<>();
        String source = sourceSpinner.getSelectedItem().toString();
        int amount = Integer.parseInt(amountSpinner.getSelectedItem().toString());


        // confirm

        //go to activity based on mode

        db.close();
        startActivity(new Intent(this, FlashCardActivity.class));
    }

}