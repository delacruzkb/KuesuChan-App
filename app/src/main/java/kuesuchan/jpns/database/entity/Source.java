package kuesuchan.jpns.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity
public class Source {

    @PrimaryKey
    @NonNull
    private String source;

    private int sectionCount;

    @Override
    public String toString() {
        return "Source: '" + source + "'" +
                "\nSectionCount: " + sectionCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Source source1 = (Source) o;
        return sectionCount == source1.sectionCount && source.equals(source1.source);
    }

    @NonNull
    public String getSource() {
        return source;
    }

    public void setSource(@NonNull String source) {
        this.source = source;
    }

    public int getSectionCount() {
        return sectionCount;
    }

    public void setSectionCount(int sectionCount) {
        this.sectionCount = sectionCount;
    }
}
