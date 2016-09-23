package com.lanyus.blocksecureflag.bean;

/**
 * Created by ilanyu on 2016/9/22.
 */

public class CheckedApp{
    private boolean check;
    private String appName;
    private String appPackageName;

    public CheckedApp() {
    }

    public CheckedApp(String appPackageName, String appName, boolean check) {
        this.appPackageName = appPackageName;
        this.appName = appName;
        this.check = check;
    }

    public CheckedApp(String appPackageName) {
        this.appPackageName = appPackageName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CheckedApp that = (CheckedApp) o;

        return appPackageName != null ? appPackageName.equals(that.appPackageName) : that.appPackageName == null;

    }

    @Override
    public int hashCode() {
        return appPackageName != null ? appPackageName.hashCode() : 0;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppPackageName() {
        return appPackageName;
    }

    public void setAppPackageName(String appPackageName) {
        this.appPackageName = appPackageName;
    }
}
