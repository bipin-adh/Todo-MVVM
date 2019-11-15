package com.example.architectureexample;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

/**
 * The RoomDatabase is an abstract class that ties all the pieces together and
 * connects the entities to their corresponding DAO.
 * Just as in an SQLiteOpenHelper, we have to define a version number and a migration strategy.
 *
 *  With fallbackToDestructiveMigration we can let Room recreate our database if we increase the version number.
 *
 *  We create our database in form of a static singleton with the databaseBuilder,
 *  where we have to pass our database class and a file name.
 */
@Database(entities = {Note.class}, version = 1)
public abstract class NoteDatabase extends RoomDatabase {

    private static NoteDatabase instance;

    public abstract NoteDao noteDao();

    // synchronized means only one thread at a time can access this method.
    public static synchronized NoteDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    NoteDatabase.class, "note_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallBack)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };
    private static class PopulateDbAsyncTask extends AsyncTask<Void,Void,Void>{

        private NoteDao noteDao;

        private PopulateDbAsyncTask(NoteDatabase db){

            noteDao = db.noteDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.insert(new Note("title1","desc1",1));
            noteDao.insert(new Note("title2","desc2",2));
            noteDao.insert(new Note("title3","desc3",3));
            noteDao.insert(new Note("title4","desc4",4));
            return null;
        }
    }
}