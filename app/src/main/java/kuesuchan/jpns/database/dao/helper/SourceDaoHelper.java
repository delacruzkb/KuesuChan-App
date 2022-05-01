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

public class SourceDaoHelper {

    public static enum Columns{
        source,
        sectionCount
    }


    private final Scheduler DEFAULT_SCHEDULER = Schedulers.io();
    private SourceDao sourceDao;

    public SourceDaoHelper(SourceDao sourceDao) {
        this.sourceDao = sourceDao;
    }

    public void insert(Source... source){
        sourceDao.insert(source).subscribeOn(DEFAULT_SCHEDULER).subscribe();
    }

    public void delete(Source... source){
        sourceDao.delete(source).subscribeOn(DEFAULT_SCHEDULER).subscribe();
    }

    public void update(Source source){
        sourceDao.update(source).subscribeOn(DEFAULT_SCHEDULER).subscribe();
    }

    public Source getSource(String source){
        try{
            return sourceDao.getSource(source).subscribeOn(DEFAULT_SCHEDULER).blockingGet();
        } catch (EmptyResultSetException e) {
            return null;
        }
    }

    public List<Source> search(Columns column, String input){
        try{
            return sourceDao.search(column.name(), input).subscribeOn(DEFAULT_SCHEDULER).blockingGet();
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
