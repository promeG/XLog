package com.promegu.xlog.base;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.junit.Test;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.nullValue;

/**
 * Created by guyacong on 2015/7/8.
 */
public class XLogUtilsTest {
    static final List<String> classNames = new ArrayList<>();
    static {
        classNames.add("com.promegu.xlog.base.XLog");
        classNames.add("com.promegu.xlog.base.MethodToLog");
        classNames.add("com.promegu.other.class1");
        classNames.add("com.promegu.other.class2");
        classNames.add("java.lang.Object");
        classNames.add("java.lang.test.Object");
        classNames.add("retrofit");
        classNames.add("retrofit.utils");
    }

    @Test
    public void testFilterResult1() {
        Set<MethodToLog> methodToLogs = new Gson()
                .fromJson(MethodToLogTest.METHODS_TO_LOG, new TypeToken<Set<MethodToLog>>() {
                }.getType());

        List<String> xlogClassNames = new ArrayList<>();
        for(MethodToLog methodToLog : methodToLogs){
            xlogClassNames.add(methodToLog.getClassName());
        }

        XLogSetting xLogSetting = new XLogSetting(methodToLogs, null, XLogUtils.getPkgPrefixesForCoarseMatch(xlogClassNames, 2), xlogClassNames);


        assertThat(XLogUtils.filterResult("com.promegu.xlog.base.TestMatchMethod.InnerClass",
                xLogSetting), is(true));
        assertThat(XLogUtils.filterResult("com.promegu.xlog.base.TestMatchMethod.InnerClass$1",
                xLogSetting), is(true));
        assertThat(XLogUtils.filterResult("com.promegu.xlog.base.TestMatchMethod.InnerClass.1",
                xLogSetting), is(true));
        assertThat(XLogUtils.filterResult("com.promegu.xlog",
                xLogSetting), is(false));
    }

    @Test
    public void testGetPkgPrefixesForCoarseMatch1() {
        // null or empty classNames --> empty list
        assertThat(XLogUtils.getPkgPrefixesForCoarseMatch(null, 1).size(), is(0));
        assertThat(XLogUtils.getPkgPrefixesForCoarseMatch(new ArrayList<String>(), 1).size(), is(0));
    }

    @Test
    public void testGetPkgPrefixesForCoarseMatch2() {
        // pkgSectionSize <= 0 --> result = classNames
        assertThat(XLogUtils.getPkgPrefixesForCoarseMatch(classNames, -1).size(), is(classNames.size()));
        assertThat(XLogUtils.getPkgPrefixesForCoarseMatch(classNames, 0).size(), is(classNames.size()));
    }

    @Test
    public void testGetPkgPrefixesForCoarseMatch3() {
        List<String> expected = new ArrayList<>();
        expected.add("com.promegu");
        expected.add("java.lang");
        expected.add("retrofit");
        assertThat(XLogUtils.getPkgPrefixesForCoarseMatch(classNames, 2), is(expected));

        expected.clear();
        expected.add("com.promegu.xlog");
        expected.add("com.promegu.other");
        expected.add("java.lang.Object");
        expected.add("java.lang.test");
        expected.add("retrofit");
        assertThat(XLogUtils.getPkgPrefixesForCoarseMatch(classNames, 3), is(expected));
    }

    @Test
    public void testGetClassNameSectionSize() {
        assertThat(XLogUtils.getClassNameSectionSize(""), is(0));
        assertThat(XLogUtils.getClassNameSectionSize(null), is(0));
        assertThat(XLogUtils.getClassNameSectionSize("com"), is(1));
        assertThat(XLogUtils.getClassNameSectionSize("com.promegu"), is(2));
        assertThat(XLogUtils.getClassNameSectionSize("com.promegu.xlog.base"), is(4));
    }
    @Test
    public void testGetClassNameSection() {
        assertThat(XLogUtils.getClassNameSection("", 0), is(""));
        assertThat(XLogUtils.getClassNameSection(null, 0), nullValue());
        assertThat(XLogUtils.getClassNameSection("com", -1), is("com"));
        assertThat(XLogUtils.getClassNameSection("com", 2), is("com"));
        assertThat(XLogUtils.getClassNameSection("com.promegu", 1), is("com"));
        assertThat(XLogUtils.getClassNameSection("com.promegu", 2), is("com.promegu"));
        assertThat(XLogUtils.getClassNameSection("com.promegu.xlog.base", 2), is("com.promegu"));
        assertThat(XLogUtils.getClassNameSection("com.promegu.xlog.base", 4), is("com.promegu.xlog.base"));
    }


}
