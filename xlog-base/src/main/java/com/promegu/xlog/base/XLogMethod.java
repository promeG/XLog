package com.promegu.xlog.base;

import java.lang.reflect.Member;

/**
 * Created by guyacong on 15/8/8.
 */
public class XLogMethod {

    String mClassName;

    String mMethodName;

    public XLogMethod(Member member) {
        this(member.getDeclaringClass().getCanonicalName(), member.getName());
    }

    public XLogMethod(Class clazz, String methodName) {
        this(clazz.getCanonicalName(), methodName);
    }

    public XLogMethod(String className, String methodName) {
        mClassName = className;
        mMethodName = methodName;
    }

    public String getClassName() {
        return mClassName;
    }

    public String getMethodName() {
        return mMethodName;
    }
}
