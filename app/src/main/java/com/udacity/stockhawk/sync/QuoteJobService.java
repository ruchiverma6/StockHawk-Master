package com.udacity.stockhawk.sync;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;

import com.udacity.stockhawk.R;

import timber.log.Timber;

public class QuoteJobService extends JobService {


    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Timber.d(getString(R.string.intent_handled_log_msg));
        Intent nowIntent = new Intent(getApplicationContext(), QuoteIntentService.class);
        getApplicationContext().startService(nowIntent);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }


}
