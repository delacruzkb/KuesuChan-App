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
import kuesuchan.jpns.database.dao.helper.KanjiWritingDaoHelper;
import kuesuchan.jpns.database.entity.KanjiWriting;
import kuesuchan.jpns.database.entity.Vocabulary;
import kuesuchan.jpns.tuple.KanjiWritingTuple;

@Dao
public interface KanjiWritingDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    Completable insert(KanjiWriting kanjiWriting);

    @Delete
    Completable delete(KanjiWriting kanjiWriting);

    @Update
    Completable update(KanjiWriting kanjiWriting);

    @Query("SELECT COUNT(*) from KanjiWriting")
    Single<Integer> getRowCount();

    @Query("SELECT * from KanjiWriting where kanji=:kanji")
    Single<KanjiWriting> getKanjiWriting(String kanji);

    @Query("SELECT kanji, japanese_reading, phonetic_reading, strokes, meaning FROM KanjiWriting WHERE source=:source limit :amount")
    Single<List<KanjiWritingTuple>> getFlashCards(int amount, String source);

    @Query("SELECT kanji, japanese_reading, phonetic_reading, strokes, meaning FROM KanjiWriting WHERE source=:source and section in (:section) limit :amount")
    Single<List<KanjiWritingTuple>> getFlashCardsBySections(int amount, String source, List<Integer> section);

    @Query("SELECT * from KanjiWriting where :column LIKE '%' + :input + '%'")
    Single<List<KanjiWriting>> search(String column, String input);
}
