package com.promegu.xloggerexample;


import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.github.promeg.xlog_android.lib.XLogConfig;

public class SampleApplication extends Application {

    private static final String TAG = "SampleApplication";
    @Override
    public void onCreate() {
        super.onCreate();

        final Context context = this;
        long startTime = System.currentTimeMillis();
        XLogConfig.config(XLogConfig.newConfigBuilder(context)
                .benchmark(XLogConfig.ANNOTATED)
                .build());
        Log.d(TAG, "onCreate cost:  " + (System.currentTimeMillis() - startTime));
    }
}
