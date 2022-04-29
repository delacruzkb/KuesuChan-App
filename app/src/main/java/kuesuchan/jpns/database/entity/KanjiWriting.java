package kuesuchan.jpns.database.entity;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class KanjiWriting {
    @PrimaryKey
    @NonNull
    private  String kanji;

    private String japanese_reading;

    private String phonetic_reading;

    private int strokes;

    private String meaning;

    @NonNull
    private Set<String> sources;

    @NonNull
    private Set<Integer> sections;

    public KanjiWriting(@NonNull String kanji, @NonNull String japanese_reading,@NonNull  String phonetic_reading, int strokes,@NonNull  String meaning, @NonNull String source, @NonNull Integer section) {
        this.kanji = kanji;
        this.japanese_reading = japanese_reading;
        this.phonetic_reading = phonetic_reading;
        this.strokes = strokes;
        this.meaning = meaning;
        this.sources = new HashSet<>();
        this.sources.add(source);
        this.sections = new HashSet<>();
        this.sections.add(section);
    }

    public KanjiWriting(@NonNull String kanji, @NonNull String japanese_reading, @NonNull String phonetic_reading, int strokes, @NonNull String meaning, @NonNull Set<String> sources, @NonNull Set<Integer> sections) {
        this.kanji = kanji;
        this.japanese_reading = japanese_reading;
        this.phonetic_reading = phonetic_reading;
        this.strokes = strokes;
        this.meaning = meaning;
        this.sources = sources;
        this.sections = sections;
    }



    @Override
    public String toString() {
        return "Kanji: " + kanji +
                "\nJapanese_reading: " + japanese_reading +
                "\nPhonetic_reading: " + phonetic_reading +
                "\nStrokes: " + strokes +
                "\nMeaning: " + meaning +
                "\nSources: " + sources +
                "\nSections: " + sections;
    }

    public boolean pkEquals(KanjiWriting other){
        return kanji.equals(other.getKanji());
    }

    @NonNull
    public String getKanji() {
        return kanji;
    }

    public void setKanji(@NonNull String kanji) {
        this.kanji = kanji;
    }

    public String getJapanese_reading() {
        return japanese_reading;
    }

    public void setJapanese_reading(String japanese_reading) {
        this.japanese_reading = japanese_reading;
    }

    public String getPhonetic_reading() {
        return phonetic_reading;
    }

    public void setPhonetic_reading(String phonetic_reading) {
        this.phonetic_reading = phonetic_reading;
    }

    public int getStrokes() {
        return strokes;
    }

    public void setStrokes(int strokes) {
        this.strokes = strokes;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    @NonNull
    public Set<String> getSources() {
        return sources;
    }

    public void addSource(@NonNull String source){
        sources.add(source);
    }

    public void setSources(@NonNull Set<String> sources) {
        this.sources = sources;
    }

    @NonNull
    public Set<Integer> getSections() {
        return sections;
    }

    public void addSection(@NonNull Integer section){
        sections.add(section);
    }

    public void setSections(@NonNull Set<Integer> sections) {
        this.sections = sections;
    }
}
