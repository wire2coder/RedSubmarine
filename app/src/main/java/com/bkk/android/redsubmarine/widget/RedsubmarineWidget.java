package com.bkk.android.redsubmarine.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.bkk.android.redsubmarine.R;

import java.util.Arrays;

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

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
//        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.d( LOG_TAG,"onUpdate() " + Arrays.toString(appWidgetIds) );

        TODO: 10/6 HERE

    } // onUpdate

    // TODO: 10/6, need a helper setRemoteAdapter()

    @Override
    public void onEnabled(Context context) {
        // for when the first widget is created
        Log.d(LOG_TAG,"onEnabled()");
    } // onEnabled()

    @Override
    public void onDisabled(Context context) {
        // when the last widget is disabled
        Log.d(LOG_TAG,"onDisabled()");
    } // onDisabled()


} // class Redsubmarine Widget extends AppWidgetProvider
