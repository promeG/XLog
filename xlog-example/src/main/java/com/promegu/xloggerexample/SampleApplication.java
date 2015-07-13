package com.promegu.xloggerexample;


import com.github.promeg.xlog_android.lib.XLogConfig;

import android.app.Application;

public class SampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        XLogConfig.config(XLogConfig.newConfigBuilder(this)
                .build());
    }
}
