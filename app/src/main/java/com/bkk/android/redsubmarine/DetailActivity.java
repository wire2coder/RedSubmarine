package com.bkk.android.redsubmarine;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

// This class is for showing 1 Reddit post
public class DetailActivity extends AppCompatActivity {

    private static final String CLASS_TAG = DetailActivity.class.getSimpleName();
    private static final String LOG_TAG = "ttt>>>: ";

    // class variables
    String id;
    Bundle bundle1;
    Toolbar toolbar1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail); // TODO: 9/18 add code to activity_detail.xml file

        setSupportActionBar(toolbar1); // set up the top Action Bar
        ActionBar actionBar1 = getSupportActionBar();
        if (actionBar1 != null) {
            actionBar1.setDisplayHomeAsUpEnabled(true); // setting the "back" button for the action bar
        }


    } // onCreate()



} // class DetailActivity
