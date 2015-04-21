package com.promegu.xlog.xposedmodule;

import android.text.TextUtils;
import com.github.promeg.xlog_android.lib.XLogConfig;
import com.github.promeg.xlog_android.lib.XLogInitializer;
import com.google.gson.Gson;
import de.robv.android.xposed.XSharedPreferences;

/**
 * Created by guyacong on 2015/4/21.
 */
class XLogConfigUtils {
    public static XLogInitializer getXLogConfig(String pkgName, String prefName){
        XSharedPreferences xSharedPreferences = new XSharedPreferences(pkgName, prefName);

        String configStr = xSharedPreferences.getString(XLogConfig.PREF_CONFIG, null);
        if(TextUtils.isEmpty(configStr)){
            return new XLogInitializer(null, XLogConfig.NONE);
        }
        return new Gson().fromJson(configStr, XLogInitializer.class);
    }
}
