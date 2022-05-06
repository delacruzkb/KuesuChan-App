package kuesuchan.jpns.database.dao.helper;

import androidx.room.Delete;
import androidx.room.EmptyResultSetException;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import io.reactivex.Completable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import kuesuchan.jpns.database.dao.SourceDao;
import kuesuchan.jpns.database.entity.Source;
import kuesuchan.jpns.database.tuple.SourceTuple;

public class SourceDaoHelper {

    private final Scheduler DEFAULT_SCHEDULER = Schedulers.io();
    private SourceDao sourceDao;

    public SourceDaoHelper(SourceDao sourceDao) {
        this.sourceDao = sourceDao;
    }

    public long insert(Source source) {
        return sourceDao.insert(source).subscribeOn(DEFAULT_SCHEDULER).blockingGet();
    }

    public int insertSourceIdFromTuples(String source_id, Source.TYPE type,Set<SourceTuple> insertTupleSet) {
        int totalUpdates=0;
        insertTupleSet.stream().forEach(sourceTuple -> {
            Source source = getSource(sourceTuple);
            if( source !=null){
                update(source.addSource_id(source_id));
            } else {
                insert(new Source(sourceTuple.getSource(), sourceTuple.getSection(), type).addSource_id(source_id));
            }

        });
        return totalUpdates;
    }

    public int delete(Source source) {
        return sourceDao.delete(source).subscribeOn(DEFAULT_SCHEDULER).blockingGet();
    }

    public int deleteSourceIdFromTuples(String source_id, Set<SourceTuple> deleteTupleSet) {
        int totalUpdates=0;
        for ( SourceTuple st: deleteTupleSet) {

            totalUpdates += delete(getSource(st).removeSource_id(source_id));
        }
        return totalUpdates;
    }

    public int deleteSourceId(String source_id) {
        return deleteSourceIdFromTuples(source_id, new HashSet<>(getSourceTuplesBySourceId(source_id)));
    }

    public int update(Source source) {
        return sourceDao.update(source).subscribeOn(DEFAULT_SCHEDULER).blockingGet();
    }

    public Source getSource(String name, int section){
        try{
            return sourceDao.getSource(name,section).subscribeOn(DEFAULT_SCHEDULER).blockingGet();
        } catch (EmptyResultSetException e) {
            return null;
        }
    }

    public Source getSource(SourceTuple sourceTuple){
        return getSource(sourceTuple.getSource(), sourceTuple.getSection());
    }

    public List<SourceTuple> getSourceTuplesBySourceId(String source_id){
        try{
            return sourceDao.getSourceTuplesBySourceId(source_id).subscribeOn(DEFAULT_SCHEDULER).blockingGet();
        } catch (EmptyResultSetException e) {
            return null;
        }
    }

    public List<Source> getSourcesBySourceId(String source_id){
        try{
            return sourceDao.getSourcesBySourceId(source_id).subscribeOn(DEFAULT_SCHEDULER).blockingGet();
        } catch (EmptyResultSetException e) {
            return null;
        }
    }

    public List<String> getSourceNames(){
        try{
            return sourceDao.getSourceNames().subscribeOn(DEFAULT_SCHEDULER).blockingGet();
        } catch (EmptyResultSetException e) {
            return null;
        }
    }

    public List<Integer> getSourceSections( String name){
        try{
            return sourceDao.getSourceSections(name).subscribeOn(DEFAULT_SCHEDULER).blockingGet();
        } catch (EmptyResultSetException e) {
            return null;
        }
    }

    public static List<String> getColumnList(){
        return Arrays.stream(Source.Columns.values()).map(columns -> columns.name()).collect(Collectors.toList());
    }


}
