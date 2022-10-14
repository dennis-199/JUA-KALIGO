package com.example.jua_kaligo;

import android.widget.Filter;

import java.util.ArrayList;

public class FilterOrderShop extends Filter {
    private AdapterOrderShop adapter;
    private ArrayList<ModelOrderShop> filterList;

    public FilterOrderShop(AdapterOrderShop adapter , ArrayList < ModelOrderShop > filterList) {
        this.adapter = adapter;
        this.filterList = filterList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        // validate data for search querry
        if(constraint != null && constraint.length () >0 ){
            // search field not empty , search something, perform search
            // change to upper case to make case insensitive
            constraint = constraint.toString ().toUpperCase();
            // store filtered list
            ArrayList<ModelOrderShop> filterModels = new ArrayList<>();
            for (int i=0; i< filterList.size ( ); i++){
                if(filterList.get(i).getOrderStatus().toUpperCase().contains(constraint)
                ){
                    // adding filtered data to list
                    filterModels.add(filterList.get(i));
                }
            }
            results.count = filterModels.size();
            results.values = filterModels;

        }else{
            // search field empty , not searching , return original/complete list
            results.count = filterList.size();
            results.values = filterList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.orderShopArrayList = (ArrayList<ModelOrderShop>)results.values;
        // refresh adapter
        adapter.notifyDataSetChanged();

    }
}
