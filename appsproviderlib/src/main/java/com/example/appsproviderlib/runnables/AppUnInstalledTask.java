package com.example.appsproviderlib.runnables;

import androidx.lifecycle.MutableLiveData;

import com.example.appsproviderlib.AppsManager;
import com.example.appsproviderlib.data.AppInfo;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Helps in Removing the uninstalled from the list and intimates through LiveData.
 */
public class AppUnInstalledTask implements Runnable {
    private String packageName;
    private WeakReference<MutableLiveData<List<AppInfo>>> appslistRef;

    public AppUnInstalledTask(String packageName, MutableLiveData<List<AppInfo>> appsLiveData) {
        this.packageName = packageName;
        appslistRef = new WeakReference<>(appsLiveData);
    }

    @Override
    public void run() {
        ArrayList<AppInfo> newList = new ArrayList<>();
        List<AppInfo> oldList = AppsManager.getInstance().getInstalledApps().getValue();
        if (oldList != null) {
            for (AppInfo appInfo : oldList) {
                if (!appInfo.getPackageName().equalsIgnoreCase(packageName)) {
                    newList.add(appInfo);
                }
            }
        }
        if (appslistRef.get() != null) {
            appslistRef.get().postValue(newList);
        }
    }
}
