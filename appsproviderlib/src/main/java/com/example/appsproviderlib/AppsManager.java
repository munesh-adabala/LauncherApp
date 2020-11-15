package com.example.appsproviderlib;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.appsproviderlib.Utils.Constants;
import com.example.appsproviderlib.data.AppInfo;
import com.example.appsproviderlib.runnables.AppUnInstalledTask;
import com.example.appsproviderlib.runnables.AppsFetcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppsManager {

    private static AppsManager appsManager;
    private MutableLiveData<List<AppInfo>> listOfApps;
    private static final String TAG = "AppsManager";
    private ExecutorService executorService;


    public static AppsManager getInstance() {
        if (appsManager == null) {
            synchronized (AppsManager.class) {
                if (appsManager == null) {
                    appsManager = new AppsManager(AppsSDK.getContext());
                }
            }
        }
        return appsManager;
    }

    private BroadcastReceiver pkgNotificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, "onReceive: Intent--" + intent.toString());
            if (intent.getAction() != null) {
                if (intent.getAction().equalsIgnoreCase(Constants.APP_INSTALLED_ACTION) && intent.getData() != null) {
                    appInstalled(intent.getData().toString().replace("package:", "").trim());
                } else if (intent.getAction().equalsIgnoreCase(Constants.APP_UNINSTALLED_ACTION) && intent.getData() != null) {
                    executorService.execute(new AppUnInstalledTask(intent.getData().toString().replace("package:", "").trim(), listOfApps));
                }
            }
        }
    };

    private AppsManager(Context context) {
        Log.e(TAG, "AppsManager: Creating Apps Manager");
        executorService = Executors.newSingleThreadExecutor();
        listOfApps = new MutableLiveData<>();
        executorService.execute(new AppsFetcher(context, listOfApps));
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addDataScheme(Constants.APP_DATA_SCHEME);
        context.registerReceiver(pkgNotificationReceiver, intentFilter);
    }

    /**
     * API fetches App info when new app gets installed and intimates through the Live Data
     *
     * @param packageName
     */
    private void appInstalled(String packageName) {
        PackageManager packageManager = AppsSDK.getContext().getPackageManager();
        AppInfo appInfo = null;
        try {
            ApplicationInfo info = packageManager.getApplicationInfo(packageName, 0);
            String appName = String.valueOf(packageManager.getApplicationLabel(info));
            Drawable iconDrawable = info.loadIcon(packageManager);
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            appInfo = new AppInfo(appName, packageName, iconDrawable, packageName, packageInfo.versionCode, packageInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (listOfApps.getValue() != null && appInfo != null) {
            listOfApps.getValue().add(appInfo);
            Collections.sort(listOfApps.getValue());
        }

    }

    public LiveData<List<AppInfo>> getInstalledApps() {
        return listOfApps;
    }

    /**
     * Helps in searching the Apps using provided text
     *
     * @param text
     * @return
     */
    public ArrayList<AppInfo> searchApps(String text) {
        ArrayList<AppInfo> searchList = new ArrayList<>();
        if (listOfApps.getValue() != null) {
            for (AppInfo info : listOfApps.getValue()) {
                if (info.getAppName().toLowerCase().contains(text.toLowerCase())) {
                    searchList.add(info);
                }
            }
        }
        return searchList;
    }

    /**
     * Cleans up all instances and other temporally stored data
     */
    protected void clean() {
        if (AppsSDK.getContext() != null) {
            AppsSDK.getContext().unregisterReceiver(pkgNotificationReceiver);
        }
        executorService.shutdownNow();
        if (listOfApps.getValue() != null)
            listOfApps.getValue().clear();
        appsManager = null;
    }
}
