package com.promegu.xlog.xposedmodule;

import android.os.Looper;
import android.util.Log;
import com.promegu.xlog.base.MethodToLog;
import de.robv.android.xposed.XC_MethodHook;

/**
 * Created by guyacong on 2015/3/10.
 */
public class XLogMethodHook extends XC_MethodHook {
    private long startTime;
    private MethodToLog methodToLog;
    private Class declaringClass;

    public XLogMethodHook(MethodToLog methodToLog, Class declaringClass) {
        this.methodToLog = methodToLog;
        this.declaringClass = declaringClass;
    }

    @Override
    protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
        // this will be called before the clock was updated by the original method

        StringBuilder builder = new StringBuilder("\u21E2 ");
        builder.append(param.method.getName()).append('(');
        for (int i = 0; i < param.args.length; i++) {
            if (i > 0) {
                builder.append(", ");
            }
            builder.append(methodToLog.getParameterNames().get(i)).append('=');
            builder.append(Strings.toString(param.args[i]));
        }
        builder.append(')');
        if (Looper.myLooper() != Looper.getMainLooper()) {
            builder.append(" [Thread:\"").append(Thread.currentThread().getName()).append("\"]");
        }
        Log.d(asTag(declaringClass), builder.toString());
        startTime = System.currentTimeMillis();
    }

    @Override
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        // this will be called after the clock was updated by the original method
        long lengthMillis = System.currentTimeMillis() - startTime;
        StringBuilder builder = new StringBuilder("\u21E0 ")
                .append(param.method.getName())
                .append(" [")
                .append(lengthMillis)
                .append("ms]");

        if (param.getResult() != null) {
            builder.append(" = ");
            builder.append(Strings.toString(param.getResult()));
        }
        Log.d(asTag(declaringClass), builder.toString());
    }

    /**
     * from https://github.com/JakeWharton/hugo
     */
    private static String asTag(Class<?> cls) {
        if (cls.isAnonymousClass()) {
            return asTag(cls.getEnclosingClass());
        }
        return cls.getSimpleName();
    }

}
