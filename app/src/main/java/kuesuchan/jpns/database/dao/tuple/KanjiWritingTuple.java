package kuesuchan.jpns.database.dao.tuple;

import androidx.room.ColumnInfo;

public class KanjiWritingTuple {

    @ColumnInfo(name = "kanji")
    public String kanji;

    @ColumnInfo(name = "japanese_reading")
    public String japanese_reading;

    @ColumnInfo(name = "phonetic_reading")
    public String phonetic_reading;

    @ColumnInfo(name = "strokes")
    public int strokes;

    @ColumnInfo(name = "meaning")
    public String meaning;
}
