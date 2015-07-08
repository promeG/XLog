package com.github.promeg.xlog_android.lib;


import com.promegu.xlog.base.MethodToLog;
import com.promegu.xlog.base.XLog;
import com.promegu.xlog.base.XLogUtils;
import com.taobao.android.dexposed.DexposedBridge;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Set;

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

    private static final String TAG = "XLogConfig";

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
        SharedPreferences sharedPreferences = initializer.getContext().getSharedPreferences(XLOG_SharedPreferences, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(PREF_CONFIG, initializer.toString()).commit();
        if (DexposedBridge.canDexposed(initializer.getContext())) {
            hookAllMethods(initializer.getContext());
        }
    }

    private static void hookAllMethods(Context context){
        Set<Member> allMethods = MultiDexHelper.getAllMethodsWithAnnoation(context, XLog.class);
        Set<MethodToLog> methodToLogs = MultiDexHelper.getAllMethodToLog(context, XLogUtils.PKG_NAME);
        if(allMethods == null){
            return;
        }
        for(Member member : allMethods){
            MethodToLog methodToLog = null;
            if(member instanceof Method || member instanceof Constructor){
                for(MethodToLog m : methodToLogs){
                    if(m != null && m.matchMethodOrConstructor(member)){
                        methodToLog = m;
                        break;
                    }
                }
                DexposedBridge.hookMethod(member, new XLogMethodHook(member, methodToLog));
                Log.d(TAG, "hooked: " + member.toString());
            }
        }
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
