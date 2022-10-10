package com.example.jua_kaligo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import p32929.androideasysql_library.EasyDB;

public class ShopDetailsActivity extends AppCompatActivity {

    // declare UI views

    private ImageView shopIv;
    private TextView shopNameTv, phoneTv,emailTv, openClosedTv,
            deliveryFeeTv, addressTv, filteredProductsTv, cartCountTv;
    private ImageButton callBtn, mapBtn,cartBtn,backBtn,filterProductsBtn,reviewBtn;
    private EditText searchProductEt;
    private RecyclerView productsRv;
    private RatingBar ratingBar;

    private String shopUid;
    private String myLatitude, myLongitude, myPhone;
    private String shopName, shopEmail, shopPhone, shopAddress, shopLatitude, shopLongitude;
    public String deliveryFee;

    private FirebaseAuth firebaseAuth;
    private EasyDB easyDB;

    //progress dialog
    private ProgressDialog progressDialog;
    private AdapterProductUser adapterProductUser;
    private ArrayList< ModelProduct > productsList;

    //private ArrayList<ModelCartItem> cartItemsList;
    //private AdapterCartItem adapterCartItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_details);

        //init UI views

        shopIv = findViewById ( R.id.shopIv );
        shopNameTv = findViewById ( R.id.shopNameTv );
        phoneTv = findViewById ( R.id.phoneTv );
        emailTv = findViewById ( R.id.emailTv );
        openClosedTv = findViewById ( R.id.openClosedTv );
        deliveryFeeTv = findViewById ( R.id.deliveryFeeTv );
        addressTv = findViewById ( R.id.addressTv );
        callBtn = findViewById ( R.id.callBtn );
        mapBtn = findViewById ( R.id.mapBtn );
        cartBtn = findViewById ( R.id.cartBtn );
        backBtn = findViewById ( R.id.backBtn );
        searchProductEt = findViewById ( R.id.searchProductEt );
        filterProductsBtn = findViewById ( R.id.filterProductsBtn );
        filteredProductsTv = findViewById ( R.id.filteredProductsTv );
        productsRv = findViewById ( R.id.productsRv );
        cartCountTv = findViewById ( R.id.cartCountTv );
        reviewBtn = findViewById ( R.id.reviewBtn );
        ratingBar = findViewById ( R.id.ratingBar );

        //init progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        shopUid = getIntent ().getStringExtra ( "shopUid" );
        firebaseAuth = FirebaseAuth.getInstance ();

        loadMyInfo();

        // loading shop details

        loadShopDetails();
        loadShopProducts();

        // search
        searchProductEt.addTextChangedListener ( new TextWatcher( ) {
            @Override
            public void beforeTextChanged(CharSequence s , int start , int count , int after) {

            }

            @Override
            public void onTextChanged(CharSequence s , int start , int before , int count) {
                try{
                    adapterProductUser.getFilter ().filter ( s );
                }
                catch(Exception e) {
                    e.printStackTrace ();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        } );

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go back to previous activity
                onBackPressed ();
            }
        });
        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // cart
            }
        });
        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialPhone();
            }
        });
        mapBtn.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                openMap();
            }
        } );
        filterProductsBtn.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder (ShopDetailsActivity.this );
                builder.setTitle ( "Choose Category:" )
                        .setItems ( Constants.productCategories1 , new DialogInterface.OnClickListener ( ) {
                            @Override
                            public void onClick(DialogInterface dialog , int which) {
                                //get selected item
                                String selected = Constants.productCategories1[which];
                                filteredProductsTv.setText ( (selected) );
                                if(selected.equals ( "All" )) {
                                    loadShopProducts ();
                                }
                                else {
                                    // load filtered
                                    adapterProductUser.getFilter ().filter ( selected );
                                }
                            }
                        } ).show ();

            }
        } );

    }

    private void openMap() {
        String address = "https://maps.google.com/maps?saddr="+ myLatitude + "," + myLongitude + "&daddr=" + shopLatitude + "," + shopLongitude;
        Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse ( address ));
        startActivity ( intent );
    }

    private void dialPhone() {
        startActivity ( new Intent(Intent.ACTION_DIAL, Uri.parse ( "tel:"+Uri.encode ( shopPhone )))  );
        Toast.makeText ( this , ""+shopPhone , Toast.LENGTH_SHORT ).show ( );
    }

    private void loadMyInfo() {

        DatabaseReference ref = FirebaseDatabase.getInstance ().getReference ("Users");
        ref.orderByChild ( shopUid ).equalTo ( firebaseAuth.getUid () )
                .addValueEventListener ( new ValueEventListener( ) {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds: dataSnapshot.getChildren ()) {
                            String name = "" + ds.child ( "name" ).getValue ( );
                            String email = "" + ds.child ( "email" ).getValue ( );
                            myPhone = "" + ds.child ( "Phone" ).getValue ( );
                            String profileImage = "" + ds.child ( "profileImage" ).getValue ( );
                            String accountType = "" + ds.child ( "accountType" ).getValue ( );
                            String city = "" + ds.child ( "city" ).getValue ( );
                            myLatitude = "" + ds.child ( "latitude" ).getValue ( );
                            myLongitude = "" + ds.child ( "longitude" ).getValue ( );

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                } );
    }
    private void loadShopDetails() {
        DatabaseReference ref = FirebaseDatabase.getInstance ().getReference ("Users");
        ref.child ( shopUid ).addValueEventListener ( new ValueEventListener ( ) {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String name = "" + snapshot.child ( "name" ).getValue ( );
                shopName = "" + snapshot.child ( "shopName" ).getValue ( );
                shopEmail = "" + snapshot.child ( "email" ).getValue ( );
                shopPhone = "" + snapshot.child ( "phones" ).getValue ( );
                shopAddress = "" + snapshot.child ( "address" ).getValue ( );
                //shopLatitude = "" + snapshot.child ( "latitude" ).getValue ( );
                //shopLongitude = "" + snapshot.child ( "longitude" ).getValue ( );
                deliveryFee = "" + snapshot.child ( "deliveryFee" ).getValue ( );
                String profileImage = "" + snapshot.child ( "profileImage" ).getValue ( );
                String shopOpen = "" + snapshot.child ( "shopOpen" ).getValue ( );

                 //set data

                shopNameTv.setText ( shopName );
                emailTv.setText ( shopEmail );
                phoneTv.setText ( shopPhone );
                deliveryFeeTv.setText ( "Delivery Fee: Ksh"+deliveryFee );
                addressTv.setText ( shopAddress );

                if(shopOpen.equals ( "true" )){
                    openClosedTv.setText ( "Open" );
                }
                else {
                    openClosedTv.setText ( "Closed" );
                }
                try {
                    Picasso.get ().load ( profileImage ).into ( shopIv );
                }
                catch(Exception e) {

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        } );
    }
    private void loadShopProducts() {
        // init list
        productsList = new ArrayList <> (  );

        DatabaseReference reference = FirebaseDatabase.getInstance ().getReference ("Users");
        reference.child ( shopUid).child ( "Products" )
                .addValueEventListener ( new ValueEventListener ( ) {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // clear list before adding products
                        productsList.clear ();
                        for(DataSnapshot ds: snapshot.getChildren ()){
                            ModelProduct modelProduct = ds.getValue ( ModelProduct.class );
                            productsList.add ( modelProduct );
                        }
                        //setup adapter
                        adapterProductUser = new AdapterProductUser ( ShopDetailsActivity.this,productsList );
                        //set adapter
                        productsRv.setAdapter(adapterProductUser);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                } );
    }
}