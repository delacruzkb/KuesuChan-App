package kuesuchan.jpns.database.dao.helper;

import java.util.List;

import io.reactivex.schedulers.Schedulers;
import kuesuchan.jpns.database.dao.VocabularyDao;
import kuesuchan.jpns.database.entity.Vocabulary;
import kuesuchan.jpns.tuple.FlashCardTuple;

public class VocabularyDaoHelper {
    public static enum Columns{
        english,
        kana,
        kanji,
        help_text,
        type,
        source,
        section
    }

    private VocabularyDao dao;

    public VocabularyDaoHelper(VocabularyDao dao) {
        this.dao = dao;
    }

    public void insert(Vocabulary... vocabularies) {
        dao.insert(vocabularies).subscribeOn(Schedulers.io()).subscribe();
    }

    public void delete(Vocabulary... vocabulary) {
        dao.insert(vocabulary).subscribeOn(Schedulers.io()).subscribe();
    }

    public void update(Vocabulary vocabulary) {
        dao.update(vocabulary).subscribeOn(Schedulers.io()).subscribe();
    }

    public List<String> getSources() {
        return dao.getSources().subscribeOn(Schedulers.io()).blockingGet();
    }

    public Integer getMaxSection() {
        return dao.getMaxSection().subscribeOn(Schedulers.io()).blockingGet();
    }

    public Vocabulary getVocabulary(String english, String kana) {
        return dao.getVocabulary(english, kana).subscribeOn(Schedulers.io()).blockingGet();
    }

    public List<FlashCardTuple> getFlashCards(int amount, String source) {
        return dao.getFlashCards(amount, source).subscribeOn(Schedulers.io()).blockingGet();
    }

    public List<FlashCardTuple> getFlashCardsBySection(int amount, String source, List<Integer> sections) {
        return dao.getFlashCardsBySection(amount, source, sections).subscribeOn(Schedulers.io()).blockingGet();
    }

    public List<Vocabulary> search(Columns column, String input) {
        return dao.search(column.name(), input).subscribeOn(Schedulers.io()).blockingGet();
    }
}
