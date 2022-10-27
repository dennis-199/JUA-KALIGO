package com.example.jua_kaligo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class servicesFragment extends Fragment {

    private RecyclerView shopsRv, ordersRv;
    private FirebaseAuth firebaseAuth;
    private ArrayList<Vendor> shopsList;
    private AdapterShop adapterShop;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_services, container, false);

        firebaseAuth = FirebaseAuth.getInstance ();
        //loadshopdetails();
        shopsRv = (RecyclerView)rootView.findViewById(R.id.shopsRv);


        shopsList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.orderByChild("accountType").equalTo("Vendors")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        shopsList.clear();
                        for(DataSnapshot ds: dataSnapshot.getChildren()){
                            String typeOfBusiness = "" + ds.child("gender").getValue();
                            Vendor modelShop = ds.getValue(Vendor.class);

                            String shopCity = ""+ds.child("city").getValue();

                             if (typeOfBusiness.equals("Services")){
                            shopsList.add(modelShop);
                             }

                            // if you want to see all shops, skip the if statement and this
                            // shopsList.add(modelShop);
                        }
                        adapterShop = new AdapterShop(getActivity(), shopsList);

                        shopsRv.setAdapter(adapterShop);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        return rootView;
    }
}