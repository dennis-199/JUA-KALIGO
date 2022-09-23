package com.example.jua_kaligo;

import android.view.LayoutInflater;
import android.widget.Filter;

import java.util.ArrayList;

public class FilterProduct extends Filter {
    private AdapterProductSeller adapter;
    private ArrayList<ModelProduct> filterList;

    public FilterProduct(AdapterProductSeller adapter , ArrayList < ModelProduct > filterList) {
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
            ArrayList<ModelProduct> filterModels = new ArrayList<>();
            for (int i=0; i< filterList.size ( ); i++){
                if(filterList.get(i).getProductTitle().toUpperCase().contains(constraint)  ||
                        filterList.get ( i ).getProductCategory ().toUpperCase ().contains ( constraint )
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
        return null;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.productList = (ArrayList<ModelProduct>)results.values;
        // refresh adapter
        adapter.notifyDataSetChanged();

    }
}
