package com.promegu.xlog.base;

import java.lang.reflect.Member;

/**
 * Created by guyacong on 15/8/8.
 */
public class XLogMethod {

    public XLogMethod(Member member) {
    }

    public XLogMethod(Class clazz, String methodName) {
    }

    public XLogMethod(String className, String methodName) {
    }

    public String getClassName() {
        return null;
    }

    public String getMethodName() {
        return null;
    }
}
