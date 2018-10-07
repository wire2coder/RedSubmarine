package com.bkk.android.redsubmarine.widget;

import android.content.Intent;
import android.database.Cursor;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bkk.android.redsubmarine.database.AppDatabase;

public class RedWidgetRemoteViewServices extends RemoteViewsService {

    // class variables
    private static String LOG_TAG = RedWidgetRemoteViewServices.class.getSimpleName();


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        return new RemoteViewsFactory() {

            // App database
            AppDatabase mDb;

            @Override
            public void onCreate() {

            }

            @Override
            public void onDataSetChanged() {

            // App database getAll()

            if (mDb != null) {
                mDb.close();
            }

                mDb = AppDatabase.getsInstance( getBaseContext().getApplicationContext() );
                // TODO: this is not going anywhere
                mDb.redditPostDao().loadAllSavedRedditPost();

            } // onDataSetChanged()

            @Override
            public void onDestroy() {

                if (mDb != null) {
                    mDb.close();
                }

            }

            @Override
            public int getCount() {
                return 5;
            }

            @Override
            public RemoteViews getViewAt(int position) {
                return null;

                // TODO: 10/7 HERE
            }

            @Override
            public RemoteViews getLoadingView() {
                return null;
            }

            @Override
            public int getViewTypeCount() {
                return 0;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }

        }; // return

    } // RemoteViewFactory()

} // class RedWidgetRemoteViewServices
