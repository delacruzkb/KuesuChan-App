package kuesuchan.jpns.database.dao.helper;

import java.util.Collections;
import java.util.List;

import io.reactivex.schedulers.Schedulers;
import kuesuchan.jpns.database.dao.KanjiWritingDao;
import kuesuchan.jpns.database.entity.KanjiWriting;
import kuesuchan.jpns.tuple.KanjiWritingTuple;

public class KanjiWritingDaoHelper{

    public enum Columns{
        kanji,japanese_reading,phonetic_reading,strokes,meaning,source,section;
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

    public List<KanjiWritingTuple> getFlashCards(int amount, String source) {
        return dao.getFlashCards(amount, source).subscribeOn(Schedulers.io()).blockingGet();
    }

    public List<KanjiWritingTuple> getFlashCardsBySections(int amount, String source, List<Integer> section) {
        return dao.getFlashCardsBySections(amount, source, section).subscribeOn(Schedulers.io()).blockingGet();
    }

    public List<KanjiWriting> search(Columns columns, String input) {
        return dao.search(columns.name(), input).subscribeOn(Schedulers.io()).blockingGet();
    }
}
