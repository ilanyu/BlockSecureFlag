package com.lanyus.blocksecureflag.adapter;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.lanyus.blocksecureflag.R;
import com.lanyus.blocksecureflag.bean.CheckedApp;
import com.lanyus.blocksecureflag.filter.RecyclerViewFilter;

import java.util.List;

/**
 * Created by ilanyu on 2016/10/2.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewViewHolder> {

    private PackageManager pckMan;
    private List<PackageInfo> packs;
    private List<CheckedApp> checkedApps;
    private List<PackageInfo> packsFilter;
    private RecyclerViewFilter recyclerViewFilter;

    public RecyclerViewAdapter(Context context, List<CheckedApp> checkedApps) {
        pckMan = context.getPackageManager();
        packs = pckMan.getInstalledPackages(0);
        this.checkedApps = checkedApps;
        this.packsFilter = packs;
        recyclerViewFilter = new RecyclerViewFilter(pckMan, packs, this);
    }

    @Override
    public RecyclerViewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyclerViewViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main_lv_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerViewViewHolder holder, int position) {
        String appName = packsFilter.get(position).applicationInfo.loadLabel(pckMan).toString(); //获取App名字
        String appPackageName = packsFilter.get(position).applicationInfo.packageName; //获取App包名
        final CheckedApp app = new CheckedApp(appPackageName, appName, holder.checkBox.isChecked());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    app.setCheck(true);
                    if (checkedApps.contains(app)) {
                        checkedApps.remove(app);
                    }
                    checkedApps.add(app);
                } else {
                    app.setCheck(false);
                    checkedApps.remove(app);
                }
            }
        });
        holder.appName.setText(appName);
        TextPaint tp = holder.appName.getPaint();
        tp.setFakeBoldText(true);
        holder.appPackageName.setText(appPackageName);
        if (checkedApps.contains(app)) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }
    }

    public void filterList(String filter) {
        recyclerViewFilter.filter(filter);
    }

    public void setList(List<PackageInfo> packs) {
        this.packsFilter = packs;
    }

    @Override
    public int getItemCount() {
        return packsFilter.size();
    }

    class RecyclerViewViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkBox;
        TextView appName;
        TextView appPackageName;

        RecyclerViewViewHolder(View itemView) {
            super(itemView);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);
            appName = (TextView) itemView.findViewById(R.id.appName);
            appPackageName = (TextView) itemView.findViewById(R.id.appPackageName);
        }
    }
}
