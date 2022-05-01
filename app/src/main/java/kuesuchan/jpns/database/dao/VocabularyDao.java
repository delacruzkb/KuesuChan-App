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
import kuesuchan.jpns.database.entity.Vocabulary;
import kuesuchan.jpns.database.dao.tuple.FlashCardTuple;

@Dao
public interface VocabularyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(Vocabulary... vocabularies);

    @Delete
    Completable delete(Vocabulary... vocabulary);

    @Update
    Completable update(Vocabulary vocabulary);

    @Query("SELECT * FROM VOCABULARY where UPPER(english)=UPPER(:english) and UPPER(kana)=UPPER(:kana)")
    Single<Vocabulary> getVocabulary(String english, String kana);

    @Query("SELECT * from Vocabulary where UPPER(:column) LIKE '%' + UPPER(:input) + '%'")
    Single<List<Vocabulary>> search(String column, String input);

    @Query("SELECT english, kana, kanji, help_text FROM Vocabulary WHERE :sourceCondition limit :amount")
    Single<List<FlashCardTuple>> getFlashCards(int amount, String sourceCondition);
}
