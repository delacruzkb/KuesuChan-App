package kuesuchan.jpns.database.dao.helper;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import io.reactivex.schedulers.Schedulers;
import kuesuchan.jpns.database.dao.KanjiWritingDao;
import kuesuchan.jpns.database.entity.KanjiWriting;
import kuesuchan.jpns.database.dao.tuple.KanjiWritingTuple;

public class KanjiWritingDaoHelper{

    public enum Columns{
        kanji,japanese_reading,phonetic_reading,strokes,meaning,source;
    }

    private KanjiWritingDao dao;

    public KanjiWritingDaoHelper(KanjiWritingDao kanjiWritingDao) {
        this.dao = kanjiWritingDao;
    }

    public void insert(KanjiWriting kanjiWriting) {
        dao.insert(kanjiWriting).subscribeOn(Schedulers.io()).doOnError(error -> {
            System.out.println(error.getMessage());
        }).subscribe();
    }

    public void delete(KanjiWriting kanjiWriting) {
        dao.delete(kanjiWriting).subscribeOn(Schedulers.io()).subscribe();
    }

    public void update(KanjiWriting kanjiWriting) {
        dao.update(kanjiWriting).subscribeOn(Schedulers.io()).subscribe();
    }

    public Integer getRowCount() {
        return dao.getRowCount().subscribeOn(Schedulers.io()).blockingGet();
    }

    public KanjiWriting getKanjiWriting(String kanji) {
        return dao.getKanjiWriting(kanji).subscribeOn(Schedulers.io()).blockingGet();
    }

    public List<KanjiWritingTuple> getKanjiWritingTuples(int amount, Set<String> sources) {
        StringBuilder sourceCondition = new StringBuilder();
        sources.forEach(source->{
            sourceCondition.append(VocabularyDaoHelper.Columns.source.name().toUpperCase() + "LIKE '%' + " + source.toUpperCase() + " + '%'" );
            sourceCondition.append(" or ");
        });
        sourceCondition.delete(sourceCondition.lastIndexOf("or"), sourceCondition.length()-1);
        return dao.getKanjiWritingTuples(amount, sourceCondition.toString()).subscribeOn(Schedulers.io()).blockingGet();
    }

    public List<KanjiWriting> search(Columns columns, String input) {
        return dao.search(columns.name(), input).subscribeOn(Schedulers.io()).blockingGet();
    }

    public static List<String> getColumnList() {
        return Arrays.stream(VocabularyDaoHelper.Columns.values()).map(Enum::name).collect(Collectors.toList());
    }
}
