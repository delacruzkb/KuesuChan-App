package kuesuchan.jpns.tuple;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;

public class FlashCardTuple {

    @ColumnInfo(name = "english")
    public String english;

    @ColumnInfo(name = "kana")
    public String kana;

    @ColumnInfo(name = "kanji")
    public String kanji;

    @ColumnInfo(name = "help_text")
    public String help_text;

}
