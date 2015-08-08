package com.promegu.xlog.base;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.junit.Test;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

/**
 * Created by guyacong on 2015/7/8.
 */
public class XLogUtilsTest {
    static final List<String> classNames = new ArrayList<>();
    static final List<XLogMethod> xlogMethods = new ArrayList<>();
    static {
        classNames.add("com.promegu.xlog.base.XLog");
        classNames.add("com.promegu.xlog.base.MethodToLog");
        classNames.add("com.promegu.other.class1");
        classNames.add("com.promegu.other.class2");
        classNames.add("java.lang.Object");
        classNames.add("java.lang.test.Object");
        classNames.add("retrofit");
        classNames.add("retrofit.utils");

        xlogMethods.add(new XLogMethod(XLog.class, ""));
        xlogMethods.add(new XLogMethod(MethodToLog.class, null));
        xlogMethods.add(new XLogMethod("com.promegu.other.class1", ""));
        xlogMethods.add(new XLogMethod("com.promegu.other.class2", ""));
        xlogMethods.add(new XLogMethod(Object.class, "getClass"));
        xlogMethods.add(new XLogMethod("java.lang.test.Object", ""));
        xlogMethods.add(new XLogMethod("retrofit", ""));
        xlogMethods.add(new XLogMethod("retrofit.utils", ""));
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
    public void testGetPkgPrefixesForCoarseMatch4() {
        // null or empty XLogMethods --> empty list
        assertThat(XLogUtils.getPkgPrefixesForCoarseMatchXLogMethods(new ArrayList<XLogMethod>(), 1).size(), is(0));
    }

    @Test
    public void testGetPkgPrefixesForCoarseMatch5() {
        // pkgSectionSize <= 0 --> result = classNames
        assertThat(XLogUtils.getPkgPrefixesForCoarseMatchXLogMethods(xlogMethods, -1).size(), is(xlogMethods.size()));
        assertThat(XLogUtils.getPkgPrefixesForCoarseMatchXLogMethods(xlogMethods, 0).size(), is(xlogMethods.size()));
    }

    @Test
    public void testGetPkgPrefixesForCoarseMatch6() {
        List<String> expected = new ArrayList<>();
        expected.add("com.promegu");
        expected.add("java.lang");
        expected.add("retrofit");
        assertThat(XLogUtils.getPkgPrefixesForCoarseMatchXLogMethods(xlogMethods, 2), is(expected));

        expected.clear();
        expected.add("com.promegu.xlog");
        expected.add("com.promegu.other");
        expected.add("java.lang.Object");
        expected.add("java.lang.test");
        expected.add("retrofit");
        assertThat(XLogUtils.getPkgPrefixesForCoarseMatchXLogMethods(xlogMethods, 3), is(expected));
    }

    @Test
    public void testShouldLogMember(){
        assertThat(XLogUtils.shouldLogMember(null, getMember(XLogMethod.class, "get")), is(false));
        assertThat(XLogUtils.shouldLogMember(xlogMethods, null), is(false));
        assertThat(XLogUtils.shouldLogMember(xlogMethods, getMember(MethodToLog.class, "get")), is(true));
        assertThat(XLogUtils.shouldLogMember(xlogMethods, getMember(Object.class, "toString")), is(false));
        assertThat(XLogUtils.shouldLogMember(xlogMethods, getMember(Object.class, "getClass")), is(true));
    }

    @Test
    public void testGetRemainingClassNames(){
        Set<Class<?>> classes = new HashSet<>();
        classes.add(XLog.class);
        classes.add(MethodToLog.class);
        classes.add(Object.class);

        Set<String> 

        assertThat(XLogUtils.getRemainingClassNames(null, getMember(XLogMethod.class, "get")),
                is(false));

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

    private Member getMember(final Class clazz, final String name){
        return new Member() {
            @Override
            public Class<?> getDeclaringClass() {
                return clazz;
            }

            @Override
            public String getName() {
                return name;
            }

            @Override
            public int getModifiers() {
                return 0;
            }

            @Override
            public boolean isSynthetic() {
                return false;
            }
        };
    }


}
