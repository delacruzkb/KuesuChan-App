package kuesuchan.jpns.database;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import kuesuchan.jpns.database.dao.helper.KanjiWritingDaoHelper;
import kuesuchan.jpns.database.dao.helper.VocabularyDaoHelper;
import kuesuchan.jpns.database.entity.KanjiWriting;
import kuesuchan.jpns.database.entity.Vocabulary;

@RunWith(AndroidJUnit4.class)
public class AppDatabaseTest extends TestCase {

    private Context context;
    private AppDatabase db;
    @Before
    public void setup(){
        context = ApplicationProvider.getApplicationContext();
        db = AppDatabase.getInstance(context);
        db.clearAllTables();
    }

    @After
    public void finish(){
        db.close();
    }

    @Test
    public void testGetVocabularyDaoHelper() {
        VocabularyDaoHelper daoHelper = db.getVocabularyDaoHelper();
        Vocabulary testVocabulary= new Vocabulary("English", "Kana", "Kanji", "Help Text");

        //insert
        daoHelper.insert(testVocabulary);
        try {
            daoHelper.insert(testVocabulary);
        } catch (SQLiteConstraintException e){
            // to nothing, abort is working
        } catch (Exception e){
            fail(e.toString());
        }
        //get
        assertEquals(testVocabulary, daoHelper.getVocabulary(testVocabulary.getEnglish(), testVocabulary.getKana()));
        // update
        String newHelp = "No Help Needed";
        testVocabulary.setHelp_text(newHelp);
        assertEquals(newHelp, testVocabulary.getHelp_text());
        daoHelper.update(testVocabulary);
        assertEquals(testVocabulary, daoHelper.getVocabulary(testVocabulary.getEnglish(), testVocabulary.getKana()));
        //delete
        assertEquals(1,daoHelper.delete(testVocabulary));
        assertNull(daoHelper.getVocabulary(testVocabulary.getEnglish(), testVocabulary.getKana()));

        //getBySource

        Vocabulary sourceItem1= new Vocabulary("daughter1", "むすめさん", "", "In relation to someone else");
        Vocabulary sourceItem2= new Vocabulary("daughter2", "むすめ", "", "In relation to you");
        Vocabulary sourceItem3= new Vocabulary("daughter3", "むすめさん", "", "In relation to someone else");
        Vocabulary sourceItem4= new Vocabulary("daughter4", "むすめ", "", "In relation to you");
        Vocabulary sourceItem5= new Vocabulary("daughter5", "むすめ", "", "In relation to you");
        daoHelper.insert(sourceItem1);
        daoHelper.insert(sourceItem2);
        daoHelper.insert(sourceItem3);
        daoHelper.insert(sourceItem4);
        daoHelper.insert(sourceItem5);

        Set<String> sourceList = new HashSet<>();
        sourceList.add("Udemy.01");

        //query just 1, test similar strings
        List<Vocabulary> vocabularyList = daoHelper.getBySource(10,sourceList);
        assertEquals(1, vocabularyList.size());
        assertTrue(vocabularyList.contains(sourceItem1));

        //test limit
        sourceList.add("Udemy.11");
        vocabularyList = daoHelper.getBySource(1,sourceList);
        assertEquals(1, vocabularyList.size());
        assertTrue(vocabularyList.contains(sourceItem1));

        // Search
        daoHelper.delete(sourceItem1);
        daoHelper.delete(sourceItem2);
        daoHelper.delete(sourceItem3);
        daoHelper.delete(sourceItem4);
        daoHelper.delete(sourceItem5);

        Vocabulary searchItem1= new Vocabulary("daughter", "むすめさん", "A", "In relation to someone else", "Udemy.01");
        Vocabulary searchItem2= new Vocabulary("Not Not DAUGHTER", "むすめ", "", "In relation to you", "Udemy.02");
        Vocabulary searchItem3= new Vocabulary("Not Son", "むすめ", "BAB", "", "Udemy.03");

        daoHelper.insert(searchItem1);
        daoHelper.insert(searchItem2);
        daoHelper.insert(searchItem3);


        List<Vocabulary> searchResult = daoHelper.search(VocabularyDaoHelper.Columns.english, "augh");
        assertEquals(2, searchResult.size());
        assertTrue(searchResult.contains(searchItem1) && searchResult.contains(searchItem2));

        searchResult = daoHelper.search(VocabularyDaoHelper.Columns.kana, "す");
        assertEquals(3, searchResult.size());
        assertTrue(searchResult.contains(searchItem1) && searchResult.contains(searchItem2) && searchResult.contains(searchItem3));

        searchResult = daoHelper.search(VocabularyDaoHelper.Columns.kanji, "B");
        assertEquals(1, searchResult.size());
        assertTrue(searchResult.contains(searchItem3));

        searchResult = daoHelper.search(VocabularyDaoHelper.Columns.help_text, "");
        assertEquals(3, searchResult.size());
        assertTrue(searchResult.contains(searchItem1) && searchResult.contains(searchItem2) && searchResult.contains(searchItem3));

        searchResult = daoHelper.search(VocabularyDaoHelper.Columns.sources, "d");
        assertEquals(3, searchResult.size());
        assertTrue(searchResult.contains(searchItem1) && searchResult.contains(searchItem2) && searchResult.contains(searchItem3));
    }

    @Test
    public void testGetKanjiWritingDaoHelper() {
        //TODO: do tests for methods
        KanjiWritingDaoHelper daoHelper = db.getKanjiWritingDaoHelper();
        KanjiWriting testKanjiWriting = new KanjiWriting("Kanji", "JapaneseR", "PhoneticR", 1, "Meaning", "Udemy.1");
        //insert
        daoHelper.insert(testKanjiWriting);
        try {
            daoHelper.insert(testKanjiWriting);
        } catch (SQLiteConstraintException e){
            // to nothing, abort is working
        } catch (Exception e){
            fail(e.toString());
        }
        //get
        assertEquals(testKanjiWriting, daoHelper.getKanjiWriting(testKanjiWriting.getKanji()));
        // update
        String newMeaning = "Poof";
        testKanjiWriting.setMeaning(newMeaning);
        assertEquals(newMeaning, testKanjiWriting.getMeaning());
        daoHelper.update(testKanjiWriting);
        assertEquals(testKanjiWriting, daoHelper.getKanjiWriting(testKanjiWriting.getKanji()));
        //delete
        assertEquals(1,daoHelper.delete(testKanjiWriting));
        assertNull(daoHelper.getKanjiWriting(testKanjiWriting.getKanji()));

        //getBySource

        KanjiWriting sourceItem1= new KanjiWriting("山","やま","サン",3,"mountain", "Udemy.01");
        KanjiWriting sourceItem2= new KanjiWriting("八","やっ（つ）","ハチ",2,"eight", "Udemy.02");
        KanjiWriting sourceItem3= new KanjiWriting("天","","テン",4,"heaven", "Udemy.12");
        daoHelper.insert(sourceItem1);
        daoHelper.insert(sourceItem2);
        daoHelper.insert(sourceItem3);

        Set<String> sourceList = new HashSet<>();
        sourceList.add("Udemy.01");

        //query just 1, test similar strings
        List<KanjiWriting> kanjiWritingList = daoHelper.getBySource(10,sourceList);
        assertEquals(1, kanjiWritingList.size());
        assertTrue(kanjiWritingList.contains(sourceItem1));

        //test Set search, should return 3
        sourceItem3.addSource("Udemy.01");
        sourceItem3.addSource("Udemy.02");
        daoHelper.update(sourceItem3);
        kanjiWritingList = daoHelper.getBySource(10,sourceList);
        assertEquals(2, kanjiWritingList.size());
        assertTrue(kanjiWritingList.contains(sourceItem1));
        assertTrue(kanjiWritingList.contains(sourceItem3));

        // Search
        List<KanjiWriting> searchResult = daoHelper.search(KanjiWritingDaoHelper.Columns.kanji, "山");
        assertEquals(1, searchResult.size());
        assertTrue(searchResult.contains(sourceItem1));

        searchResult = daoHelper.search(KanjiWritingDaoHelper.Columns.japanese_reading, "や");
        assertEquals(2, searchResult.size());
        assertTrue(searchResult.contains(sourceItem1) && searchResult.contains(sourceItem2));

        searchResult = daoHelper.search(KanjiWritingDaoHelper.Columns.phonetic_reading, "ン");
        assertEquals(2, searchResult.size());
        assertTrue(searchResult.contains(sourceItem1) &&  searchResult.contains(sourceItem3));

        searchResult = daoHelper.search(KanjiWritingDaoHelper.Columns.strokes, "3");
        assertEquals(1, searchResult.size());
        assertTrue(searchResult.contains(sourceItem1));

        searchResult = daoHelper.search(KanjiWritingDaoHelper.Columns.meaning, "i");
        assertEquals(2, searchResult.size());
        assertTrue(searchResult.contains(sourceItem1) && searchResult.contains(sourceItem2));

        searchResult = daoHelper.search(KanjiWritingDaoHelper.Columns.sources, "d");
        assertEquals(3, searchResult.size());
        assertTrue(searchResult.contains(sourceItem1) && searchResult.contains(sourceItem2) && searchResult.contains(sourceItem3));
    }

    public void testGetSourceDaoHelper() {
        //TODO: do tests for methods
    }
}