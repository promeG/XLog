package com.promegu.xlog.base;

import java.util.List;

/**
 * Created by guyacong on 2015/3/9.
 */
public class MethodToLog {
    private int type;
    private String pkg;
    private String name;
    private List<String> parameterClasses;
    private List<String> parameterNames;

    public MethodToLog(int type, String pkg, String name, List<String> parameterClasses, List<String> parameterNames) {
        this.type = type;
        this.pkg = pkg;
        this.name = name;
        this.parameterClasses = parameterClasses;
        this.parameterNames = parameterNames;
    }

    public int getType() {
        return type;
    }

    public String getPkg() {
        return pkg;
    }

    public String getName() {
        return name;
    }

    public List<String> getParameterClasses() {
        return parameterClasses;
    }

    public List<String> getParameterNames() {
        return parameterNames;
    }

    public boolean isMethod() {
        return type == XLogUtils.TYPE_METHOD;
    }

    public boolean isConstructor() {
        return type == XLogUtils.TYPE_CONSTRUCTOR;
    }

}
