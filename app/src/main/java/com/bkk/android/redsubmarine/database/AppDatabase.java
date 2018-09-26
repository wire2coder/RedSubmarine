package com.bkk.android.redsubmarine.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

// this is a SINGLETON class, meaning you can only create ONE object from this class
@Database(entities = {RedditPostEntry.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    // class variables
    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "savedpost";
    private static AppDatabase sInstance;


    public static AppDatabase getsInstance(Context context) {
        if (sInstance == null) {

            synchronized (LOCK) {
               Log.d(LOG_TAG, "Creating new database instance");
               sInstance = Room.databaseBuilder(
                       context.getApplicationContext()
                       ,AppDatabase.class
                       ,AppDatabase.DATABASE_NAME)

                       // Queries should be done in a separate thread to avoid lock the UI
                       // TODO: 9/26 We will disable this "method()" in production code
                       .allowMainThreadQueries()
                       .build();
            } // synchronized

        } // if

        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    } // constructor

    // what is this for again?
    public abstract RedditPostDao redditPostDao();

} // class AppDatabase
