package com.promegu.xloggerexample;


import com.github.promeg.xlog_android.lib.XLogConfig;
import com.promegu.xlog.base.XLogMethod;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

public class SampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        List<XLogMethod> xLogMethods = new ArrayList<>();
        xLogMethods.add(new XLogMethod(BaseCalculator.class, "calculate"));

        XLogConfig.config(XLogConfig.newConfigBuilder(this)
                .logMethods(xLogMethods)
                .build());
    }
}
