package com.example.launcherapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appsproviderlib.AppsManager;
import com.example.appsproviderlib.AppsSDK;
import com.example.appsproviderlib.data.AppInfo;
import com.example.launcherapp.adapters.AppsAdapter;
import com.example.launcherapp.utils.MEventsManager;
import com.example.launcherapp.viewmodels.AppsVM;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MEventsManager.EventNotifier {
    private static final String TAG = "MainActivity";
    private AppsAdapter appsAdapter;
    private AppsVM appsVM;

    private Observer<List<AppInfo>> appsObserver = new Observer<List<AppInfo>>() {
        @Override
        public void onChanged(List<AppInfo> appInfos) {
            updateRecyclerView(appInfos);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppsSDK.initialize(this);
        appsVM = new ViewModelProvider(this).get(AppsVM.class);
        RecyclerView appsRecyclerView = findViewById(R.id.appsRecycler);
        EditText searchEditText = findViewById(R.id.search_edit_text);
        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 4);
        appsRecyclerView.setLayoutManager(layoutManager);
        appsAdapter = new AppsAdapter(getApplicationContext());
        appsRecyclerView.setAdapter(appsAdapter);
        searchEditText.addTextChangedListener(textWatcher);
        appsVM.getAppsLiveData().observe(this, appsObserver);
    }


    private void updateRecyclerView(List<AppInfo> list) {
        appsAdapter.setData(list);
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            updateRecyclerView(AppsManager.getInstance().searchApps(String.valueOf(s)));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    protected void onStart() {
        MEventsManager.getInstance().addListener(MEventsManager.LAUNCH_APP, this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        MEventsManager.getInstance().removeListener(MEventsManager.LAUNCH_APP, this);
    }


    /**
     * Helps in launching the App using packageName
     *
     * @param packageName of the App that has to be launched
     */
    private void launchApp(String packageName) {
        try {
            Intent i = getPackageManager().getLaunchIntentForPackage(packageName);
            startActivity(i);
        } catch (Exception e) {
            Log.e(TAG, "onReceiveEvent: Exception handled ==" + e.getMessage());
        }
    }


    /**
     * Events Manager sends the event that are registered in this class.
     *
     * @param type   of the event this class interested in.
     * @param object data that is required to handle the event.
     */
    @Override
    public void onReceiveEvent(int type, Object object) {
        switch (type) {
            case MEventsManager.LAUNCH_APP:
                String packageName = (String) object;
                try {
                    launchApp(packageName);
                } catch (Exception e) {
                    Log.e(TAG, "onReceiveEvent: Exception while launching app" + e.getMessage());
                }
                break;
        }
    }
}