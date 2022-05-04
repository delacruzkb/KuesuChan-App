package kuesuchan.jpns.database.dao.helper;

import androidx.room.Delete;
import androidx.room.EmptyResultSetException;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.Completable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import kuesuchan.jpns.database.dao.SourceDao;
import kuesuchan.jpns.database.entity.Source;

public class SourceDaoHelper implements DaoHelper{

    public static enum Columns{
        source,
        sectionCount
    }

    private SourceDao sourceDao;

    public SourceDaoHelper(SourceDao sourceDao) {
        this.sourceDao = sourceDao;
    }

    @Override
    public long insert(Object object) {
        return sourceDao.insert((Source) object).subscribeOn(DEFAULT_SCHEDULER).blockingGet();
    }

    @Override
    public int delete(Object object) {
        return sourceDao.delete((Source) object).subscribeOn(DEFAULT_SCHEDULER).blockingGet();
    }

    @Override
    public int update(Object object) {
        return sourceDao.update((Source) object).subscribeOn(DEFAULT_SCHEDULER).blockingGet();
    }

    public Source getSource(String name, int section){
        try{
            return sourceDao.getSource(name,section).subscribeOn(DEFAULT_SCHEDULER).blockingGet();
        } catch (EmptyResultSetException e) {
            return null;
        }
    }

    public List<String> getSourceList(){
        try{
            return sourceDao.getSources().subscribeOn(DEFAULT_SCHEDULER).blockingGet().stream().map(Source::getSource).collect(Collectors.toList());
        } catch (EmptyResultSetException e) {
            return null;
        }
    }

    public static String toSourceSting(String source, int section){
        return source + "." + section;
    }
    public static List<String> getColumnList() {
        return Arrays.stream(Columns.values()).map(Enum::name).collect(Collectors.toList());
    }

}
