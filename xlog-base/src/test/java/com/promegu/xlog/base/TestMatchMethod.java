package com.promegu.xlog.base;

/**
 * Created by guyacong on 2015/7/8.
 */
public class TestMatchMethod {
    private int mInt;
    private Long mLong;
    private MethodToLog mMethodToLog;

    @XLog
    public TestMatchMethod(){

    }

    @XLog
    public TestMatchMethod(int anInt) {
        mInt = anInt;
    }

    @XLog
    public TestMatchMethod(int anInt, Long aLong, MethodToLog methodToLog) {
        mInt = anInt;
        mLong = aLong;
        mMethodToLog = methodToLog;
    }

    @XLog
    private void method1(){

    }

    @XLog
    private void method1(MethodToLog methodToLog){

    }

    @XLog
    private void method1(int i, Integer j){

    }

    public class InnerClass{

        @XLog
        public InnerClass() {
        }

        @XLog
        private void innerMethod(){

        }

        @XLog
        private void innerMethod(int i){

        }

        @XLog
        private void innerMethod(MethodToLog methodToLog){

        }
    }

    public static class StaticNestedClass{

        @XLog
        public StaticNestedClass() {
        }

        @XLog
        private void innerMethod(){

        }

        @XLog
        private void innerMethod(int i){

        }

        @XLog
        private void innerMethod(MethodToLog methodToLog){

        }
    }

}
