package com.lanyus.blocksecureflag;

import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.lanyus.blocksecureflag.bean.CheckedApp;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<CheckedApp> checkedApps = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        SharedPreferences sharedPreferences = getSharedPreferences("checkedApp", MODE_WORLD_READABLE + MODE_WORLD_WRITEABLE);
        checkedApps = JSON.parseArray(sharedPreferences.getString("checkedApps", JSON.toJSONString(checkedApps)), CheckedApp.class);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new RecycleViewDivider(MainActivity.this, LinearLayoutManager.HORIZONTAL));
        recyclerView.setAdapter(new RecyclerViewAdapter());
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (save()) {
                    Toast.makeText(MainActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean save() {
        SharedPreferences sharedPreferences = getSharedPreferences("checkedApp", MODE_WORLD_READABLE + MODE_WORLD_WRITEABLE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("checkedApps", JSON.toJSONString(checkedApps));
        return editor.commit();
    }

    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewViewHolder> {

        private PackageManager pckMan;
        private List<PackageInfo> packs;

        RecyclerViewAdapter() {
            pckMan = getPackageManager();
            packs = pckMan.getInstalledPackages(0);
        }

        @Override
        public RecyclerViewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new RecyclerViewViewHolder(LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main_lv_item, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerViewViewHolder holder, int position) {
            String appName = packs.get(position).applicationInfo.loadLabel(pckMan).toString(); //获取App名字
            String appPackageName = packs.get(position).applicationInfo.packageName; //获取App包名
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

        @Override
        public int getItemCount() {
            return packs.size();
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
}
