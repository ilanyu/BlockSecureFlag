package com.lanyus.blocksecureflag.filter;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.Filter;

import com.lanyus.blocksecureflag.adapter.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 过滤器
 * 按照app名过滤
 * Created by ilanyu on 2016/10/2.
 */

public class RecyclerViewFilter extends Filter {

    private List<PackageInfo> packageInfos;
    private List<PackageInfo> filteredPackageInfos;
    private RecyclerViewAdapter adapter;
    private PackageManager pckMan;

    public RecyclerViewFilter(PackageManager pckMan, List<PackageInfo> packageInfos, RecyclerViewAdapter adapter) {
        this.pckMan = pckMan;
        this.packageInfos = packageInfos;
        this.adapter = adapter;
        this.filteredPackageInfos = new ArrayList<>();
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        filteredPackageInfos.clear();
        FilterResults results = new FilterResults();
        String filter = constraint.toString().toLowerCase().trim();
        for (PackageInfo packageInfo : packageInfos) {
            if (packageInfo.applicationInfo.loadLabel(pckMan).toString().toLowerCase().trim().contains(filter)
                    || packageInfo.applicationInfo.packageName.toLowerCase().trim().contains(filter)) {
                filteredPackageInfos.add(packageInfo);
            }
        }
        results.values = filteredPackageInfos;
        results.count = filteredPackageInfos.size();
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.setList(filteredPackageInfos);
        adapter.notifyDataSetChanged();
    }
}
