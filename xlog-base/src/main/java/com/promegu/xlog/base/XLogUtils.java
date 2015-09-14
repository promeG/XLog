package com.promegu.xlog.base;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by guyacong on 2015/3/9.
 */
public final class XLogUtils {

    private XLogUtils() {
        //no instance
    }

    public static final int TYPE_METHOD = 0;

    public static final int TYPE_CONSTRUCTOR = 1;

    public static final String PKG_NAME = "com.promegu.xlogger";

    public static final String CLASS_NAME = "XLoggerMethods";

    public static final String FIELD_NAME_METHODS = "METHODS_TO_LOG";

    public static final String FIELD_NAME_CLASSES = "CLASSES_TO_LOG";

    public static Set<String> getRemainingClassNames(Set<Class<?>> classes,
            Set<String> classNames) {
        if (classes == null || classes.size() == 0 || classNames == null) {
            return classNames;
        }
        Set<String> nameFromClasses = new HashSet<String>();
        for (Class clazz : classes) {
            try {
                if (clazz != null && clazz.getCanonicalName() != null) {
                    nameFromClasses.add(clazz.getCanonicalName().replaceAll("\\$", "."));
                }
            } catch (Throwable throwable) {
                // ignore
            }
        }

        nameFromClasses.retainAll(classNames);
        classNames.removeAll(nameFromClasses);

        return classNames;
    }

    public static boolean shouldLogMember(List<XLogMethod> xLogMethods, Member member) {
        if (xLogMethods == null || member == null) {
            return false;
        }
        for (XLogMethod xLogMethod : xLogMethods) {
            if (xLogMethod != null && xLogMethod.getClassName() != null
                    && xLogMethod.getClassName()
                    .equals(member.getDeclaringClass().getCanonicalName())) {
                // find XLogMethod
                if (xLogMethod.getMethodName() == null || xLogMethod.getMethodName().equals("")) {
                    return true;
                }
                if (xLogMethod.getMethodName().equals(member.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean filterResult(String className, XLogSetting xLogSetting) {
        if (className == null || className.length() == 0) {
            return false;
        }
        if (xLogSetting == null || xLogSetting.xlogClassPrefixes == null
                || xLogSetting.xlogClassPrefixes.size() == 0) {
            return true;
        }
        className = className.replaceAll("\\$", ".");

        boolean prefixFilter = false;
        for (String s : xLogSetting.xlogClassPrefixes) {
            if (className.startsWith(s)) {
                prefixFilter = true;
            }
        }
        if (!prefixFilter) {
            return false;
        }

        for (String s : xLogSetting.xlogClassNames) {
            if (className.startsWith(s)) {
                return true;
            }
        }
        return false;
    }

    public static List<String> getPkgPrefixesForCoarseMatch(List<String> classNames,
            int pkgSectionSize) {
        if (pkgSectionSize <= 0) {
            return classNames;
        }
        List<String> result = new ArrayList<String>();
        if (classNames != null) {
            for (String className : classNames) {
                if (getClassNameSectionSize(className) < pkgSectionSize) {
                    if (!result.contains(className)) {
                        result.add(className);
                    }
                } else {
                    String prefix = getClassNameSection(className, pkgSectionSize);
                    if (!result.contains(prefix)) {
                        result.add(prefix);
                    }
                }
            }
        }

        Set<String> names = new HashSet<String>(result);
        Iterator<String> iterator = result.iterator();
        while (iterator.hasNext()) {
            String className = iterator.next();
            for (String s : names) {
                if (className.startsWith(s) && !className.equals(s)) {
                    // there is a B in result list which is the prefix of A, should remove A
                    iterator.remove();
                }
            }
        }

        return result;
    }

    public static List<String> getPkgPrefixesForCoarseMatchXLogMethods(List<XLogMethod> xLogMethods,
            int pkgSectionSize) {
        if (xLogMethods == null || xLogMethods.size() == 0) {
            return new ArrayList<String>();
        }
        List<String> classNames = new ArrayList<String>();
        for (XLogMethod xLogMethod : xLogMethods) {
            if (xLogMethod != null && xLogMethod.getClassName() != null
                    && xLogMethod.getClassName().length() > 0) {
                classNames.add(xLogMethod.getClassName().replaceAll("\\$", "."));
            }
        }
        return getPkgPrefixesForCoarseMatch(classNames, pkgSectionSize);
    }

    static int getClassNameSectionSize(String className) {
        if (className == null || className.length() == 0) {
            return 0;
        }
        int count = 0;
        for (int i = 0; i < className.length(); i++) {
            if ('.' == className.charAt(i)) {
                count++;
            }
        }
        return count + 1;
    }

    static String getClassNameSection(String className, int section) {
        int sectionSize = getClassNameSectionSize(className);

        if (className == null || section <= 0 || section >= sectionSize) {
            return className;
        }

        int index = 0;
        while (section > 0) {
            index++;
            if ('.' == className.charAt(index)) {
                section--;
            }
        }

        return className.substring(0, index);
    }
}
