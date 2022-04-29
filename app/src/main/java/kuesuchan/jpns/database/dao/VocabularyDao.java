package kuesuchan.jpns.database.dao;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import kuesuchan.jpns.database.entity.Vocabulary;
import kuesuchan.jpns.tuple.FlashCardTuple;

@Dao
public interface VocabularyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(Vocabulary... vocabularies);

    @Delete
    Completable delete(Vocabulary... vocabulary);

    @Update
    Completable update(Vocabulary vocabulary);

    @Query("SELECT source from Vocabulary group by source")
    Single<List<String>> getSources();

    @Query("SELECT MAX(section) from Vocabulary")
    Single<Integer> getMaxSection();

    @Query("SELECT * FROM VOCABULARY where english=:english and kana=:kana")
    Single<Vocabulary> getVocabulary(String english, String kana);

    @Query("SELECT english, kana, kanji, help_text FROM Vocabulary WHERE source=:source limit :amount")
    Single<List<FlashCardTuple>> getFlashCards(int amount, String source);

    @Query("SELECT english, kana, kanji, help_text FROM Vocabulary WHERE source=:source and section in (:sections) limit :amount")
    Single<List<FlashCardTuple>> getFlashCardsBySection(int amount, String source, List<Integer> sections);

    @Query("SELECT * from Vocabulary where :column LIKE '%' + :input + '%'")
    Single<List<Vocabulary>> search(String column, String input);
}
