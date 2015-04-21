package com.promegu.xlog.xposedmodule;

import android.content.pm.ApplicationInfo;
import android.text.TextUtils;
import com.github.promeg.xlog_android.lib.XLogConfig;
import com.github.promeg.xlog_android.lib.XLogInitializer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.promegu.xlog.base.MethodToLog;
import com.promegu.xlog.base.XLogUtils;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import org.jf.dexlib2.DexFileFactory;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.iface.ClassDef;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static de.robv.android.xposed.XposedHelpers.findAndHookConstructor;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**
 * Created by guyacong on 2015/3/9.
 */
public class XLogModule implements IXposedHookLoadPackage {
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        XposedBridge.log("Loaded app: " + lpparam.packageName);
        XLogInitializer xLogInitializer = XLogConfigUtils.getXLogConfig(lpparam.packageName, XLogConfig.XLOG_SharedPreferences);

        XposedBridge.log("xLogInitializer: " + xLogInitializer);

        Set<Class> classes = getXLogClasses(XLogUtils.PKG_NAME, lpparam);

        for (Class xlogClass : classes) {

            String methodsStr = (String) XposedHelpers.findField(xlogClass, XLogUtils.FIELD_NAME).get(null);

            XposedBridge.log("methodsStr: " + methodsStr);

            List<MethodToLog> methodToLogs = new Gson().fromJson(methodsStr, new TypeToken<List<MethodToLog>>() {
            }.getType());

            for (MethodToLog methodToLog : methodToLogs) {
                Class declaringClass = XposedHelpers.findClass(methodToLog.getPkg(), lpparam.classLoader);
                int parameterLength = methodToLog.getParameterClasses() == null ? 0 : methodToLog.getParameterClasses().size();
                Object[] classCustomTypes = new Object[parameterLength + 1];
                for (int i = 0; i < parameterLength; i++) {
                    classCustomTypes[i] = XposedHelpers.findClass(methodToLog.getParameterClasses().get(i), lpparam.classLoader);
                }
                classCustomTypes[parameterLength] = new XLogMethodHook(methodToLog, declaringClass);

                switch (methodToLog.getType()) {
                    case XLogUtils.TYPE_METHOD:
                        findAndHookMethod(methodToLog.getPkg(), lpparam.classLoader, methodToLog.getName(), classCustomTypes);
                        break;
                    case XLogUtils.TYPE_CONSTRUCTOR:
                        findAndHookConstructor(methodToLog.getPkg(), lpparam.classLoader, classCustomTypes);
                        break;
                }
            }
        }
    }

    private Set<Class> getXLogClasses(String xLogPkgName, final XC_LoadPackage.LoadPackageParam lpparam) {
        ApplicationInfo appInfo = lpparam.appInfo;

        int targetSdkVersion = appInfo.targetSdkVersion;
        String apkPath = appInfo.sourceDir;
        Set<Class> classes = new HashSet<>();

        File apkFile = new File(apkPath);
        if (apkFile.exists()) {
            DexBackedDexFile dexFile = null;
            try {
                dexFile = DexFileFactory.loadDexFile(apkFile.getAbsolutePath(), targetSdkVersion);
            } catch (IOException ex) {
                return null;
            }
            Set<? extends ClassDef> classDefs = dexFile.getClasses();
            for (ClassDef cd : classDefs) {
                String[] pkgClass = getPackageClassName(cd.getType());

                if (xLogPkgName.equals(pkgClass[0]) && pkgClass[1] != null && pkgClass[1].startsWith(XLogUtils.CLASS_NAME)) {
                    XposedBridge.log("ClassDef:" + pkgClass[0] + "  " + pkgClass[1]);
                    Class xlogClass = XposedHelpers.findClass(XLogUtils.PKG_NAME + "." + pkgClass[1], lpparam.classLoader);
                    classes.add(xlogClass);
                }
            }


        }
        return classes;
    }


    public static String[] getPackageClassName(String classDescriptor) {
        String[] packageClassName = new String[2];
        String trimedClassDescriptor = classDescriptor.substring(1, classDescriptor.length() - 1);
        String[] stringItems = trimedClassDescriptor.split("/");

        ArrayList<String> listItems = new ArrayList<String>();
        int i = 0;
        for (i = 0; i < stringItems.length - 1; i++) {
            listItems.add(stringItems[i]);
        }
        String packageName = TextUtils.join(".", listItems);
        String className = stringItems[i];

        packageClassName[0] = packageName;
        packageClassName[1] = className;
        return packageClassName;
    }


}
