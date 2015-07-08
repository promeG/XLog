package com.github.promeg.xlog_android.lib;

import android.content.Context;

/**
 * Created by guyacong on 2015/4/21.
 */
public class XLogInitializer {

    private static final String TAG = "XLogInitializer";

    private transient final Context context;
    private int mBenchmark;

    public XLogInitializer(Context context, int mBenchmark) {
        this.context = context;
        this.mBenchmark = mBenchmark;
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
