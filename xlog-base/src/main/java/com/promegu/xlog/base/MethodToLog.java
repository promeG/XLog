package com.promegu.xlog.base;

import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

/**
 * Created by guyacong on 2015/3/9.
 */
public class MethodToLog {
    private int type;
    private String className;
    private String methodName;
    private List<String> parameterClasses;
    private List<String> parameterNames;

    public MethodToLog(int type, String className, String methodName, List<String> parameterClasses, List<String> parameterNames) {
        this.type = type;
        this.className = className;
        this.methodName = methodName;
        this.parameterClasses = parameterClasses;
        this.parameterNames = parameterNames;
    }

    public int getType() {
        return type;
    }

    public String getClassName() {
        return className;
    }

    public String getName() {
        return methodName;
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

    public boolean matchMethodOrConstructor(Member member){
        if(member instanceof Method){
            return matchMethod((Method) member);
        } else if(member instanceof  Constructor){
            return matchMethod((Constructor) member);
        }
        return false;
    }

    private boolean matchMethod(Method method){
        if(method == null){
            return false;
        }

        String otherClassName ;
        if(method.getDeclaringClass().getEnclosingClass() != null){
            // nested class
            otherClassName = method.getDeclaringClass().getName().replaceAll("\\$", ".");
        } else{
            otherClassName = method.getDeclaringClass().getName();
        }

        //1. method name
        if(methodName == null || !methodName.equals(method.getName())){
            return false;
        }

        //2. class name
        if(className == null || !className.equals(otherClassName)){
            return false;
        }

        //3. parameter count
        if(parameterClasses == null){
            return false;
        }

        if(parameterClasses.size() != method.getParameterTypes().length){
            return false;
        }

        //4. parameter types
        for(int i = 0; i < parameterClasses.size(); i++){
            String str = parameterClasses.get(i);
            if(str == null || !str.equals(method.getParameterTypes()[i].getName())){
                return false;
            }
        }

        return true;
    }

    private boolean matchMethod(Constructor constructor){
        if(constructor == null){
            return false;
        }

        String otherClassName ;
        int paramOffset = 0;
        if(constructor.getDeclaringClass().getEnclosingClass() != null){
            // nested class
            otherClassName = constructor.getDeclaringClass().getName().replaceAll("\\$", ".");
            if(Modifier.isStatic(constructor.getDeclaringClass().getModifiers())){
                //static nested class
                paramOffset = 0;
            } else{
                // inner class
                paramOffset = 1;
            }
        } else{
            otherClassName = constructor.getDeclaringClass().getName();
        }

        //1. method name
        if(!"<init>".equals(methodName)
                || constructor.getName() == null){
            return false;
        }

        //2. class name
        if(className == null || !className.equals(otherClassName)){
            return false;
        }

        //3. parameter count
        if(parameterClasses == null){
            return false;
        }

        if(parameterClasses.size() != (constructor.getParameterTypes().length - paramOffset)){
            return false;
        }

        //4. parameter types
        for(int i = 0; i < parameterClasses.size(); i++){
            String str = parameterClasses.get(i);
            if(str == null || !str.equals(constructor.getParameterTypes()[i + paramOffset].getName())){
                return false;
            }
        }

        return true;
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
                ", \"className\":" + "\"" + className + "\"" +
                ", \"methodName\":" + "\"" + methodName + "\"" +
                ", \"parameterClasses\":" + classesSb.toString() +
                ", \"parameterNames\":" + namesSb.toString() +
                '}';
    }
}
