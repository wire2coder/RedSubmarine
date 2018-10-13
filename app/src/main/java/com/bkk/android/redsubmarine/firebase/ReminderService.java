package com.bkk.android.redsubmarine.firebase;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import com.bkk.android.redsubmarine.DetailActivity;
import com.bkk.android.redsubmarine.MainActivity;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

// http://blogs.quovantis.com/how-to-schedule-jobs-in-android-using-firebase-job-dispatcher/
public class ReminderService extends JobService {

    // class variables
    private static final String TAG = ReminderService.class.getSimpleName();


    StatFs statFs = new StatFs(Environment.getRootDirectory().getPath());

    @Override
    public boolean onStartJob(JobParameters job) {

        // Do some work here
        Log.i(TAG, "onStartJob");

        return false; // Answers the question: "Is there still work going on?"
    } // onStartJob()

    @Override
    public boolean onStopJob(JobParameters job) {

        // Do some work here
        Log.i(TAG, "onStopJob");

        return false; // Answers the question: "Should this job be retried?"
    } // onStopJob()


} // class ReminderService
