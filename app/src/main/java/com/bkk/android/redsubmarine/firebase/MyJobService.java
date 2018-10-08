package com.bkk.android.redsubmarine.firebase;

import android.util.Log;

import com.bkk.android.redsubmarine.DetailActivity;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

// http://blogs.quovantis.com/how-to-schedule-jobs-in-android-using-firebase-job-dispatcher/
public class MyJobService extends JobService {

    // class variables
    private static final String LOG_TAG = MyJobService.class.getSimpleName();

    @Override
    public boolean onStartJob(JobParameters job) {
        // Do some work here
        Log.d(LOG_TAG, "onStartJob()" );
        return false; // Answers the question: "Is there still work going on?"
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false; // Answers the question: "Should this job be retried?"
    }

} // class MyJobService
