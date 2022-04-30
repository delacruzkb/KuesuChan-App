package kuesuchan.jpns.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import kuesuchan.jpns.R;
import kuesuchan.jpns.database.entity.Vocabulary;
import kuesuchan.jpns.listeners.OnSwipeTouchListener;

public class FlashCardActivity extends AppCompatActivity{

    private TextView flashCardTextView, flashCardHelpTextView;

    private final String[] writing_styles={"English", "かな", "漢字"};
    private List<Vocabulary> vocabularyList;
    private int currentCardIndex;
    private int currentWritingStyleIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_card);
        setupViews();
        currentCardIndex =0;
        //TODO: figure out prompt from intent
        currentWritingStyleIndex=0;

        //TODO: get from intent
        vocabularyList = new ArrayList<>();
        loadCard(0);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupViews() {
        flashCardHelpTextView = findViewById(R.id.flashCardHelpTextView);
        flashCardTextView = findViewById(R.id.flashCardTextView);
        flashCardTextView.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeDown() {
                flipCard(false);
            }

            @Override
            public void onSwipeUp() {
                flipCard(true);
            }

            @Override
            public void onSwipeLeft() {
                nextCard();
            }

            @Override
            public void onSwipeRight() {
                prevCard();
            }
        });
    }

    private void nextCard(){
        if(currentCardIndex < vocabularyList.size()-1){
            loadCard(currentCardIndex +1);
        }
    }

    private void prevCard(){
        if(currentCardIndex >0){
            loadCard(currentCardIndex -1);
        }
    }

    private void loadCard(int index){
        Vocabulary vocabulary = vocabularyList.get(index);
        flashCardHelpTextView.setText(vocabulary.getHelp_text());

        //based off of prompt
        flashCardTextView.setText(vocabulary.getEnglish());
        currentCardIndex = index;
    }

    //make more efficient
    private void flipCard(boolean up){
        if(up){
            switchWritingStyle( (currentWritingStyleIndex+1) % 3);
        } else {
            switchWritingStyle( (currentWritingStyleIndex+2) % 3);
        }

    }

    private void switchWritingStyle(int index){
        if(index==0){
            flashCardTextView.setText(vocabularyList.get(currentCardIndex).getEnglish());
            currentWritingStyleIndex=0;
        }
        if(index==1){
            flashCardTextView.setText(vocabularyList.get(currentCardIndex).getKana());
            currentWritingStyleIndex=1;
        }
        if(index==2){
            flashCardTextView.setText(vocabularyList.get(currentCardIndex).getKanji());
            currentWritingStyleIndex=2;
        }
    }

}