package com.example.appsproviderlib.runnables;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.appsproviderlib.data.AppInfo;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Runnable helps in fetching all the Apps info that are installed in the System.
 */
public class AppsFetcher implements Runnable {

    private static final String TAG = "AppsFetcher";

    private WeakReference<MutableLiveData<List<AppInfo>>> appslistRef;
    private WeakReference<Context> contextRef;

    public AppsFetcher(Context context, MutableLiveData<List<AppInfo>> appsList) {
        appslistRef = new WeakReference<>(appsList);
        contextRef = new WeakReference<>(context);
    }

    @Override
    public void run() {
        fetchApps();
    }

    private void fetchApps() {
        Log.e(TAG, "fetchApps: Fetching apps");
        Context context = contextRef.get();
        ArrayList<AppInfo> listOfApps = new ArrayList<>();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> appsList = packageManager.queryIntentActivities(intent, PackageManager.MATCH_ALL);
        for (ResolveInfo info : appsList) {
            ApplicationInfo applicationInfo = info.activityInfo.applicationInfo;
            AppInfo app = null;
            try {
                PackageInfo packageInfo = packageManager.getPackageInfo(info.activityInfo.packageName, 0);
                app = new AppInfo(packageManager.getApplicationLabel(applicationInfo).toString(), info.activityInfo.packageName,
                        info.loadIcon(packageManager), info.activityInfo.parentActivityName, packageInfo.versionCode, packageInfo.versionName);
            } catch (PackageManager.NameNotFoundException e) {
                Log.e(TAG, "fetchApps: Exception while getting package info" + e.getMessage());
            }
            listOfApps.add(app);
        }
        Collections.sort(listOfApps);
        if (appslistRef.get() != null) {
            appslistRef.get().postValue(listOfApps);
        }
    }
}
