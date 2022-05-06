package kuesuchan.jpns.database.dao.helper;

import androidx.room.EmptyResultSetException;
import androidx.room.Entity;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SimpleSQLiteQuery;

import java.util.Arrays;
import java.util.HashSet;
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
import kuesuchan.jpns.database.tuple.SourceTuple;

public class VocabularyDaoHelper{

    private final Scheduler DEFAULT_SCHEDULER = Schedulers.io();
    private VocabularyDao vocabularyDao;
    private SourceDaoHelper sourceDaoHelper;

    public VocabularyDaoHelper(VocabularyDao vocabularyDao, SourceDao sourceDao) {
        this.vocabularyDao = vocabularyDao;
        this.sourceDaoHelper = new SourceDaoHelper(sourceDao);
    }

    public long insert(Vocabulary vocabulary, String sourceName, int sourceSection) {
        Source source = sourceDaoHelper.getSource(sourceName, sourceSection);
        if(source != null){
            source.addSource_id(vocabulary.getSource_id());
            sourceDaoHelper.update(source);
        } else {
            sourceDaoHelper.insert(new Source(sourceName, sourceSection, Source.TYPE.vocabulary,vocabulary.getSource_id()));
        }
        return vocabularyDao.insert(vocabulary).subscribeOn(DEFAULT_SCHEDULER).blockingGet();
    }

    public long insert(Vocabulary vocabulary, Set<SourceTuple> newTuples) {
        sourceDaoHelper.insertSourceIdFromTuples(vocabulary.getSource_id(),Source.TYPE.vocabulary,newTuples);

        return vocabularyDao.insert(vocabulary).subscribeOn(DEFAULT_SCHEDULER).blockingGet();
    }

    public int delete(Vocabulary vocabulary) {
        sourceDaoHelper.deleteSourceId(vocabulary.getSource_id());
        return vocabularyDao.delete(vocabulary).subscribeOn(DEFAULT_SCHEDULER).blockingGet();
    }

    public int update(Vocabulary newVocabulary) {
        return vocabularyDao.update(newVocabulary).subscribeOn(DEFAULT_SCHEDULER).blockingGet();
    }

    public int update(Vocabulary newVocabulary, Set<SourceTuple> newTuples) {
        Set<SourceTuple> oldTuples = new HashSet<>(sourceDaoHelper.getSourceTuplesBySourceId(newVocabulary.getSource_id()));

        Set<SourceTuple> deleteTupleSet = new HashSet<>(oldTuples);
        deleteTupleSet.removeAll(newTuples);
        sourceDaoHelper.deleteSourceIdFromTuples(newVocabulary.getSource_id(), deleteTupleSet);

        Set<SourceTuple> insertTupleSet = new HashSet<>(newTuples);
        insertTupleSet.removeAll(oldTuples);
        sourceDaoHelper.insertSourceIdFromTuples(newVocabulary.getSource_id(),Source.TYPE.vocabulary, insertTupleSet);

        return update(newVocabulary);
    }

    public Vocabulary getVocabulary(String english, String kana){
        try{
            return vocabularyDao.getVocabulary(english,kana).subscribeOn(DEFAULT_SCHEDULER).blockingGet();
        } catch (EmptyResultSetException e){
            return null;
        }
    }

    public List<Vocabulary> search(Vocabulary.Columns column, String input){
        String sql = "SELECT * FROM Vocabulary WHERE UPPER(" + column.name() + ") LIKE '%" + input.toUpperCase() + "%'";
        try{
            return vocabularyDao.rawQuery(new SimpleSQLiteQuery(sql)).subscribeOn(DEFAULT_SCHEDULER).blockingGet();
        } catch (EmptyResultSetException e){
            return null;
        }
    }

    public List<Vocabulary> getBySource(String sourceName, Set<Integer> sections, int amount){
        StringBuilder sqlString =
                new StringBuilder("SELECT * FROM VOCABULARY v, SOURCE s"
                                    + " WHERE s.name LIKE " + sourceName
                                    + " and s.type LIKE " + Source.TYPE.vocabulary.name()
                                    + " and section in (" + Converters.integerSetToString(sections) +")"
                                    + " and s.source_ids  LIKE '%' + v.source_id + '%'"
                                    + " limit " + amount);
        try{
            SimpleSQLiteQuery query = new SimpleSQLiteQuery(sqlString.toString());
            return vocabularyDao.rawQuery(query).subscribeOn(DEFAULT_SCHEDULER).blockingGet();
        } catch (EmptyResultSetException e){
            return null;
        }
    }

    public static List<String> getColumnList(){
        return Arrays.stream(Vocabulary.Columns.values()).map(columns -> columns.name()).collect(Collectors.toList());
    }

}
