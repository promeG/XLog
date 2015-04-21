package com.promegu.xloggerexample;


import android.app.Application;
import android.content.Context;
import com.github.promeg.xlog_android.lib.XLogConfig;

public class SampleApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        final Context context = this;
        XLogConfig.config(XLogConfig.newConfigBuilder(context)
                .benchmark(XLogConfig.ANNOTATED)
                .build());
    }
}
