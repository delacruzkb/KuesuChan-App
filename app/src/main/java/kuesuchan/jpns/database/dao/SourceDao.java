package kuesuchan.jpns.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
import io.reactivex.Single;
import kuesuchan.jpns.database.entity.Source;
import kuesuchan.jpns.database.tuple.SourceTuple;

@Dao
public interface SourceDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    Single<Long> insert(Source source);

    @Delete
    Single<Integer> delete(Source source);

    @Update
    Single<Integer> update(Source source);

    @Query("SELECT * from SOURCE where UPPER(name) LIKE UPPER(:name) and section = :section")
    Single<Source> getSource(String name, int section);

    @Query("SELECT name,section from SOURCE where source_ids LIKE '%' + source_ids + '%'")
    Single<List<SourceTuple>> getSourcesBySourceId(String source_id);

    @Query("SELECT name from SOURCE group by name")
    Single<List<String>> getSourceNames();

    @Query("SELECT section from SOURCE where name LIKE :name")
    Single<List<Integer>> getSourceSections( String name);

}
