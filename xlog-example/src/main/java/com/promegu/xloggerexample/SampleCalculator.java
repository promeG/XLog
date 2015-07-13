package com.promegu.xloggerexample;

import com.promegu.xlog.base.XLog;

/**
 * Created by guyacong on 2015/7/12.
 */
@XLog
public class SampleCalculator extends BaseCalculator {

    @Override
    public String getName(){
        return "SampleCalculator";
    }
}
