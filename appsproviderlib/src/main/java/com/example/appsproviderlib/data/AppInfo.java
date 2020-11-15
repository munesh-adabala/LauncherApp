package com.example.appsproviderlib.data;

import android.graphics.drawable.Drawable;

public class AppInfo implements Comparable<AppInfo> {
    private String appName;
    private String packageName;
    private Drawable iconDrawable;
    private String mainActivity;
    private int versionCode;
    private String versionName;

    public AppInfo(String appName, String packageName, Drawable iconDrawable, String mainActivity, int versionCode, String versionName) {
        this.appName = appName;
        this.packageName = packageName;
        this.iconDrawable = iconDrawable;
        this.mainActivity = mainActivity;
        this.versionCode = versionCode;
        this.versionName = versionName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Drawable getIconDrawable() {
        return iconDrawable;
    }

    public void setIconDrawable(Drawable iconDrawable) {
        this.iconDrawable = iconDrawable;
    }

    public String getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(String mainActivity) {
        this.mainActivity = mainActivity;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "appName='" + appName + '\'' +
                ", packageName='" + packageName + '\'' +
                ", iconResId=" + iconDrawable +
                ", mainActivity='" + mainActivity + '\'' +
                ", versionCode=" + versionCode +
                ", versionName='" + versionName + '\'' +
                '}';
    }

    @Override
    public int compareTo(AppInfo o) {
        return appName.toLowerCase().compareTo(o.appName.toLowerCase());
    }
}
