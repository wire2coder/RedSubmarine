package com.bkk.android.redsubmarine.widget;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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


            @Override
            public void onDataSetChanged() {
                Log.d(LOG_TAG,"onDataSetChanged() ");

                // Android Room
                mDb = AppDatabase.getsInstance( getBaseContext().getApplicationContext() );
                asdf1 = mDb.redditPostDao().loadAllSavedRedditPost();

            } // onDataSetChanged()


            @Override
            public int getCount() {
                Log.d(LOG_TAG,"getCount() " + asdf1.size() );
                return (asdf1 == null) ? 0 : asdf1.size() ;
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


                // making "one view" for the Widget
                RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.widget_item_list_view);

                remoteViews.setTextViewText(R.id.widget_post_title, post_title);
                remoteViews.setTextViewText(R.id.widget_sub_reddit_name, "/r/" + post_sub_reddit_name);
                remoteViews.setTextViewText(R.id.widget_post_vote, String.valueOf(num_of_votes) );
                remoteViews.setTextViewText(R.id.widget_post_comments, String.valueOf(num_of_comments) );


                RedditPost redditPost1 = new RedditPost(
                        asdf1.get(position).getTitle(),
                        asdf1.get(position).getThumbnail(),
                        asdf1.get(position).getUrl(),
                        asdf1.get(position).getSubreddit(),
                        asdf1.get(position).getAuthor(),
                        asdf1.get(position).getPermalink(),
                        asdf1.get(position).getPost_id(),
                        asdf1.get(position).getSubreddit_name_prefixed(),
                        asdf1.get(position).getScore(),
                        asdf1.get(position).getNumberOfComments(),
                        asdf1.get(position).getPostedDate(),
                        asdf1.get(position).getOver18()
                );

                Intent intentingToOpenDetailActivity = new Intent();
                Bundle bundle1 = new Bundle();
                bundle1.putParcelable("redditPost1", redditPost1);

                intentingToOpenDetailActivity.putExtras(bundle1);

                remoteViews.setOnClickFillInIntent(R.id.widget_item_list_view, intentingToOpenDetailActivity);

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

            @Override
            public long getItemId(int position) {
                Log.i(LOG_TAG, "getItemId() " + String.valueOf(position));
                return position;
            }


            @Override
            public boolean hasStableIds() {
                return false;
            }

        }; // return

    } // RemoteViewFactory()

} // class RedWidgetRemoteViewServices
