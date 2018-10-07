package com.bkk.android.redsubmarine.widget;

import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bkk.android.redsubmarine.R;
import com.bkk.android.redsubmarine.database.AppDatabase;
import com.bkk.android.redsubmarine.database.RedditPostEntry;
import com.bkk.android.redsubmarine.model.RedditPost;

import java.util.List;

public class RedWidgetRemoteViewServices extends RemoteViewsService {

    // class variables
    private static String LOG_TAG = RedWidgetRemoteViewServices.class.getSimpleName();


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        return new RemoteViewsFactory() {

            // App database
            private AppDatabase mDb;
            private List<RedditPostEntry> asdf1;

            @Override
            public void onCreate() {

            }

            // TODO: 10/7 onDataSetChanged() not trigger, need to add some code to DetailFragment.java
            @Override
            public void onDataSetChanged() {
                Log.d(LOG_TAG,"onDataSetChanged() ");

            // App database getAll()

//            if (mDb != null) {
//                mDb.close();
//            }

                mDb = AppDatabase.getsInstance( getBaseContext().getApplicationContext() );
                asdf1 = mDb.redditPostDao().loadAllSavedRedditPost();

            } // onDataSetChanged()


            @Override
            public int getCount() {
                Log.d(LOG_TAG,"getCount() " + asdf1.size() );
                return   (asdf1 == null) ? 0 : asdf1.size() ;
            } // getCount()


            @Override
            public RemoteViews getViewAt(int position) {

                if ( mDb == null ) {
                    Log.d(LOG_TAG, "asdf1 is null");
                    return null;
                }

                String post_title = asdf1.get(position).getTitle();
                String post_sub_reddit_name = asdf1.get(position).getSubreddit();
                int num_of_votes = asdf1.get(position).getScore();
                int num_of_comments = asdf1.get(position).getNumberOfComments();

                //                Log.d(LOG_TAG, "title > " + title );
                //                Log.d(LOG_TAG, "subtitle > " + subtitle );

                // making "one view" for the Widget
                RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.widget_item_list_view);

                remoteViews.setTextViewText(R.id.widget_post_title, post_title);
                remoteViews.setTextViewText(R.id.widget_sub_reddit_name, post_sub_reddit_name);
                remoteViews.setTextViewText(R.id.widget_post_vote, String.valueOf(num_of_votes) );
                remoteViews.setTextViewText(R.id.widget_post_comments, String.valueOf(num_of_comments) );


                // TODO: 10/7 why does stuff not showing in the Widget?
                return remoteViews; // RemoteViews, need to return "RemoteViews" thing
            } // getViewAt()


            // This allows for the use of a custom loading view which appears between the time that #getViewAt(int) is called and returns.
            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews( getPackageName(), R.layout.widget_item_list_view );
            }


            @Override
            public void onDestroy() {
                if (mDb != null) {
                    mDb.close();
                }
            } // onDestroy()


            // If the adapter always returns the same type of View for all items, this method should return 1.
            @Override
            public int getViewTypeCount() {
                return 1;
            }

//            TODO: 10/7 here, look below this line.

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
