package com.github.promeg.xlog_android.lib;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

/**
 * Initialization and configuration entry point for XLog.  Example usage:
 * <p/>
 * <pre>
 *   XLogConfig.config(XLogConfig.newConfigBuilder(context)
 *       .benchmark(XLogConfig.ANNOTATED)
 *       .build());
 * </pre>
 */
public class XLogConfig {
    public static final String XLOG_SharedPreferences = "xlog_settings";
    public static final String PREF_CONFIG = "xlog_config";

    public static final int NONE = 0;
    public static final int ANNOTATED = 1;
    public static final int SPECIFIED = 2;
    public static final int ALL = 3;

    private XLogConfig() {
    }

    public static ConfigBuilder newConfigBuilder(Context context) {
        return new ConfigBuilder(context);
    }

    public static void config(@NonNull final XLogInitializer initializer) {
        SharedPreferences sharedPreferences = initializer.getContext().getSharedPreferences(XLOG_SharedPreferences, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(PREF_CONFIG, initializer.toString()).commit();
    }


    public static class ConfigBuilder {
        final Context mContext;
        int mBenchmark = NONE;

        private ConfigBuilder(Context context) {
            mContext = context.getApplicationContext();
        }

        public ConfigBuilder benchmark(int benchmark) {
            this.mBenchmark = benchmark;
            return this;
        }

        public XLogInitializer build() {
            return new XLogInitializer(mContext, mBenchmark);
        }
    }
}
