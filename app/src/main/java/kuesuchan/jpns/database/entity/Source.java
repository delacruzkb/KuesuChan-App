package kuesuchan.jpns.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity(primaryKeys = {"name","section", "type"})
public class Source {

    public static enum Columns{
        name,
        section,
        type
    }

    public static enum TYPE{
        vocabulary,
        kanji_writing
    }

    @NonNull
    private String name;

    private int section;

    @NonNull
    private TYPE type;

    private Set<String> source_ids;

    public Source(@NonNull String name, int section, TYPE type) {
        this.name = name;
        this.section = section;
        this.type = type;
        this.source_ids = new HashSet<>();
    }

    public Source(@NonNull String name, int section, TYPE type , String source_id) {
        this.name = name;
        this.section = section;
        this.source_ids = new HashSet<>();
        this.type = type;
        source_ids.add(source_id);
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
    }

    public Set<String> getSource_ids() {
        return source_ids;
    }

    public void setSource_ids(Set<String> source_ids) {
        this.source_ids = source_ids;
    }

    public Source addSource_id(String source_id){

        this.source_ids.add(source_id);
        return  this;
    }

    public Source removeSource_id(String source_id){
        this.source_ids.remove(source_id);
        return  this;
    }

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }
}
