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

    static final List<String> CLASS_NAMES = new ArrayList<String>();

    static final List<XLogMethod> XLOG_METHODS = new ArrayList<XLogMethod>();

    static {
        CLASS_NAMES.add("com.promegu.xlog.base.XLog");
        CLASS_NAMES.add("com.promegu.xlog.base.MethodToLog");
        CLASS_NAMES.add("com.promegu.other.class1");
        CLASS_NAMES.add("com.promegu.other.class2");
        CLASS_NAMES.add("java.lang.Object");
        CLASS_NAMES.add("java.lang.test.Object");
        CLASS_NAMES.add("retrofit");
        CLASS_NAMES.add("retrofit.utils");

        XLOG_METHODS.add(new XLogMethod(XLog.class, ""));
        XLOG_METHODS.add(new XLogMethod(MethodToLog.class, null));
        XLOG_METHODS.add(new XLogMethod("com.promegu.other.class1", ""));
        XLOG_METHODS.add(new XLogMethod("com.promegu.other.class2", ""));
        XLOG_METHODS.add(new XLogMethod(Object.class, "getClass"));
        XLOG_METHODS.add(new XLogMethod("java.lang.test.Object", ""));
        XLOG_METHODS.add(new XLogMethod("retrofit", ""));
        XLOG_METHODS.add(new XLogMethod("retrofit.utils", ""));
    }

    @Test
    public void testFilterResult1() {
        Set<MethodToLog> methodToLogs = new Gson()
                .fromJson(MethodToLogTest.METHODS_TO_LOG, new TypeToken<Set<MethodToLog>>() {
                }.getType());

        List<String> xlogClassNames = new ArrayList<String>();
        for (MethodToLog methodToLog : methodToLogs) {
            xlogClassNames.add(methodToLog.getClassName());
        }

        XLogSetting xLogSetting = new XLogSetting(methodToLogs, null,
                XLogUtils.getPkgPrefixesForCoarseMatch(xlogClassNames, 2), xlogClassNames);

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
        // null or empty CLASS_NAMES --> empty list
        assertThat(XLogUtils.getPkgPrefixesForCoarseMatch(new ArrayList<String>(), 1).size(),
                is(0));
    }

    @Test
    public void testGetPkgPrefixesForCoarseMatch2() {
        // pkgSectionSize <= 0 --> result = CLASS_NAMES
        assertThat(XLogUtils.getPkgPrefixesForCoarseMatch(CLASS_NAMES, -1).size(),
                is(CLASS_NAMES.size()));
        assertThat(XLogUtils.getPkgPrefixesForCoarseMatch(CLASS_NAMES, 0).size(),
                is(CLASS_NAMES.size()));
    }

    @Test
    public void testGetPkgPrefixesForCoarseMatch3() {
        List<String> expected = new ArrayList<String>();
        expected.add("com.promegu");
        expected.add("java.lang");
        expected.add("retrofit");
        assertThat(XLogUtils.getPkgPrefixesForCoarseMatch(CLASS_NAMES, 2), is(expected));

        expected.clear();
        expected.add("com.promegu.xlog");
        expected.add("com.promegu.other");
        expected.add("java.lang.Object");
        expected.add("java.lang.test");
        expected.add("retrofit");
        //CHECKSTYLE:OFF
        assertThat(XLogUtils.getPkgPrefixesForCoarseMatch(CLASS_NAMES, 3), is(expected));
        //CHECKSTYLE:ON
    }

    @Test
    public void testGetPkgPrefixesForCoarseMatch4() {
        // null or empty XLogMethods --> empty list
        assertThat(XLogUtils.getPkgPrefixesForCoarseMatchXLogMethods(null, 1).size(), is(0));
        assertThat(XLogUtils.getPkgPrefixesForCoarseMatchXLogMethods(new ArrayList<XLogMethod>(), 1)
                .size(), is(0));
    }

    @Test
    public void testGetPkgPrefixesForCoarseMatch5() {
        // pkgSectionSize <= 0 --> result = CLASS_NAMES
        assertThat(XLogUtils.getPkgPrefixesForCoarseMatchXLogMethods(XLOG_METHODS, -1).size(),
                is(XLOG_METHODS.size()));
        assertThat(XLogUtils.getPkgPrefixesForCoarseMatchXLogMethods(XLOG_METHODS, 0).size(),
                is(XLOG_METHODS.size()));
    }

    @Test
    public void testGetPkgPrefixesForCoarseMatch6() {
        List<String> expected = new ArrayList<String>();
        expected.add("com.promegu");
        expected.add("java.lang");
        expected.add("retrofit");
        assertThat(XLogUtils.getPkgPrefixesForCoarseMatchXLogMethods(XLOG_METHODS, 2), is(expected));

        expected.clear();
        expected.add("com.promegu.xlog");
        expected.add("com.promegu.other");
        expected.add("java.lang.Object");
        expected.add("java.lang.test");
        expected.add("retrofit");
        //CHECKSTYLE:OFF
        assertThat(XLogUtils.getPkgPrefixesForCoarseMatchXLogMethods(XLOG_METHODS, 3), is(expected));
        //CHECKSTYLE:ON
    }

    @Test
    public void testShouldLogMember() {
        assertThat(XLogUtils.shouldLogMember(null, getMember(XLogMethod.class, "get")), is(false));
        assertThat(XLogUtils.shouldLogMember(XLOG_METHODS, null), is(false));
        assertThat(XLogUtils.shouldLogMember(XLOG_METHODS, getMember(MethodToLog.class, "get")),
                is(true));
        assertThat(XLogUtils.shouldLogMember(XLOG_METHODS, getMember(Object.class, "toString")),
                is(false));
        assertThat(XLogUtils.shouldLogMember(XLOG_METHODS, getMember(Object.class, "getClass")),
                is(true));
    }

    @Test
    public void testGetRemainingClassNames() {
        Set<Class<?>> classes = new HashSet<Class<?>>();
        classes.add(XLog.class);
        classes.add(MethodToLog.class);
        classes.add(Object.class);

        Set<String> expected = new HashSet<String>();
        expected.add("com.promegu.other.class1");
        expected.add("com.promegu.other.class2");
        expected.add("java.lang.test.Object");
        expected.add("retrofit");
        expected.add("retrofit.utils");

        assertThat(
                XLogUtils.getRemainingClassNames(classes, new HashSet<String>(CLASS_NAMES)),
                is(expected));

    }

    @Test
    public void testGetClassNameSectionSize() {
        assertThat(XLogUtils.getClassNameSectionSize(""), is(0));
        assertThat(XLogUtils.getClassNameSectionSize(null), is(0));
        assertThat(XLogUtils.getClassNameSectionSize("com"), is(1));
        assertThat(XLogUtils.getClassNameSectionSize("com.promegu"), is(2));
        //CHECKSTYLE:OFF
        assertThat(XLogUtils.getClassNameSectionSize("com.promegu.xlog.base"), is(4));
        //CHECKSTYLE:ON
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
        //CHECKSTYLE:OFF
        assertThat(XLogUtils.getClassNameSection("com.promegu.xlog.base", 4),
                is("com.promegu.xlog.base"));
        //CHECKSTYLE:ON
    }

    private Member getMember(final Class clazz, final String name) {
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
