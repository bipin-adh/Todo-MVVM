package com.example.architectureexample;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteRepository {

    private NoteDao noteDao;
    private LiveData<List<Note>> allNotes;

    /**
     * Application is a subclass of context
     *
     * @param application
     */
    public NoteRepository(Application application){

        NoteDatabase database = NoteDatabase.getInstance(application);
        noteDao = database.noteDao();
        allNotes = noteDao.getAllNotes();
    }

    /**
     * Room automatically execute db operation that returns the livedata on the background threads.
     * i.e. public LiveData<List<Note >> getAllNotes();
     *
     *  But for other db operations , we have to operate on background thread ourselves.
     *  (otherwise app will crash/freeze)  i.e. insert(), update(), delete() , deleteAllNotes()
     *
     *  Since Room doesn't allow database queries on the main thread,
     *  we use AsyncTasks to execute them asynchronously.
     *
     * @param note
     */
    public void insert(Note note){

        new InsertNoteAsyncTask(noteDao).execute(note);
    }
    public void update(Note note){

        new UpdateNoteAsyncTask(noteDao).execute(note);
    }
    public void delete(Note note){

        new DeleteNoteAsyncTask(noteDao).execute(note);
    }
    public void deleteAllNotes(){

        new DeleteAllNotesAsyncTask(noteDao).execute();
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

    /**
     * It has to be static because it does not have a reference to a repository class.Otherwise
     * this will cause a memory leak.
     */
    public static class InsertNoteAsyncTask extends AsyncTask<Note,Void,Void>{

        private NoteDao noteDao;

        private InsertNoteAsyncTask (NoteDao noteDao){

            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.insert(notes[0]);
            return null;
        }
    }

    private static class UpdateNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;

        private UpdateNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.update(notes[0]);
            return null;
        }
    }

    private static class DeleteNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;

        private DeleteNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.delete(notes[0]);
            return null;
        }
    }

    private static class DeleteAllNotesAsyncTask extends AsyncTask<Void, Void, Void> {
        private NoteDao noteDao;

        private DeleteAllNotesAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.deleteAllNotes();
            return null;
        }
    }
}