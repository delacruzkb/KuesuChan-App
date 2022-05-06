package kuesuchan.jpns.database.dao.helper;

import androidx.room.EmptyResultSetException;
import androidx.sqlite.db.SimpleSQLiteQuery;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import kuesuchan.jpns.database.Converters;
import kuesuchan.jpns.database.dao.KanjiWritingDao;
import kuesuchan.jpns.database.dao.SourceDao;
import kuesuchan.jpns.database.entity.KanjiWriting;
import kuesuchan.jpns.database.entity.Source;
import kuesuchan.jpns.database.entity.Vocabulary;
import kuesuchan.jpns.database.tuple.SourceTuple;

public class KanjiWritingDaoHelper{

    private final Scheduler DEFAULT_SCHEDULER = Schedulers.io();
    private KanjiWritingDao kanjiWritingDao;
    private SourceDaoHelper sourceDaoHelper;

    public KanjiWritingDaoHelper(KanjiWritingDao kanjiWritingDao, SourceDao sourceDao) {
        this.kanjiWritingDao = kanjiWritingDao;
        this.sourceDaoHelper = new SourceDaoHelper(sourceDao);
    }

    public long insert(KanjiWriting kanjiWriting, String sourceName, int sourceSection) {
        Source source = sourceDaoHelper.getSource(sourceName, sourceSection);
        if(source != null){
            source.addSource_id(kanjiWriting.getKanji());
            sourceDaoHelper.update(source);
        } else {
            sourceDaoHelper.insert(new Source(sourceName, sourceSection, Source.TYPE.kanji_writing,kanjiWriting.getKanji()));
        }
        return kanjiWritingDao.insert(kanjiWriting).subscribeOn(DEFAULT_SCHEDULER).blockingGet();
    }

    public long insert(KanjiWriting kanjiWriting, Set<SourceTuple> newTuples) {
        sourceDaoHelper.insertSourceIdFromTuples(kanjiWriting.getKanji(),Source.TYPE.kanji_writing,newTuples);

        return kanjiWritingDao.insert(kanjiWriting).subscribeOn(DEFAULT_SCHEDULER).blockingGet();
    }


    public int delete(KanjiWriting kanjiWriting) {
        sourceDaoHelper.deleteSourceId(kanjiWriting.getKanji());
        return kanjiWritingDao.delete(kanjiWriting).subscribeOn(DEFAULT_SCHEDULER).blockingGet();
    }

    public int update(KanjiWriting kanjiWriting) {
        return kanjiWritingDao.update(kanjiWriting).subscribeOn(DEFAULT_SCHEDULER).blockingGet();
    }

    public int update(KanjiWriting kanjiWriting, Set<SourceTuple> newTuples) {
        Set<SourceTuple> oldTuples = new HashSet<>(sourceDaoHelper.getSourceTuplesBySourceId(kanjiWriting.getKanji()));

        Set<SourceTuple> deleteTupleSet = new HashSet<>(oldTuples);
        deleteTupleSet.removeAll(newTuples);
        sourceDaoHelper.deleteSourceIdFromTuples(kanjiWriting.getKanji(), deleteTupleSet);

        Set<SourceTuple> insertTupleSet = new HashSet<>(newTuples);
        insertTupleSet.removeAll(oldTuples);
        sourceDaoHelper.insertSourceIdFromTuples(kanjiWriting.getKanji(),Source.TYPE.kanji_writing, insertTupleSet);

        return update(kanjiWriting);
    }

    public KanjiWriting getKanjiWriting(String kanji) {
        try{
            return kanjiWritingDao.getKanjiWriting(kanji).subscribeOn(Schedulers.io()).blockingGet();
        } catch (EmptyResultSetException e){
            return null;
        }
    }

    public List<KanjiWriting> search(KanjiWriting.Columns columns, String input) {
        String sql = "SELECT * FROM KanjiWriting WHERE UPPER(" + columns.name() + ") LIKE '%" + input.toUpperCase() + "%'";
        try{
            return kanjiWritingDao.rawQuery(new SimpleSQLiteQuery(sql)).subscribeOn(DEFAULT_SCHEDULER).blockingGet();
        } catch (EmptyResultSetException e){
            return null;
        }
    }

    public List<KanjiWriting> getBySource(String sourceName, Set<Integer> sections, int amount) {
        StringBuilder sqlString =
                new StringBuilder("SELECT * FROM KANJIWRITING k, SOURCE s"
                        + " WHERE s.name LIKE " + sourceName
                        + " and s.type LIKE " + Source.TYPE.kanji_writing.name()
                        + " and section in (" + Converters.integerSetToString(sections) +")"
                        + " and s.source_ids  LIKE '%' + k.source_id + '%'"
                        + " limit " + amount);
        try{
            SimpleSQLiteQuery query = new SimpleSQLiteQuery(sqlString.toString());
            return kanjiWritingDao.rawQuery(query).subscribeOn(DEFAULT_SCHEDULER).blockingGet();
        } catch (EmptyResultSetException e){
            return null;
        }
    }

    public static List<String> getColumnList(){
        return Arrays.stream(KanjiWriting.Columns.values()).map(columns -> columns.name()).collect(Collectors.toList());
    }
}
