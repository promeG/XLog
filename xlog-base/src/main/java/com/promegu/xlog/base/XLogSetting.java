package com.promegu.xlog.base;

import java.util.List;
import java.util.Set;

/**
 * Created by guyacong on 2015/7/12.
 */
public class XLogSetting {
    public Set<MethodToLog> methodToLogs;
    public Set<String> XLoggerMethodsClassNames;
    public List<String> xlogClassPrefixes;
    public List<String> xlogClassNames;

    public XLogSetting(Set<MethodToLog> methodToLogs,
            Set<String> XLoggerMethodsClassNames, List<String> xlogClassPrefixes,
            List<String> xlogClassNames) {
        this.methodToLogs = methodToLogs;
        this.XLoggerMethodsClassNames = XLoggerMethodsClassNames;
        this.xlogClassPrefixes = xlogClassPrefixes;
        this.xlogClassNames = xlogClassNames;
    }
}
