package com.github.promeg.xlog_android.lib;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.promegu.xlog.base.MethodToLog;
import com.promegu.xlog.base.XLogUtils;
import com.taobao.android.dexposed.ClassUtils;
import com.taobao.android.dexposed.XposedHelpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dalvik.system.DexFile;

/**
 * Created by xudshen@hotmail.com on 14/11/13.
 */
public class MultiDexHelper {

    private static final String TAG = "MultiDexHelper";

    private static final String EXTRACTED_NAME_EXT = ".classes";

    private static final String EXTRACTED_SUFFIX = ".zip";

    private static final String SECONDARY_FOLDER_NAME = "code_cache" + File.separator +
            "secondary-dexes";

    private static final String PREFS_FILE = "multidex.version";

    private static final String KEY_DEX_NUMBER = "dex.number";

    private static SharedPreferences getMultiDexPreferences(Context context) {
        return context.getSharedPreferences(PREFS_FILE,
                Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB
                        ? Context.MODE_PRIVATE
                        : Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);
    }

    /**
     * get all the dex path
     *
     * @param context the application context
     * @return all the dex path
     */
    public static List<String> getSourcePaths(Context context)
            throws PackageManager.NameNotFoundException, IOException {
        ApplicationInfo applicationInfo = context.getPackageManager()
                .getApplicationInfo(context.getPackageName(), 0);
        File sourceApk = new File(applicationInfo.sourceDir);
        File dexDir = new File(applicationInfo.dataDir, SECONDARY_FOLDER_NAME);

        List<String> sourcePaths = new ArrayList<String>();
        sourcePaths.add(applicationInfo.sourceDir); //add the default apk path

        //the prefix of extracted file, ie: test.classes
        String extractedFilePrefix = sourceApk.getName() + EXTRACTED_NAME_EXT;
        //the total dex numbers
        int totalDexNumber = getMultiDexPreferences(context).getInt(KEY_DEX_NUMBER, 1);

        for (int secondaryNumber = 2; secondaryNumber <= totalDexNumber; secondaryNumber++) {
            //for each dex file, ie: test.classes2.zip, test.classes3.zip...
            String fileName = extractedFilePrefix + secondaryNumber + EXTRACTED_SUFFIX;
            File extractedFile = new File(dexDir, fileName);
            if (extractedFile.isFile()) {
                sourcePaths.add(extractedFile.getAbsolutePath());
                //we ignore the verify zip part
            } else {
                throw new IOException("Missing extracted secondary dex file '" +
                        extractedFile.getPath() + "'");
            }
        }

        return sourcePaths;
    }

    /**
     * get all the classes name in "classes.dex", "classes2.dex", ....
     *
     * @param context the application context
     * @return all the classes
     */
    public static List<Class<?>> getAllClasses(Context context)
            throws PackageManager.NameNotFoundException, IOException {
        List<Class<?>> classNames = new ArrayList<Class<?>>();
        List<String> sourcePaths = getSourcePaths(context);
        for (String path : sourcePaths) {
            try {
                DexFile dexfile = null;
                if (path.endsWith(EXTRACTED_SUFFIX)) {
                    //NOT use new DexFile(path), because it will throw "permission error in /data/dalvik-cache"
                    dexfile = DexFile.loadDex(path, path + ".tmp", 0);
                } else {
                    dexfile = new DexFile(path);
                }
                Enumeration<String> dexEntries = dexfile.entries();
                while (dexEntries.hasMoreElements()) {
                    try {
                        classNames.add(ClassUtils.getClass(dexEntries.nextElement()));
                    } catch (ClassNotFoundException e) {
                        Log.d(TAG, "class not found: " + dexEntries.nextElement());

                    }
                }
            } catch (IOException e) {
                throw new IOException("Error at loading dex file '" +
                        path + "'");
            }
        }
        return classNames;
    }

    public static List<String> getAllClassNames(Context context)
            throws PackageManager.NameNotFoundException, IOException {
        List<String> classNames = new ArrayList<String>();
        List<String> sourcePaths = getSourcePaths(context);
        for (String path : sourcePaths) {
            try {
                DexFile dexfile = null;
                if (path.endsWith(EXTRACTED_SUFFIX)) {
                    //NOT use new DexFile(path), because it will throw "permission error in /data/dalvik-cache"
                    dexfile = DexFile.loadDex(path, path + ".tmp", 0);
                } else {
                    dexfile = new DexFile(path);
                }
                Enumeration<String> dexEntries = dexfile.entries();
                while (dexEntries.hasMoreElements()) {
                    classNames.add(dexEntries.nextElement());
                }
            } catch (IOException e) {
                throw new IOException("Error at loading dex file '" +
                        path + "'");
            }
        }
        return classNames;
    }


    public static Set<MethodToLog> getAllMethodToLog(Context context,
            String xLogPkgName) {

        List<String> allClassNames = null;
        try {
            allClassNames = getAllClassNames(context);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(allClassNames == null){
            return null;
        }

        Set<MethodToLog> methodToLogs = new HashSet<MethodToLog>();
        for (String className : allClassNames) {
            if (className != null && className
                    .startsWith(xLogPkgName + "." + XLogUtils.CLASS_NAME)) {

                String methodsStr = null;
                try {
                    Class xlogClass = ClassUtils.getClass(className);
                    methodsStr = (String) XposedHelpers
                            .findField(xlogClass, XLogUtils.FIELD_NAME).get(null);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    Log.d(TAG, "still not found: " + className);
                }

                methodToLogs.addAll(new Gson().<List<MethodToLog>>fromJson(methodsStr,
                        new TypeToken<List<MethodToLog>>() {
                        }.getType()));
            }
        }
        return methodToLogs;
    }

    public static Set<Member> getAllMethodsWithAnnoation(Context context,
            Class<? extends Annotation> annoationClass) {
        try {
            List<Class<?>> allClasses = getAllClasses(context);
            Set<Member> allMethodsWithAnnoation = new HashSet<Member>();
            for (Class entryClass : allClasses) {
                if (entryClass != null) {

                    try {
                        Method[] methods = entryClass.getDeclaredMethods();
                        for (Method method : methods) {
                            if (method.isAnnotationPresent(annoationClass)) {
                                allMethodsWithAnnoation.add(method);
                            }
                        }
                    } catch (Throwable t) {
                        //ignore
                    }

                    try {
                        Constructor[] constructors = entryClass.getDeclaredConstructors();
                        for (Constructor constructor : constructors) {
                            if (constructor.isAnnotationPresent(annoationClass)) {
                                allMethodsWithAnnoation.add(constructor);
                            }
                        }
                    } catch (Throwable t) {
                        //ignore
                    }
                }
            }

            return allMethodsWithAnnoation;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
