package kuesuchan.jpns.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import kuesuchan.jpns.R;

public class HomeActivity extends AppCompatActivity{
    private Button kanjiWritingButton, flashCardButton, practiceButton,
            databaseButton, statisticsButton, settingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setupViews();

    }

    private void setupViews() {
        databaseButton = findViewById(R.id.databaseButton);
        databaseButton.setOnClickListener(view -> startActivity(new Intent(this, DatabaseActivity.class)));

        flashCardButton = findViewById(R.id.flashCardButton);
        flashCardButton.setOnClickListener( view -> startActivity(new Intent(this, SetupActivity.class).putExtra("mode", getString(R.string.flash_card_label))));

        practiceButton = findViewById(R.id.practiceButton);
        practiceButton.setOnClickListener(view -> startActivity(new Intent(this, SetupActivity.class).putExtra("mode", getString(R.string.practice_label))));

        kanjiWritingButton = findViewById(R.id.kanjiWritingButton);
        kanjiWritingButton.setOnClickListener( view -> startActivity(new Intent(this, SetupActivity.class).putExtra("mode", getString(R.string.kanji_writing_label))));

        statisticsButton = findViewById(R.id.statisticsButton);
        statisticsButton.setOnClickListener(view -> startActivity(new Intent(this, StatisticsActivity.class)));

        settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(view -> startActivity(new Intent(this, SettingsActivity.class)));
    }

}