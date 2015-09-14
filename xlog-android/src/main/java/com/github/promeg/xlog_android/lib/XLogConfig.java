package com.github.promeg.xlog_android.lib;


import com.promegu.xlog.base.MethodToLog;
import com.promegu.xlog.base.XLog;
import com.promegu.xlog.base.XLogMethod;
import com.promegu.xlog.base.XLogSetting;
import com.promegu.xlog.base.XLogUtils;
import com.taobao.android.dexposed.DexposedBridge;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.util.Log;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
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
public final class XLogConfig {

    private static final String TAG = "XLogConfig";

    public static final String XLOG_SHARED_PREFERENCES = "xlog_settings";

    public static final String PREF_CONFIG = "xlog_config";

    public static final int NONE = 0;

    public static final int ANNOTATED = 1;

    public static final int SPECIFIED = 2;

    public static final int ALL = 3;

    public static final long TIME_THRESHOLD_NONE = -1L;

    /** TIME_THRESHOLD_BEFORE_HOOK must lessthan TIME_THRESHOLD_NONE **/
    public static final long TIME_THRESHOLD_BEFORE_HOOK = -2L;

    private XLogConfig() {
    }

    public static ConfigBuilder newConfigBuilder(Context context) {
        return new ConfigBuilder(context);
    }

    public static void config(final XLogInitializer initializer) {
        SharedPreferences sharedPreferences = initializer.getContext()
                .getSharedPreferences(XLOG_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(PREF_CONFIG, initializer.toString()).commit();
        if (DexposedBridge.canDexposed(initializer.getContext())) {
            hookAllMethods(initializer.getContext(), initializer.getTimeThreshold(),
                    initializer.getXLogMethods());
        }
    }

    private static void hookAllMethods(Context context, long timeThreshold,
            List<XLogMethod> xLogMethods) {
        try {
            XLogSetting xLogSetting = MultiDexHelper.getXLogSetting(context, XLogUtils.PKG_NAME);
            if (xLogSetting != null && xLogMethods != null) {
                List<String> classNames = new ArrayList<String>();
                for (XLogMethod xLogMethod : xLogMethods) {
                    if (xLogMethod != null && !classNames.contains(xLogMethod.getClassName())) {
                        classNames.add(xLogMethod.getClassName());
                    }
                }
                xLogSetting.appendPrefixes(
                        XLogUtils.getPkgPrefixesForCoarseMatchXLogMethods(xLogMethods, 2),
                        classNames);
            }
            Set<Member> methodsToHook = MultiDexHelper
                    .getAllMethodsWithAnnoation(context, XLog.class, xLogSetting, xLogMethods);
            if (methodsToHook == null) {
                return;
            }
            for (Member member : methodsToHook) {
                MethodToLog methodToLog = null;
                if (member instanceof Method || member instanceof Constructor) {
                    for (MethodToLog m : xLogSetting.methodToLogs) {
                        if (m != null && m.matchMethodOrConstructor(member)) {
                            methodToLog = m;
                            break;
                        }
                    }
                    DexposedBridge.hookMethod(member,
                            new XLogMethodHook(member, methodToLog, timeThreshold));
                    Log.d(TAG, "hooked: " + member.toString());
                }
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static final class ConfigBuilder {

        final Context mContext;

        int mBenchmark = NONE;

        long mTimeThreshold = TIME_THRESHOLD_NONE;

        List<XLogMethod> mXLogMethods = null;

        private ConfigBuilder(Context context) {
            mContext = context.getApplicationContext();
        }

        // TODO add this function
        private ConfigBuilder benchmark(int benchmark) {
            this.mBenchmark = benchmark;
            return this;
        }

        public ConfigBuilder timeThreshold(long timeInMillis) {
            mTimeThreshold = timeInMillis;
            return this;
        }

        public ConfigBuilder logMethods(List<XLogMethod> xLogMethods) {
            this.mXLogMethods = xLogMethods;
            return this;
        }

        public XLogInitializer build() {
            return new XLogInitializer(mContext, mBenchmark, mTimeThreshold, mXLogMethods);
        }
    }
}
