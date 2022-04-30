package kuesuchan.jpns.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import kuesuchan.jpns.database.entity.Source;

@Dao
public interface SourceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(Source... source);

    @Delete
    Completable delete(Source... source);

    @Update
    Completable update(Source source);

    @Query("SELECT * from SOURCE where source LIKE :source")
    Single<Source> getSource(String source);

    @Query("SELECT * from SOURCE where UPPER(:column) LIKE '%' + UPPER(:input) + '%'")
    Single<List<Source>> search(String column, String input);

    @Query("SELECT * from SOURCE")
    Single<List<Source>> getSources();

}
