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
import kuesuchan.jpns.database.dao.helper.SourceDaoHelper;
import kuesuchan.jpns.database.dao.helper.VocabularyDaoHelper;
import kuesuchan.jpns.database.entity.KanjiWriting;
import kuesuchan.jpns.database.entity.Source;
import kuesuchan.jpns.database.entity.Vocabulary;
import kuesuchan.jpns.database.tuple.SourceTuple;

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
        VocabularyDaoHelper vocabularyDaoHelper = db.getVocabularyDaoHelper();
        SourceDaoHelper sourceDaoHelper = db.getSourceDaoHelper();
        Vocabulary testVocabulary = new Vocabulary("English", "Kana", "Kanji", "Help+Text");
        String testSourceName = "Source_A";
        int testSourceSection = 1;

        //Insert Vocabulary
        vocabularyDaoHelper.insert(testVocabulary, testSourceName,  testSourceSection);
        // // Check if Source was created
        Source testSource = sourceDaoHelper.getSourcesBySourceId(testVocabulary.getSource_id()).get(0);
        assertNotNull(testSource);
        assertEquals(testSourceName, testSource.getName());
        assertEquals(testSourceSection, testSource.getSection());
        assertTrue(testSource.getSource_ids().contains(testVocabulary.getSource_id()));

        // Get Vocabulary
        Vocabulary dbVocab = vocabularyDaoHelper.getVocabulary(testVocabulary.getEnglish(), testVocabulary.getKana());
        assertNotNull(dbVocab);
        assertEquals(testVocabulary, dbVocab);

        // Update
        Set<SourceTuple> newTuples = new HashSet<>(sourceDaoHelper.getSourceTuplesBySourceId(testVocabulary.getSource_id()));
        newTuples.add(new SourceTuple("Source_A",2));
        String newHelpText = "Help_Text";
        testVocabulary.setHelp_text(newHelpText);
        // // adding sources
        vocabularyDaoHelper.update(testVocabulary,newTuples);
        Set<SourceTuple> dbTuples = new HashSet<>(sourceDaoHelper.getSourceTuplesBySourceId(testVocabulary.getSource_id()));
        assertTrue(dbTuples.containsAll(newTuples));
        assertTrue(newTuples.containsAll(dbTuples));
        assertEquals(testVocabulary, vocabularyDaoHelper.getVocabulary(testVocabulary.getEnglish(),testVocabulary.getKana()));
        // // remove sources
        Set<SourceTuple> deleteTuples = new HashSet<>(newTuples);
        deleteTuples.remove(testSource);
        vocabularyDaoHelper.update(testVocabulary,deleteTuples);
        dbTuples = new HashSet<>(sourceDaoHelper.getSourceTuplesBySourceId(testVocabulary.getSource_id()));
        assertTrue(dbTuples.containsAll(deleteTuples));
        assertTrue(deleteTuples.containsAll(dbTuples));
        assertEquals(testVocabulary, vocabularyDaoHelper.getVocabulary(testVocabulary.getEnglish(),testVocabulary.getKana()));

        //Delete
        vocabularyDaoHelper.delete(testVocabulary);
        assertNull(vocabularyDaoHelper.getVocabulary(testVocabulary.getEnglish(),testVocabulary.getKana()));
        assertNull(sourceDaoHelper.getSourcesBySourceId(testVocabulary.getSource_id()));

        //Search
        vocabularyDaoHelper.insert(testVocabulary, testSourceName,testSourceSection);
        List<Vocabulary> searchList = vocabularyDaoHelper.search(Vocabulary.Columns.english, "g");
        assertEquals(1, searchList);
        assertTrue(searchList.contains(testVocabulary));

        //getBySource
        Set<Integer> sections = new HashSet<>();
        sections.add(testSourceSection);
        List<Vocabulary> bySourceList = vocabularyDaoHelper.getBySource(testSourceName, sections, 1);
        assertEquals(1,searchList);
        assertTrue(bySourceList.contains(testVocabulary));








    }

    @Test
    public void testGetKanjiWritingDaoHelper() {

    }

    @Test
    public void testGetSourceDaoHelper() {
        //TODO: do tests for methods
    }
}