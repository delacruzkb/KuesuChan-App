package kuesuchan.jpns.database.entity;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import kuesuchan.jpns.database.Converters;

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

    public KanjiWriting(@NonNull String kanji, String japanese_reading, String phonetic_reading, int strokes, String meaning, @NonNull String source) {
        this.kanji = kanji;
        this.japanese_reading = japanese_reading;
        this.phonetic_reading = phonetic_reading;
        this.strokes = strokes;
        this.meaning = meaning;
        sources = new HashSet<>();
        sources.add(source);
    }

    public KanjiWriting(@NonNull String kanji, String japanese_reading, String phonetic_reading, int strokes, String meaning, @NonNull Set<String> sources) {
        this.kanji = kanji;
        this.japanese_reading = japanese_reading;
        this.phonetic_reading = phonetic_reading;
        this.strokes = strokes;
        this.meaning = meaning;
        this.sources = sources;
    }

    @Override
    public String toString() {
        return "Kanji: '" + kanji + "'" +
                "\nJapanese_reading: '" + japanese_reading + "'" +
                "\nPhonetic_reading: '" + phonetic_reading + "'" +
                "\nStrokes: " + strokes +
                "\nMeaning: '" + meaning + "'" +
                "\nsources: " + sources;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KanjiWriting that = (KanjiWriting) o;
        return kanji.equals(that.kanji);
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

    public void setSources(@NonNull Set<String> sources) {
        this.sources = sources;
    }

    public void addSource(@NonNull String source){
        this.sources.add(source);
    }

    public void addSources(Set<String> sources){
        this.sources.addAll(sources);
    }

    public String getSourceString(){
        return Converters.stringSetToString(sources);
    }

    public void setSourceFromString(@NonNull String sources) {
        this.sources = Converters.fromStringToStringSet(sources);
    }
}
