package kuesuchan.jpns.database.dao.helper;

import androidx.room.EmptyResultSetException;
import androidx.sqlite.db.SimpleSQLiteQuery;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import kuesuchan.jpns.database.dao.KanjiWritingDao;
import kuesuchan.jpns.database.entity.KanjiWriting;

public class KanjiWritingDaoHelper implements DaoHelper{

    public static enum Columns{
        kanji,
        japanese_reading,
        phonetic_reading,
        strokes,
        meaning,
        sources;
    }

    private KanjiWritingDao kanjiWritingDao;

    public KanjiWritingDaoHelper(KanjiWritingDao kanjiWritingDao) {
        this.kanjiWritingDao = kanjiWritingDao;
    }

    @Override
    public long insert(Object object) {
        return kanjiWritingDao.insert((KanjiWriting) object).subscribeOn(DEFAULT_SCHEDULER).blockingGet();
    }

    @Override
    public int delete(Object object) {
        return kanjiWritingDao.delete((KanjiWriting) object).subscribeOn(DEFAULT_SCHEDULER).blockingGet();
    }

    @Override
    public int update(Object object) {
        return kanjiWritingDao.update((KanjiWriting) object).subscribeOn(DEFAULT_SCHEDULER).blockingGet();
    }

    public KanjiWriting getKanjiWriting(String kanji) {
        try{
            return kanjiWritingDao.getKanjiWriting(kanji).subscribeOn(Schedulers.io()).blockingGet();
        } catch (EmptyResultSetException e){
            return null;
        }
    }

    public List<KanjiWriting> search(Columns columns, String input) {
        String sql = "SELECT * FROM KanjiWriting WHERE UPPER(" + columns.name() + ") LIKE '%" + input.toUpperCase() + "%'";
        try{
            return kanjiWritingDao.rawQuery(new SimpleSQLiteQuery(sql)).subscribeOn(DEFAULT_SCHEDULER).blockingGet();
        } catch (EmptyResultSetException e){
            return null;
        }
    }

    public List<KanjiWriting> getBySource(int amount, Set<String> sources) {
        StringBuilder sqlString = new StringBuilder("SELECT * FROM KanjiWriting WHERE ");
        sources.forEach(source->{
            sqlString.append("UPPER(" + Columns.sources.name() + ") LIKE '%" + source.toUpperCase() + "%' "  );
            sqlString.append(" or ");
        });
        sqlString.delete(sqlString.lastIndexOf("or"), sqlString.length()-1);
        sqlString.append(" limit " + amount);
        try{
            SimpleSQLiteQuery query = new SimpleSQLiteQuery(sqlString.toString());
            return kanjiWritingDao.rawQuery(query).subscribeOn(DEFAULT_SCHEDULER).blockingGet();
        } catch (EmptyResultSetException e){
            return null;
        }
    }

    public static List<String> getColumnList() {
        return Arrays.stream(Columns.values()).map(Enum::name).collect(Collectors.toList());
    }
}
