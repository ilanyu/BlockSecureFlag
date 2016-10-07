package com.lanyus.blocksecureflag;

import android.os.Build;
import android.view.WindowManager;

import com.alibaba.fastjson.JSON;
import com.lanyus.blocksecureflag.bean.CheckedApp;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Xposed Main
 * Created by ilanyu on 2016/9/22.
 */

public class XposedMain implements IXposedHookLoadPackage {

    private List<CheckedApp> checkedApps = new ArrayList<>();

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {

        XSharedPreferences preferences = new XSharedPreferences("com.lanyus.blocksecureflag", "checkedApp");
        preferences.makeWorldReadable();
        preferences.reload();
        checkedApps = JSON.parseArray(preferences.getString("checkedApps", JSON.toJSONString(checkedApps)), CheckedApp.class);

        if (lpparam.packageName.equals("com.lanyus.blocksecureflag") || !checkedApps.contains(new CheckedApp(lpparam.packageName))) {
            return;
        }
        XposedBridge.log("com.lanyus.blocksecureflag Loaded app: " + lpparam.packageName);

        XposedHelpers.findAndHookMethod("android.view.Window", lpparam.classLoader, "setFlags", int.class, int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Integer flags = (Integer) param.args[0];
                flags &= ~WindowManager.LayoutParams.FLAG_SECURE;
                param.args[0] = flags;
            }
        });
        if (Build.VERSION.SDK_INT >= 17) {
            XposedHelpers.findAndHookMethod("android.view.SurfaceView", lpparam.classLoader, "setSecure", boolean.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    param.args[0] = false;
                }
            });
        }
    }
}
