package com.promegu.xlog.base;

import java.util.List;

/**
 * Created by guyacong on 2015/3/9.
 */
public class MethodToLog {
    private String pkg;
    private String name;
    private List<String> parameterClasses;
    private List<String> parameterNames;

    public MethodToLog(String pkg, String name, List<String> parameterClasses, List<String> parameterNames) {
        this.pkg = pkg;
        this.name = name;
        this.parameterClasses = parameterClasses;
        this.parameterNames = parameterNames;
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
}
