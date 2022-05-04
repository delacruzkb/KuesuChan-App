package kuesuchan.jpns.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import kuesuchan.jpns.util.KuesuChanUtil;

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
    private String source_id;

    public Vocabulary(@NonNull String english, @NonNull String kana, @NonNull String kanji, @NonNull String help_text) {
        this.english = english.trim();
        this.kana = kana.trim();
        this.kanji = kanji.trim();
        this.help_text = help_text.trim();
        this.source_id = this.english + KuesuChanUtil.VOCABULARY_ID_DELIM + this.kana;
    }

    @Override
    public String toString() {
        return "English: '" + english + "'"+
                "\nKana: '" + kana + "'"+
                "\nKanji: '" + kanji + "'"+
                "\nHelp_text: '" + help_text + "'";
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
        this.english = english.trim();
        this.source_id = this.english + KuesuChanUtil.VOCABULARY_ID_DELIM + this.kana;
    }

    @NonNull
    public String getKana() {
        return kana;
    }

    public void setKana(@NonNull String kana) {
        this.kana = kana.trim();
        this.source_id = this.english + KuesuChanUtil.VOCABULARY_ID_DELIM + this.kana;
    }

    @NonNull
    public String getKanji() {
        return kanji;
    }

    public void setKanji(@NonNull String kanji) {
        this.kanji = kanji.trim();
    }

    @NonNull
    public String getHelp_text() {
        return help_text;
    }

    public void setHelp_text(@NonNull String help_text) {
        this.help_text = help_text.trim();
    }

    @NonNull
    public String getSource_id() {
        return source_id;
    }
}
