package com.rinzler.roomtodolist;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Note.class},version = 1)
public abstract class NoteDatabase extends RoomDatabase {

    //an abtract method which connects the dao interface
    public abstract  NoteDao noteDao();

    private static volatile NoteDatabase INSTANCE;

    //making a singleton of the database
    //we wont instantiate the database we just get the database to ensure only one instance is available
    static NoteDatabase getDatabaseInstance(final Context context){
        if (INSTANCE == null){
            //only one thread at a time
            synchronized (NoteDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),NoteDatabase.class,"note_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(roomCallBack)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsync(INSTANCE).execute();
        }
    };

    private static  class PopulateDbAsync extends AsyncTask<Void,Void,Void>{

        private final NoteDao noteDao;

        private PopulateDbAsync(NoteDatabase db){
            noteDao = db.noteDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.insert(new Note("Title1","Description here",1));
            noteDao.insert(new Note("Title2","Description here",2));
            return null;
        }

    }
}
