package kuesuchan.jpns.database.entity;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.HashSet;
import java.util.Set;

import kuesuchan.jpns.database.Converters;

@Entity
public class KanjiWriting {

    public static enum Columns{
        kanji,
        japanese_reading,
        phonetic_reading,
        strokes,
        meaning
    }

    @PrimaryKey
    @NonNull
    private  String kanji;

    @NonNull
    private String japanese_reading;

    @NonNull
    private String phonetic_reading;

    private int strokes;

    @NonNull
    private String meaning;

    public KanjiWriting(@NonNull String kanji, @NonNull String japanese_reading, @NonNull String phonetic_reading, int strokes, @NonNull String meaning) {
        this.kanji = kanji.trim();
        this.japanese_reading = japanese_reading.trim();
        this.phonetic_reading = phonetic_reading.trim();
        this.strokes = strokes;
        this.meaning = meaning.trim();
    }

    @Override
    public String toString() {
        return "Kanji: '" + kanji + "'" +
                "\nJapanese_reading: '" + japanese_reading + "'" +
                "\nPhonetic_reading: '" + phonetic_reading + "'" +
                "\nStrokes: " + strokes +
                "\nMeaning: '" + meaning + "'";
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
        this.kanji = kanji.trim();
    }

    public String getJapanese_reading() {
        return japanese_reading;
    }

    public void setJapanese_reading(String japanese_reading) {
        this.japanese_reading = japanese_reading.trim();
    }

    public String getPhonetic_reading() {
        return phonetic_reading;
    }

    public void setPhonetic_reading(String phonetic_reading) {
        this.phonetic_reading = phonetic_reading.trim();
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
        this.meaning = meaning.trim();
    }

}
