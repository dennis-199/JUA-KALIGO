package com.example.jua_kaligo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import p32929.androideasysql_library.Column;
import p32929.androideasysql_library.EasyDB;


public class Homefragment extends Fragment {
    private static final String TAG = "RecyclerViewFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;
    private static final int DATASET_COUNT = 60;

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected LayoutManagerType mCurrentLayoutManagerType;

    protected RadioButton mLinearLayoutRadioButton;
    protected RadioButton mGridLayoutRadioButton;

    protected RecyclerView mRecyclerView;
    protected Adapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected String[] mDataset;



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

    private ArrayList<ModelCartItem> cartItemsList;
    private AdapterCartItem adapterCartItem;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_homefragment, container, false);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //init UI views

        shopIv = rootView.findViewById ( R.id.shopIv );
        shopNameTv = rootView.findViewById ( R.id.shopNameTv );
        phoneTv = rootView.findViewById ( R.id.phoneTv );
        emailTv = rootView.findViewById ( R.id.emailTv );
        openClosedTv = rootView.findViewById ( R.id.openClosedTv );
        deliveryFeeTv = rootView.findViewById ( R.id.deliveryFeeTv );
        addressTv = rootView.findViewById ( R.id.addressTv );
        callBtn = rootView.findViewById ( R.id.callBtn );
        mapBtn = rootView.findViewById ( R.id.mapBtn );
        cartBtn = rootView.findViewById ( R.id.cartBtn );
        backBtn = rootView.findViewById ( R.id.backBtn );
        searchProductEt = rootView.findViewById ( R.id.searchProductEt );
        filterProductsBtn = rootView.findViewById ( R.id.filterProductsBtn );
        filteredProductsTv = rootView.findViewById ( R.id.filteredProductsTv );
        productsRv = rootView.findViewById ( R.id.productsRv );
        cartCountTv = rootView.findViewById ( R.id.cartCountTv );
        reviewBtn = rootView.findViewById ( R.id.reviewBtn );
        ratingBar = rootView.findViewById ( R.id.ratingBar );

        //init progress dialog
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        //shopUid = getIntent ().getStringExtra ( "shopUid" );
        firebaseAuth = FirebaseAuth.getInstance ();
        loadShopProducts();


        easyDB =  EasyDB.init(getContext(),"ITEMS_DB")
                .setTableName("ITEMS_TABLE")
                .addColumn(new Column("Item_Id", new String[]{"text","unique"}))
                .addColumn(new Column("Item_PID", new String[]{"text","not null"}))
                .addColumn(new Column("Item_Name", new String[]{"text","not null"}))
                .addColumn(new Column("Item_Price_Each", new String[]{"text","not null"}))
                .addColumn(new Column("Item_Price", new String[]{"text","not null"}))
                .addColumn(new Column("Item_Quantity", new String[]{"text","not null"}))
                .doneTableColumn();

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
//        cartBtn.setOnClickListener ( new View.OnClickListener ( ) {
//            @Override
//            public void onClick(View v) {
//                showCartDialog();
//
//            }
//        } );

        filterProductsBtn.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder (getContext() );
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


        return rootView;
    }
    public double allTotalPrice = 0.00;
    public TextView sTotalTv, dFeeTv, allTotalPriceTv, promoDescriptionTv, discountTv;
    public EditText promoCodeEt;
    public Button applyBtn;

    private void showCartDialog() {
        //init list
        cartItemsList = new ArrayList<>();

        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_cart, null);

        TextView shopNameTv = view.findViewById(R.id.shopNameTv);
        promoCodeEt = view.findViewById(R.id.promoCodeEt);
        promoDescriptionTv = view.findViewById(R.id.promoDescriptionTv);
        discountTv = view.findViewById(R.id.discountTv);
        FloatingActionButton validateBtn = view.findViewById(R.id.validateBtn);
        applyBtn = view.findViewById(R.id.applyBtn);
        RecyclerView cartItemsRv = view.findViewById(R.id.cartItemsRv);
        sTotalTv = view.findViewById(R.id.sTotalTv);
        dFeeTv = view.findViewById(R.id.dFeeTv);
        allTotalPriceTv = view.findViewById(R.id.totalTv);
        Button checkoutBtn = view.findViewById(R.id.checkoutBtn);

        // whenever cart dialog shows check if promo code is applied
        if(isPromoCodeApplied){
            // applied
            promoDescriptionTv.setVisibility(View.VISIBLE);
            applyBtn.setVisibility(View.VISIBLE);
            applyBtn.setText("Applied");
            promoCodeEt.setText(promoCode);
            promoDescriptionTv.setText(promoDescription);
        }else {
            // not applied
            promoDescriptionTv.setVisibility(View.GONE);
            applyBtn.setVisibility(View.GONE);
            applyBtn.setText("Apply");
        }

        //dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        //set view to dialog
        builder.setView(view);

        shopNameTv.setText(shopName);

        EasyDB easyDB =  EasyDB.init(getContext(),"ITEMS_DB")
                .setTableName("ITEMS_TABLE")
                .addColumn(new Column("Item_Id", new String[]{"text","unique"}))
                .addColumn(new Column("Item_PID", new String[]{"text","not null"}))
                .addColumn(new Column("Item_Name", new String[]{"text","not null"}))
                .addColumn(new Column("Item_Price_Each", new String[]{"text","not null"}))
                .addColumn(new Column("Item_Price", new String[]{"text","not null"}))
                .addColumn(new Column("Item_Quantity", new String[]{"text","not null"}))
                .doneTableColumn();

        //get all records from db
        Cursor res = easyDB.getAllData();
        while (res.moveToNext()){
            String id = res.getString(1);
            String pid = res.getString(2);
            String name = res.getString(3);
            String price = res.getString(4);
            String cost = res.getString(5);
            String quantity = res.getString(6);

            allTotalPrice = allTotalPrice + Double.parseDouble(cost);

            ModelCartItem modelCartItem = new ModelCartItem(
                    ""+id, ""+pid, ""+name, ""+price, ""+cost, ""+quantity

            );


            cartItemsList.add(modelCartItem);

        }
        //setup adapter
        adapterCartItem = new AdapterCartItem(getContext(), cartItemsList);
        //set to recyclerview
        cartItemsRv.setAdapter(adapterCartItem);

        if(isPromoCodeApplied){
            priceWithDiscount();
        }else{
            priceWithoutDiscount();
        }
        //show dialog
        AlertDialog dialog = builder.create();
        dialog.show();

        //reset total price on dialog dismiss
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                allTotalPrice = 0.00;
            }
        });
        //place order
        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //first validate delivery address
                if (myLatitude.equals("") || myLatitude.equals("null") || myLongitude.equals("") || myLongitude.equals("null")){
                    //user didn't enter address in profile
                    Toast.makeText(getContext(), "Please enter your address in your profile before placing order...", Toast.LENGTH_SHORT).show();
                    return;//don't proceed further
                }
                if (myPhone.equals("") || myPhone.equals("null")){
                    //user didn't enter phone number in profile
                    Toast.makeText(getContext(), "Please enter your phone number in your profile before placing order...", Toast.LENGTH_SHORT).show();
                    return;//don't proceed further
                }
                if (cartItemsList.size() == 0){
                    //cart list is empty
                    Toast.makeText(getContext(), "No item in cart", Toast.LENGTH_SHORT).show();
                    return;//don't proceed further

                }
                submitOrder();
            }
        });
        //start validating promo code when validate button is pressed
        validateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String promotionCode = promoCodeEt.getText().toString().trim();
                if(TextUtils.isEmpty(promotionCode)){
                    Toast.makeText(getContext(), "Please enter promo code...", Toast.LENGTH_SHORT).show();
                }else{
                    checkCodeAvailability(promotionCode);
                }
            }
        });

        // apply code
        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPromoCodeApplied = true;
                applyBtn.setText("Applied");

                priceWithDiscount();

            }
        });

    }

    private void priceWithDiscount(){
        discountTv.setText("ksh"+promoPrice);
        dFeeTv.setText("ksh"+deliveryFee);
        sTotalTv.setText("ksh"+String.format("%.2f",allTotalPrice));
        allTotalPriceTv.setText("ksh"+(allTotalPrice+Double.parseDouble(deliveryFee.replace("ksh","")) - Double.parseDouble(promoPrice)));
    }
    private void priceWithoutDiscount() {
        discountTv.setText("ksh0");
        dFeeTv.setText("ksh"+deliveryFee);
        sTotalTv.setText("ksh"+String.format("%.2f",allTotalPrice));
        allTotalPriceTv.setText("ksh"+(allTotalPrice + Double.parseDouble(deliveryFee.replace("ksh",""))));
    }
    public boolean isPromoCodeApplied = false;
    public String promoId, promoTimestamp, promoCode, promoDescription,promoExpDate, promoMinimumOrderPrice,promoPrice;
    private void checkCodeAvailability(String promotionCode){
        // progress bar
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Checking promo code ...");
        progressDialog.setCanceledOnTouchOutside(false);

        // promo code is not applied yet
        isPromoCodeApplied = false;
        applyBtn.setText("Apply");
        priceWithoutDiscount();
        // check promo availability
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(shopUid).child("Promotions").orderByChild("promoCode").equalTo(promotionCode)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // check if promotion code exists
                        if(snapshot.exists()){
                            //promo code exists
                            progressDialog.dismiss();
                            for(DataSnapshot ds: snapshot.getChildren()){
                                promoId = ""+ds.child("id").getValue();
                                promoTimestamp = ""+ds.child("timestamp").getValue();
                                promoCode = ""+ds.child("promoCode").getValue();
                                promoDescription = ""+ds.child("description").getValue();
                                promoExpDate = ""+ds.child("expireDate").getValue();
                                promoMinimumOrderPrice = ""+ds.child("minimumOrderPrice").getValue();
                                promoPrice = ""+ds.child("promoPrice").getValue();

                                //now check if code is expired or not
                                checkCodeExpireDate();


                            }
                        }else {
                            // enter promocode dosent exist
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Invalid promo code", Toast.LENGTH_SHORT).show();
                            applyBtn.setVisibility(View.GONE);
                            promoDescriptionTv.setVisibility(View.GONE);
                            promoDescriptionTv.setText("");
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }

    private void checkCodeExpireDate() {
        // Get current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        String todayDate = day+"/"+month+"/"+year;
        /*-- check for expire  date */
        try {
            SimpleDateFormat sdformat = new SimpleDateFormat("dd/MM/yyyy");
            Date currentDate = sdformat.parse(todayDate);
            Date expireDate = sdformat.parse(promoExpDate);
            // compares
            if(expireDate.compareTo(currentDate)>0){
                //
                checkMinimumOrderPrice();
            } else if(expireDate.compareTo(currentDate)<0){
                //
                Toast.makeText(getContext(), "The promotion code expired on "+promoExpDate, Toast.LENGTH_SHORT).show();
                applyBtn.setVisibility(View.GONE);
                promoDescriptionTv.setVisibility(View.GONE);
                promoDescriptionTv.setText("");
            }
            else if(expireDate.compareTo(currentDate) == 0){
                // both dates are equal
                checkMinimumOrderPrice();

            }

        }catch (Exception e){
            // if anything goes wrong while comparing current date and expiry date
            Toast.makeText(getContext(), "error"+e.getMessage(), Toast.LENGTH_SHORT).show();
            applyBtn.setVisibility(View.GONE);
            promoDescriptionTv.setVisibility(View.GONE);
            promoDescriptionTv.setText("");
        }
    }

    private void checkMinimumOrderPrice() {
        // each promo code have minimum order price requirement
        if(Double.parseDouble(String.format("%2f",allTotalPrice))< Double.parseDouble(promoMinimumOrderPrice)){
            // current order price less than minimum
            Toast.makeText(getContext(), "This code is valid for order with minimum amount: ksh"+promoMinimumOrderPrice, Toast.LENGTH_SHORT).show();
            applyBtn.setVisibility(View.GONE);
            promoDescriptionTv.setVisibility(View.GONE);
            promoDescriptionTv.setText("");

        }else {
            // current order price isequal toor greater than minimumorder price required by promo allow apply code
            applyBtn.setVisibility(View.VISIBLE);
            promoDescriptionTv.setVisibility(View.VISIBLE);
            promoDescriptionTv.setText(promoDescription);
        }
    }


    private void submitOrder() {
        //show progress dialog
        progressDialog.setMessage("Placing order...");
        progressDialog.show();

        //for order id and order time
        String timestamp = ""+System.currentTimeMillis();

        String cost = allTotalPriceTv.getText().toString().trim().replace("Ksh","");

        //setup order data
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("orderId", ""+timestamp);
        hashMap.put("orderTime", ""+timestamp);
        hashMap.put("orderStatus", "In Progress");
        hashMap.put("orderCost", ""+cost);
        hashMap.put("orderBy", ""+firebaseAuth.getUid());
        hashMap.put("orderTo", ""+shopUid);
        hashMap.put("latitude", ""+myLatitude);
        hashMap.put("longitude", ""+myLongitude);
        hashMap.put("deliveryFee", ""+deliveryFee);

        if(isPromoCodeApplied){
            // promo applied
            hashMap.put("discount",""+promoPrice);
        }else {
            // promo not applied
            hashMap.put("discount","0");
        }



        //add to db
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(shopUid).child("Orders");
        ref.child(timestamp).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //order info added now add order items
                        for (int i = 0; i < cartItemsList.size(); i++) {
                            String pId = cartItemsList.get(i).getpId();
                            String id = cartItemsList.get(i).getId();
                            String cost = cartItemsList.get(i).getCost();
                            String name = cartItemsList.get(i).getName();
                            String price = cartItemsList.get(i).getPrice();
                            String quantity = cartItemsList.get(i).getQuantity();

                            HashMap<String, String> hashMap1 = new HashMap<>();
                            hashMap1.put("pId", pId);
                            hashMap1.put("name", name);
                            hashMap1.put("cost", cost);
                            hashMap1.put("price", price);
                            hashMap1.put("quantity", quantity);

                            ref.child(timestamp).child("Items").child(pId).setValue(hashMap1);

                        }
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Order Placed Successfully...", Toast.LENGTH_SHORT).show();


                        //prepareNotificationMessage (timestamp);


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failed placing order
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void loadShopProducts() {
        // init list
        productsList = new ArrayList <> (  );
        DatabaseReference reference = FirebaseDatabase.getInstance ().getReference ("Users");
        reference.child ( "vXXuemeZSWPFtX3zFMR3bqYIOQt2").child ( "Products" )
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
                        adapterProductUser = new AdapterProductUser ( getContext(),productsList );
                        //set adapter
                        productsRv.setAdapter(adapterProductUser);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                } );
    }


}