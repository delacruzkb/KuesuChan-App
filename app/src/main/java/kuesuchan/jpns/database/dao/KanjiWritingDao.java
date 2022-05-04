package kuesuchan.jpns.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.List;
import io.reactivex.Single;
import kuesuchan.jpns.database.entity.KanjiWriting;

@Dao
public interface KanjiWritingDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    Single<Long> insert(KanjiWriting kanjiWriting);

    @Delete
    Single<Integer> delete(KanjiWriting kanjiWriting);

    @Update
    Single<Integer> update(KanjiWriting kanjiWriting);

    @Query("SELECT * from KanjiWriting where UPPER(kanji)=UPPER(:kanji)")
    Single<KanjiWriting> getKanjiWriting(String kanji);

    @RawQuery
    Single<List<KanjiWriting>> rawQuery(SupportSQLiteQuery query);
}
