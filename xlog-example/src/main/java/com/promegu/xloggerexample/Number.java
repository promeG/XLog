package com.promegu.xloggerexample;

import com.promegu.xlog.base.XLog;

/**
 * Created by guyacong on 2015/3/9.
 */
public class Number {
    int number;

    public Number(int number) {
        this.number = number;
    }

    @Override
    @XLog
    public String toString() {
        return "Number{" +
                "number=" + number +
                '}';
    }
}
