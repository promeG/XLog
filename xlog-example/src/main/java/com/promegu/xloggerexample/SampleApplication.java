package com.promegu.xloggerexample;


import com.github.promeg.xlog_android.lib.XLogConfig;
import com.promegu.xlog.base.XLogMethod;

import android.app.Application;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        List<XLogMethod> xLogMethods = new ArrayList<>();
        xLogMethods.add(new XLogMethod(TextView.class, "setText"));

        XLogConfig.config(XLogConfig.newConfigBuilder(this)
                .logMethods(xLogMethods)
                .timeThreshold(0)
                .build());
    }
}
