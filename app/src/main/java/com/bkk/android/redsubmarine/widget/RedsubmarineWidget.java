package com.bkk.android.redsubmarine.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.RemoteViews;

import com.bkk.android.redsubmarine.DetailActivity;
import com.bkk.android.redsubmarine.R;

import java.util.Arrays;

import javax.annotation.Nonnegative;

// this file is for the Widget

public class RedsubmarineWidget extends AppWidgetProvider {

    // class variables
    private static String LOG_TAG = RedsubmarineWidget.class.getSimpleName();


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        Log.d(LOG_TAG,"onReceive() " + intent.getAction() + " >> " + context.getString(R.string.data_update_key) );

        if ( context.getString(R.string.data_update_key).equals( intent.getAction() ) ) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                    new ComponentName(context, getClass() ) );
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_item_list_view1);
        }

    } // onReceive

    // what does onUpdate() do?
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
//        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.d( LOG_TAG,"onUpdate() " + Arrays.toString(appWidgetIds) );

        // HERE
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_favorited_reddit_post);

            Intent intent = new Intent(context, DetailActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            views.setPendingIntentTemplate(R.id.widget_item_list_view1, pendingIntent);
            // what is a pending intent???

            // this is a helper function
            setRemoteAdapter(context, views);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        } // for

    } // onUpdate

    // TODO: rename this
    // this goes to RedWidgetRemoteViewServices.java
    private void setRemoteAdapter(Context context, @NonNull final RemoteViews views) {

        views.setRemoteAdapter(
                R.id.widget_item_list_view1
                , new Intent(context, RedWidgetRemoteViewServices.class) );

    } // setRemoteAdapter()

    @Override
    public void onEnabled(Context context) {
        // for when the first widget is created
    } // onEnabled()

    @Override
    public void onDisabled(Context context) {
        // when the last widget is disabled
    } // onDisabled()


} // class Redsubmarine Widget extends AppWidgetProvider
