package com.bkav.demochat;

import android.app.job.JobParameters;
import android.os.Build;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
class JobService extends android.app.job.JobService {
    @Override
    public boolean onStartJob(JobParameters params) {
        doBackground(params);
        return true;
    }

    private void  doBackground(JobParameters params){

    }
    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
