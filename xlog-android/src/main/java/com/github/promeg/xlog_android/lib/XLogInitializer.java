package com.github.promeg.xlog_android.lib;

import com.promegu.xlog.base.XLogMethod;

import android.content.Context;

import java.util.List;

/**
 * Created by guyacong on 2015/4/21.
 */
final class XLogInitializer {

    private static final String TAG = "XLogInitializer";

    private final transient Context context;

    private int mBenchmark;

    private long mTimeThreshold;

    private List<XLogMethod> mXLogMethods;

    public XLogInitializer(Context context, int benchmark, long timeThreshold,
            List<XLogMethod> xLogMethods) {
        this.context = context;
        mBenchmark = benchmark;
        mTimeThreshold = timeThreshold;
        mXLogMethods = xLogMethods;
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

    public long getTimeThreshold() {
        return mTimeThreshold;
    }

    @Override
    public String toString() {
        return "{"
                + "\"mBenchmark\":" + mBenchmark
                + '}';
    }
}
