package kuesuchan.jpns.database.tuple;

import androidx.room.ColumnInfo;

import kuesuchan.jpns.util.KuesuChanUtil;

public class SourceTuple {
    @ColumnInfo(name = "source")
    public String source;

    @ColumnInfo(name = "section")
    public int section;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
    }

    @Override
    public String toString() {
        return  source + KuesuChanUtil.SOURCE_TUPLE_DELIM +section;
    }
}
