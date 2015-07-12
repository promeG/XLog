package com.promegu.xloggerexample;


import com.promegu.xlog.base.XLog;

/**
 * Created by guyacong on 2015/7/12.
 */
@XLog
public class BaseCalculator {

    public String getName(){
        return "BaseCalculator";
    }

    public int calculator(int i, int j){
        return i+j;
    }
}
