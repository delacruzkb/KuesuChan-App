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
    private String type;

    @NonNull
    private Set<String> sources;

    @NonNull
    private Set<Integer> sections;


    public Vocabulary(@NonNull String english, @NonNull String kana,@NonNull String kanji,@NonNull String help_text, @NonNull String type, @NonNull String source,@NonNull Integer section) {
        this.english = english;
        this.kana = kana;
        this.kanji = kanji;
        this.help_text = help_text;
        this.type = type;
        this.sources = new HashSet<>();
        this.sources.add(source);
        this.sections = new HashSet<>();
        this.sections.add(section);
    }

    public Vocabulary(@NonNull String english, @NonNull String kana,@NonNull String kanji,@NonNull String help_text, @NonNull String type, @NonNull Set<String> sources,@NonNull Set<Integer> sections) {
        this.english = english;
        this.kana = kana;
        this.kanji = kanji;
        this.help_text = help_text;
        this.type = type;
        this.sources = sources;
        this.sections = sections;
    }


    @Override
    public String toString() {
        return "English: " + english +
                "\nKana: " + kana +
                "\nKanji: " + kanji +
                "\nHelp_text: " + help_text +
                "\nType: " + type +
                "\nSources: " + sources +
                "\nSections: " + sections;
    }

    public boolean pkEquals(Vocabulary other){
        return english.equals(other.getEnglish()) && kana.equals(other.getKana());
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
    public String getType() {
        return type;
    }

    public void setType(@NonNull String type) {
        this.type = type;
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

    public void addSections(@NonNull Integer section){
        sections.add(section);
    }

    public void setSections(@NonNull Set<Integer> sections) {
        this.sections = sections;
    }
}
