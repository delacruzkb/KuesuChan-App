package kuesuchan.jpns.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import kuesuchan.jpns.database.Converters;

@Entity(primaryKeys = {"english","kana"})
public class Vocabulary {

    @NonNull
    private String english;

    @NonNull
    private String kana;

    @NonNull
    private String kanji;

    @NonNull
    private String help_text;

    @NonNull
    private Set<String> sources;

    public Vocabulary(@NonNull String english, @NonNull String kana, @NonNull String kanji, @NonNull String help_text, @NonNull String source) {
        this.english = english;
        this.kana = kana;
        this.kanji = kanji;
        this.help_text = help_text;
        sources = new HashSet<>();
        sources.add(source);

    }

    public Vocabulary(@NonNull String english, @NonNull String kana, @NonNull String kanji, @NonNull String help_text, @NonNull Set<String> sources) {
        this.english = english;
        this.kana = kana;
        this.kanji = kanji;
        this.help_text = help_text;
        this.sources = sources;
    }

    @Override
    public String toString() {
        return "English: '" + english + "'"+
                "\nKana: '" + kana + "'"+
                "\nKanji: '" + kanji + "'"+
                "\nHelp_text: '" + help_text + "'"+
                "\nSources: " + sources;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vocabulary that = (Vocabulary) o;
        return english.equalsIgnoreCase(that.english) && kana.equalsIgnoreCase(that.kana);
    }

    @NonNull
    public String getEnglish() {
        return english;
    }

    public void setEnglish(@NonNull String english) {
        this.english = english;
    }

    @NonNull
    public String getKana() {
        return kana;
    }

    public void setKana(@NonNull String kana) {
        this.kana = kana;
    }

    @NonNull
    public String getKanji() {
        return kanji;
    }

    public void setKanji(@NonNull String kanji) {
        this.kanji = kanji;
    }

    @NonNull
    public String getHelp_text() {
        return help_text;
    }

    public void setHelp_text(@NonNull String help_text) {
        this.help_text = help_text;
    }

    @NonNull
    public Set<String> getSources() {
        return sources;
    }

    public String getSourceString(){
        return Converters.stringSetToString(sources);
    }

    public void setSources(@NonNull Set<String> sources) {
        this.sources = sources;
    }

    public void setSourceFromString(@NonNull String sources) {
        this.sources = Converters.fromStringToStringSet(sources);
    }

    public void addSource(String source){
        sources.add(source);
    }

    public void addSources(Set<String> sources){
        this.sources.addAll(sources);
    }
}
