package com.github.promeg.xlog_android.lib;


import com.promegu.xlog.base.XLogMethod;

import android.content.Context;

import java.util.List;


public class XLogConfig {

    public static final String XLOG_SharedPreferences = "xlog_settings";

    public static final String PREF_CONFIG = "xlog_config";

    public static final int NONE = 0;

    public static final int ANNOTATED = 1;

    public static final int SPECIFIED = 2;

    public static final int ALL = 3;

    public static final long TimeThreshold_NONE = -1L;

    public static final long TimeThreshold_BEFORE_HOOK = -2L;

    private XLogConfig() {
    }

    public static ConfigBuilder newConfigBuilder(Context context) {
        return new ConfigBuilder(context);
    }

    public static void config(final XLogInitializer initializer) {

    }

    public static class ConfigBuilder {

        private ConfigBuilder(Context context) {
        }

        private ConfigBuilder benchmark(int benchmark) {
            return this;
        }

        public ConfigBuilder timeThreshold(long timeInMillis) {
            return this;
        }

        public ConfigBuilder logMethods(List<XLogMethod> xLogMethods) {
            return this;
        }

        public XLogInitializer build() {
            return new XLogInitializer();
        }
    }
}
