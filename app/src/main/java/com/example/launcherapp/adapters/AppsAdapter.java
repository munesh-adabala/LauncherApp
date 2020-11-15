package com.example.launcherapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appsproviderlib.data.AppInfo;
import com.example.launcherapp.R;
import com.example.launcherapp.utils.MEventsManager;

import java.util.List;

public class AppsAdapter extends RecyclerView.Adapter<AppsAdapter.AppVH> {

    private List<AppInfo> appsData;
    private Context context;

    public AppsAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public AppVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.app_icon_layout, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return new AppVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppVH holder, int position) {
        AppInfo appInfo = appsData.get(position);
        holder.textView.setText(appInfo.getAppName());
        holder.imageView.setImageDrawable(appInfo.getIconDrawable());
        holder.packageName = appInfo.getPackageName();
    }

    @Override
    public int getItemCount() {
        return appsData != null ? appsData.size() : 0;
    }

    public void setData(List<AppInfo> appInfos) {
        this.appsData = appInfos;
        notifyDataSetChanged();
    }

    static class AppVH extends RecyclerView.ViewHolder {
        private TextView textView;
        private ImageView imageView;
        private String packageName;

        public AppVH(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.app_name);
            imageView = itemView.findViewById(R.id.image_view);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MEventsManager.getInstance().inject(MEventsManager.LAUNCH_APP, packageName);
                }
            });
        }

    }

}
