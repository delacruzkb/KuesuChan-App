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
import java.util.Set;

import io.reactivex.Completable;
import io.reactivex.Single;
import kuesuchan.jpns.database.entity.Vocabulary;

@Dao
public interface VocabularyDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    Single<Long> insert(Vocabulary vocabularies);

    @Delete
    Single<Integer> delete(Vocabulary vocabulary);

    @Update
    Single<Integer> update(Vocabulary vocabulary);

    @Query("SELECT * FROM VOCABULARY where UPPER(english)=UPPER(:english) and UPPER(kana)=UPPER(:kana)")
    Single<Vocabulary> getVocabulary(String english, String kana);

    @Query("SELECT * FROM VOCABULARY v, SOURCE s WHERE s.name LIKE :sourceName and section in (:sectionList) and s.source_ids  LIKE '%' + v.source_id +'%' limit :amount")
    Single<List<Vocabulary>> getBySection(String sourceName, Set<Integer> sectionList, int amount);

    @RawQuery
    Single<List<Vocabulary>> rawQuery(SupportSQLiteQuery query);

}