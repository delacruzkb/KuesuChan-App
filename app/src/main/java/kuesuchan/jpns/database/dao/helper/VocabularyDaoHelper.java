package kuesuchan.jpns.database.dao.helper;

import androidx.room.EmptyResultSetException;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import kuesuchan.jpns.database.dao.SourceDao;
import kuesuchan.jpns.database.dao.VocabularyDao;
import kuesuchan.jpns.database.dao.tuple.FlashCardTuple;
import kuesuchan.jpns.database.entity.Vocabulary;

public class VocabularyDaoHelper {
    public static enum Columns{
        english,
        kana,
        kanji,
        help_text,
        source
    }

    private final Scheduler DEFAULT_SCHEDULER = Schedulers.io();

    private VocabularyDao vocabularyDao;

    public VocabularyDaoHelper(VocabularyDao vocabularyDao) {
        this.vocabularyDao = vocabularyDao;
    }

    public void insert(Vocabulary... vocabulary){
        vocabularyDao.insert(vocabulary).subscribeOn(DEFAULT_SCHEDULER).subscribe();
    }

    public void delete(Vocabulary... vocabulary){
        vocabularyDao.delete(vocabulary).subscribeOn(DEFAULT_SCHEDULER).subscribe();
    }

    public void update(Vocabulary vocabulary){
        vocabularyDao.update(vocabulary).subscribeOn(DEFAULT_SCHEDULER).subscribe();
    }

    public Vocabulary getVocabulary(String english, String kana){
        try{
            return vocabularyDao.getVocabulary(english,kana).subscribeOn(DEFAULT_SCHEDULER).blockingGet();
        } catch (EmptyResultSetException e){
            return null;
        }
    }

    public List<Vocabulary> search(Columns column, String input){

        try{
            return vocabularyDao.search(column.name(), input).subscribeOn(DEFAULT_SCHEDULER).blockingGet();
        } catch (EmptyResultSetException e){
            return null;
        }
    }

    public List<FlashCardTuple> getFlashCards(int amount, Set<String> sources){
        StringBuilder sourceCondition = new StringBuilder();
        sources.forEach(source->{
            sourceCondition.append(Columns.source.name().toUpperCase() + "LIKE '%' + " + source.toUpperCase() + " + '%'" );
            sourceCondition.append(" or ");
        });
        sourceCondition.delete(sourceCondition.lastIndexOf("or"), sourceCondition.length()-1);
        try{
            return vocabularyDao.getFlashCards(amount, sourceCondition.toString()).subscribeOn(DEFAULT_SCHEDULER).blockingGet();
        } catch (EmptyResultSetException e){
            return null;
        }


    }

    public static List<String> getColumnList() {
        return Arrays.stream(Columns.values()).map(Enum::name).collect(Collectors.toList());
    }

}
