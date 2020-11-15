package com.example.launcherapp.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.appsproviderlib.AppsManager;
import com.example.appsproviderlib.AppsSDK;

import java.util.List;

public class AppsVM extends ViewModel {
    private static final String TAG = "AppsVM";

    public LiveData<List<com.example.appsproviderlib.data.AppInfo>> getAppsLiveData() {
        return AppsManager.getInstance().getInstalledApps();
    }

    /**
     * Once the instance that is using this ViewModel gets destroyed onCleared() will
     * be called.
     */
    @Override
    protected void onCleared() {
        Log.e(TAG, "onCleared: ");
        AppsSDK.clean();
        super.onCleared();
    }
}
