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

    @Override
    public String toString() {
        StringBuilder classesSb = new StringBuilder();
        classesSb.append("[");
        if(parameterClasses != null) {
            for (int i = 0; i < parameterClasses.size(); i++) {
                String className = parameterClasses.get(i);
                classesSb.append("\"" + className + "\"");
                if(i < parameterClasses.size() -1){
                    classesSb.append(",");
                }
            }
        }
        classesSb.append("]");

        StringBuilder namesSb = new StringBuilder();
        namesSb.append("[");
        if(parameterNames != null) {
            for (int i = 0; i < parameterNames.size(); i++) {
                String className = parameterNames.get(i);
                namesSb.append("\"" + className + "\"");
                if(i < parameterNames.size() -1){
                    namesSb.append(",");
                }
            }
        }
        namesSb.append("]");

        return "{" +
                "\"type\":" + type +
                ", \"pkg\":" + "\"" + pkg + "\"" +
                ", \"name\":" + "\"" + name + "\"" +
                ", \"parameterClasses\":" + classesSb.toString() +
                ", \"parameterNames\":" + namesSb.toString() +
                '}';
    }
}
