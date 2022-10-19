package com.example.jua_kaligo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class HomeVendorFragment extends Fragment {
    private TextView nameTv, shopNameTv, emailTv, tabProductsTv, tabOrdersTv,filteredProductsTv, filteredOrdersTv;
    private EditText searchProductEt;
    private ImageButton editProfileBtn, addProductBtn,filterProductsBtn,filterOrderBtn, reviewsBtn, settingsBtn;
    private ImageView profileIv;
    private RelativeLayout productsRl,ordersRl;
    private RecyclerView productsRv, ordersRv;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    private ArrayList<ModelProduct> productList;
    private AdapterProductSeller adapterProductSeller;


    //private ArrayList<ModelOrderShop> orderShopArrayList;
    //private AdapterOrderShop adapterOrderShop;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_vendor, container, false);
        // Inflate the layout for this fragment
        // calling the ids here
        //nameTv = findViewById ( R.id.nameTv );
        //shopNameTv = findViewById ( R.id.shopNameTv);
        //emailTv = findViewById ( R.id.emailTv );
        //tabProductsTv = findViewById ( R.id.tabProductsTv );
        //tabOrdersTv = findViewById ( R.id.tabOrdersTv );
        filteredProductsTv = view.findViewById ( R.id.filteredProductsTv );
        searchProductEt = view.findViewById ( R.id.searchProductEt );

        //editProfileBtn = findViewById ( R.id.editProfileBtn);
        addProductBtn = view.findViewById ( R.id.addProductBtn);
        filterProductsBtn=view.findViewById(R.id.filterProductBtn);
        //filterProductsBtn = findViewById ( R.id.filterProductsBtn);
        profileIv = view.findViewById(R.id.profileIv);
        productsRl = view.findViewById(R.id.productsRl);
        //ordersRl = findViewById(R.id.ordersRl);
        productsRv = view.findViewById(R.id.productsRv);
        //filteredOrdersTv = findViewById(R.id.filteredOrdersTv);
        //filterOrderBtn = findViewById(R.id.filterOrderBtn);
        //ordersRv = findViewById(R.id.ordersRv);
        //reviewsBtn= findViewById(R.id.reviewsBtn);
        //settingsBtn = findViewById(R.id.settingsBtn);

        firebaseAuth = FirebaseAuth.getInstance ();
        progressDialog = new ProgressDialog ( getContext() );
        progressDialog.setTitle ( "Please Wait" );
        progressDialog.setCanceledOnTouchOutside ( false );

        filterProductsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Choose Category")
                        .setItems(Constants.productCategories1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //get selected item
                                String selected = Constants.productCategories1[which];
                                filteredProductsTv.setText(selected);
                                if(selected.equals("All")){
                                    //load all
                                    loadAllProducts();
                                }else{
                                    // load filtered
                                    loadFilteredProducts(selected);
                                }


                            }
                        }).show();
            }
        });

        loadAllProducts();
        // search
        searchProductEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    adapterProductSeller.getFilter().filter(s);

                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return view;
    }

    private void loadFilteredProducts(String selected) {
        productList = new ArrayList <> ();

        // get all products
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid()).child("Products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // before getting reset list
                productList.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    String productCategory = ""+ds.child("productCategory").getValue();

                    // if selected category matches product category then add in list
                    if(selected.equals(productCategory)){
                        ModelProduct modelProduct = ds.getValue(ModelProduct.class);
                        productList.add(modelProduct);
                    }


                }
                // setup adapter
                adapterProductSeller = new AdapterProductSeller(getContext(),productList);
                // set adapter
                productsRv.setAdapter(adapterProductSeller);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void loadAllProducts() {
        productList = new ArrayList <> ();

        // get all products
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid()).child("Products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // before getting reset list
                productList.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    ModelProduct modelProduct = ds.getValue(ModelProduct.class);
                    productList.add(modelProduct);

                }
                // setup adapter
                adapterProductSeller = new AdapterProductSeller(getContext(),productList);
                // set adapter
                productsRv.setAdapter(adapterProductSeller);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}