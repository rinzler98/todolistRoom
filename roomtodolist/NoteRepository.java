package com.rinzler.roomtodolist;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteRepository {

    private NoteDao noteDao;
    private LiveData<List<Note>> allNotes;

    public NoteRepository(Application application){
        NoteDatabase db = NoteDatabase.getDatabaseInstance(application);
        noteDao = db.noteDao();
        allNotes = noteDao.getAllNotes();
    }

    //todo : the crud operations

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

    public void insert(Note note){
        insertRunnable in = new insertRunnable(noteDao,note);
        new Thread(in).start();
    }
    public void delete(Note note){
        deleteRunnable del = new deleteRunnable(noteDao,note);
        new Thread(del).start();
    }

    public void update(Note note){
        updateRunnable up = new updateRunnable(noteDao,note);
        new Thread(up).start();
    }

    public void deleteAll(){
        deletAllRunnable delAll = new deletAllRunnable(noteDao);
        new Thread(delAll).start();
    }


    //todo threads to run in the background
    private class insertRunnable implements Runnable {
        private NoteDao noteDao;
        private Note note;

        private insertRunnable(NoteDao noteDao,Note note){
            this.noteDao = noteDao;
            this.note = note;
        }

        @Override
        public void run() {
            noteDao.insert(note);
        }
    }
    private class deleteRunnable implements Runnable {
        private NoteDao noteDao;
        private Note note;

        private deleteRunnable(NoteDao noteDao,Note note){
            this.noteDao = noteDao;
            this.note = note;
        }

        @Override
        public void run() {
            noteDao.delete(note);
        }
    }
    private class updateRunnable implements Runnable {
        private NoteDao noteDao;
        private Note note;

        private updateRunnable(NoteDao noteDao,Note note){
            this.noteDao = noteDao;
            this.note = note;
        }

        @Override
        public void run() {
            noteDao.update(note);
        }
    }
    private class deletAllRunnable implements Runnable {
        private NoteDao noteDao;

        private deletAllRunnable(NoteDao noteDao){
            this.noteDao = noteDao;

        }
        @Override
        public void run() {
            noteDao.deleteAll();
        }
    }
}
