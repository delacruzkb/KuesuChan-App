package kuesuchan.jpns.database.tuple;

import androidx.annotation.Nullable;
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

    public SourceTuple(String source, int section) {
        this.source = source;
        this.section = section;
    }

    @Override
    public String toString() {
        return  source + KuesuChanUtil.SOURCE_TUPLE_DELIM +section;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SourceTuple that = (SourceTuple) o;
        return section == that.section && source.equals(that.source);
    }
}
