package com.github.promeg.xlog_android.lib;

import com.promegu.xlog.base.XLogMethod;

import android.content.Context;

import java.util.List;

/**
 * Created by guyacong on 2015/4/21.
 */
final class XLogInitializer {

    private static final String TAG = "XLogInitializer";

    private transient final Context context;
    private int mBenchmark;
    private List<XLogMethod> mXLogMethods;

    public XLogInitializer(Context context, int benchmark,
            List<XLogMethod> XLogMethods) {
        this.context = context;
        mBenchmark = benchmark;
        mXLogMethods = XLogMethods;
    }

    public List<XLogMethod> getXLogMethods() {
        return mXLogMethods;
    }

    public Context getContext() {
        return context;
    }

    public int getBenchmark() {
        return mBenchmark;
    }

    @Override
    public String toString(){
        return "{" +
                "\"mBenchmark\":" + mBenchmark +
                '}';
    }
}
