package com.example.appsproviderlib;

import android.content.Context;

/**
 * Library initialization
 */
public class AppsSDK {
    private static Context mcontext;

    public static void initialize(Context context) {
        mcontext = context;
        AppsManager.getInstance();
    }

    public static Context getContext() {
        return mcontext;
    }

    /**
     * Clears all the SDK related instances. Call this only when you don't require any support from thi SDK.
     * NOTE: If you call this Inorder use AppsSDK you need initialize again.
     */
    public static void clean() {
        AppsManager.getInstance().clean();
        mcontext = null;
    }
}
