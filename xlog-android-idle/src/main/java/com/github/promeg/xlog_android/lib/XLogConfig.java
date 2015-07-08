package com.github.promeg.xlog_android.lib;


import android.content.Context;


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

    public static void config(final XLogInitializer initializer) {

    }

    public static class ConfigBuilder {

        private ConfigBuilder(Context context) {
        }

        public ConfigBuilder benchmark(int benchmark) {
            return this;
        }

        public XLogInitializer build() {
            return new XLogInitializer(null, 0);
        }
    }
}
