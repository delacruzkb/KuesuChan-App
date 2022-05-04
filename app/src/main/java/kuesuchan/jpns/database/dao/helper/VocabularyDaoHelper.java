package kuesuchan.jpns.database.dao.helper;

import androidx.room.EmptyResultSetException;
import androidx.room.Entity;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SimpleSQLiteQuery;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import kuesuchan.jpns.database.Converters;
import kuesuchan.jpns.database.dao.SourceDao;
import kuesuchan.jpns.database.dao.VocabularyDao;
import kuesuchan.jpns.database.entity.Source;
import kuesuchan.jpns.database.entity.Vocabulary;

public class VocabularyDaoHelper  implements DaoHelper{

    public static enum Columns{
        english,
        kana,
        kanji,
        help_text,
        sources
    }

    private VocabularyDao vocabularyDao;
    private SourceDao sourceDao;

    public VocabularyDaoHelper(VocabularyDao vocabularyDao, SourceDao sourceDao) {
        this.vocabularyDao = vocabularyDao;
        this.sourceDao = sourceDao;
    }


    @Override
    public long insert(Object object) {
        //TODO: update Source also
        return vocabularyDao.insert((Vocabulary) object).subscribeOn(DEFAULT_SCHEDULER).blockingGet();
    }

    @Override
    public int delete(Object object) {
        return vocabularyDao.delete((Vocabulary) object).subscribeOn(DEFAULT_SCHEDULER).blockingGet();
    }

    @Override
    public int update(Object object) {
        //TODO: update Source also
        return vocabularyDao.update((Vocabulary) object).subscribeOn(DEFAULT_SCHEDULER).blockingGet();
    }

    public Vocabulary getVocabulary(String english, String kana){
        try{
            return vocabularyDao.getVocabulary(english,kana).subscribeOn(DEFAULT_SCHEDULER).blockingGet();
        } catch (EmptyResultSetException e){
            return null;
        }
    }

    public List<Vocabulary> search(Columns column, String input){
        String sql = "SELECT * FROM Vocabulary WHERE UPPER(" + column.name() + ") LIKE '%" + input.toUpperCase() + "%'";
        try{
            return vocabularyDao.rawQuery(new SimpleSQLiteQuery(sql)).subscribeOn(DEFAULT_SCHEDULER).blockingGet();
        } catch (EmptyResultSetException e){
            return null;
        }
    }

    public List<Vocabulary> getBySource(String sourceName, Set<Integer> sections, int amount){
//        StringBuilder sqlString =
//                new StringBuilder("SELECT * FROM VOCABULARY v, SOURCE s"
//                                    + "WHERE s.name LIKE " + sourceName
//                                    + " and section in (" + Converters.integerSetToString(sections) +")"
//                                    + " and s.source_ids  LIKE %v.source_id%"
//                                    + " limit " + amount);
//        try{
//            SimpleSQLiteQuery query = new SimpleSQLiteQuery(sqlString.toString());
//            return vocabularyDao.rawQuery(query).subscribeOn(DEFAULT_SCHEDULER).blockingGet();
//        } catch (EmptyResultSetException e){
//            return null;
//        }
        try{
            return vocabularyDao.getBySection(sourceName, sections, amount).subscribeOn(DEFAULT_SCHEDULER).blockingGet();
        } catch (EmptyResultSetException e){
            return null;
        }
    }

    public static List<String> getColumnList() {
        return Arrays.stream(Columns.values()).map(Enum::name).collect(Collectors.toList());
    }

}
