package kuesuchan.jpns.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;
import java.util.Set;

@Entity(primaryKeys = {"name","section"})
public class Source {

    @NonNull
    private String name;

    private int section;

    private Set<String> source_ids;

}
