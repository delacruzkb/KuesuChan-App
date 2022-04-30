package kuesuchan.jpns.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import kuesuchan.jpns.R;
import kuesuchan.jpns.database.dao.KanjiWritingDao;
import kuesuchan.jpns.database.dao.SourceDao;
import kuesuchan.jpns.database.dao.VocabularyDao;
import kuesuchan.jpns.database.dao.helper.KanjiWritingDaoHelper;
import kuesuchan.jpns.database.dao.helper.SourceDaoHelper;
import kuesuchan.jpns.database.dao.helper.VocabularyDaoHelper;
import kuesuchan.jpns.database.entity.KanjiWriting;
import kuesuchan.jpns.database.entity.Source;
import kuesuchan.jpns.database.entity.Vocabulary;

@Database(entities = {Vocabulary.class, KanjiWriting.class, Source.class}, version=1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public static enum Table{
        Vocabulary,
        Kanji_Writing,
        Source
    }
    public abstract VocabularyDao vocabularyDao();
    public abstract KanjiWritingDao kanjiWritingDao();
    public abstract SourceDao sourceDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, context.getString(R.string.db_name))
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public VocabularyDaoHelper getVocabularyDaoHelper(){
        return new VocabularyDaoHelper(vocabularyDao());
    }

    public KanjiWritingDaoHelper getKanjiWritingDaoHelper(){
        return new KanjiWritingDaoHelper(kanjiWritingDao());
    }

    public SourceDaoHelper getSourceDaoHelper(){
        return new SourceDaoHelper(sourceDao());
    }
}
