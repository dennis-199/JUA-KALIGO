package com.example.jua_kaligo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class categoryFragment extends Fragment {

    private RecyclerView shopsRv, ordersRv;
    private FirebaseAuth firebaseAuth;
    private ArrayList<ModelShop> shopsList;
    private AdapterShop adapterShop;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_category, container, false);

        firebaseAuth = FirebaseAuth.getInstance ();
        loadshopdetails();
        return rootView;
    }

    private void loadshopdetails() {
        DatabaseReference ref = FirebaseDatabase.getInstance ().getReference ("Users");
        ref.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                        for(DataSnapshot ds: datasnapshot.getChildren()){
                            String accountType = ""+ds.child ( "accountType" ).getValue ();
                            String city = ""+ds.child ( "city" ).getValue ();

                            // load shops that are in the city
                            loadShops(city);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void loadShops(String myCity) {
        // init list
        shopsList = new ArrayList<>();

        DatabaseReference ref =FirebaseDatabase.getInstance().getReference("Users");
        ref.orderByChild("accountType").equalTo("Vendors")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // clear list before adding
                        shopsList.clear();
                        for(DataSnapshot ds: snapshot.getChildren()){
                            ModelShop modelShop = ds.getValue(ModelShop.class);

                            String shopCity = ""+ds.child("city").getValue();
                            // show only user city shops
                            if(shopCity.equals(myCity)){
                                shopsList.add(modelShop);
                            }
                            // to display all shops remove if statement
                            // shopsList.add(modelShop);

                        }
                        // set up adapter
                        adapterShop = new AdapterShop(getContext(), shopsList);
                        // set adapter to recycler view
                        shopsRv.setAdapter(adapterShop);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


}