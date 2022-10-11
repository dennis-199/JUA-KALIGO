package com.example.jua_kaligo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import p32929.androideasysql_library.Column;
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

    private ArrayList<ModelCartItem> cartItemsList;
    private AdapterCartItem adapterCartItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_shop_details );

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
        loadShopDetails();
        loadShopProducts();
        loadReviews();

        easyDB =  EasyDB.init(this,"ITEMS_DB")
                .setTableName("ITEMS_TABLE")
                .addColumn(new Column("Item_Id", new String[]{"text","unique"}))
                .addColumn(new Column("Item_PID", new String[]{"text","not null"}))
                .addColumn(new Column("Item_Name", new String[]{"text","not null"}))
                .addColumn(new Column("Item_Price_Each", new String[]{"text","not null"}))
                .addColumn(new Column("Item_Price", new String[]{"text","not null"}))
                .addColumn(new Column("Item_Quantity", new String[]{"text","not null"}))
                .doneTableColumn();

        //each shop has its own products and orders so if user add items to cart and go back and open cart in different shop then cart should be different
        //so delete cart data whenever user opens this activity
        deleteCartData();
        cartCount ();

        searchProductEt.addTextChangedListener ( new TextWatcher ( ) {
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

        backBtn.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                // go back to previous activity
                onBackPressed ();
            }
        } );

        cartBtn.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                showCartDialog();

            }
        } );

        callBtn.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                dialPhone();
            }
        } );

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

        //handle review button click, open review activity

        reviewBtn.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                // pass shop uid to show its reviews
                Intent intent = new Intent(ShopDetailsActivity.this,ShopReviewsActivity.class);
                intent.putExtra ("shopUid",shopUid  );
                startActivity ( intent );
            }
        } );
    }

    private float ratingSum =0;
    private void loadReviews() {

        DatabaseReference ref = FirebaseDatabase.getInstance ().getReference ("Users");
        ref.child ( shopUid ).child ( "Ratings" )
                .addValueEventListener ( new ValueEventListener ( ) {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // clear list before adding data into it
                        ratingSum=0;
                        for(DataSnapshot ds: dataSnapshot.getChildren ()){
                            float rating = Float.parseFloat ( ""+ds.child ( "ratings" ).getValue () );
                            ratingSum = ratingSum+rating;


                        }
                        long numberOfReviews = dataSnapshot.getChildrenCount ();
                        float avgRating = ratingSum/numberOfReviews;

                        ratingBar.setRating ( avgRating );

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                } );
    }


    private void deleteCartData() {

        easyDB.deleteAllDataFromTable();//delete all records from cart

    }

    public void cartCount(){

        int count = easyDB.getAllData ( ).getCount ();
        if (count<=0){
            cartCountTv.setVisibility ( View.GONE );
        }
        else {
            cartCountTv.setVisibility ( View.VISIBLE );
            cartCountTv.setText ( ""+count );
        }
    }


    public double allTotalPrice = 0.00;
    public TextView sTotalTv, dFeeTv, allTotalPriceTv;

    private void showCartDialog() {
        //init list
        cartItemsList = new ArrayList<>();

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_cart, null);

        TextView shopNameTv = view.findViewById(R.id.shopNameTv);
        RecyclerView cartItemsRv = view.findViewById(R.id.cartItemsRv);
        sTotalTv = view.findViewById(R.id.sTotalTv);
        dFeeTv = view.findViewById(R.id.dFeeTv);
        allTotalPriceTv = view.findViewById(R.id.totalTv);
        Button checkoutBtn = view.findViewById(R.id.checkoutBtn);

        //dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //set view to dialog
        builder.setView(view);

        shopNameTv.setText(shopName);

        EasyDB easyDB =  EasyDB.init(this,"ITEMS_DB")
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

            ModelCartItem modelCartItem = new ModelCartItem(""+id, ""+pid, ""+name, ""+price, ""+cost, ""+quantity

            );

            cartItemsList.add(modelCartItem);
        }
        //setup adapter
        adapterCartItem = new AdapterCartItem(this, cartItemsList);
        //set to recyclerview
        cartItemsRv.setAdapter(adapterCartItem);



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
                    Toast.makeText(ShopDetailsActivity.this, "Please enter your address in your profile before placing order...", Toast.LENGTH_SHORT).show();
                    return;//don't proceed further
                }
                if (myPhone.equals("") || myPhone.equals("null")){
                    //user didn't enter phone number in profile
                    Toast.makeText(ShopDetailsActivity.this, "Please enter your phone number in your profile before placing order...", Toast.LENGTH_SHORT).show();
                    return;//don't proceed further
                }
                if (cartItemsList.size() == 0){
                    //cart list is empty
                    Toast.makeText(ShopDetailsActivity.this, "No item in cart", Toast.LENGTH_SHORT).show();
                    return;//don't proceed further

                }
                submitOrder();
            }
        });

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
                        Toast.makeText(ShopDetailsActivity.this, "Order Placed Successfully...", Toast.LENGTH_SHORT).show();


                        prepareNotificationMessage (timestamp);


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failed placing order
                        progressDialog.dismiss();
                        Toast.makeText(ShopDetailsActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void openMap() {
        String address = "https://maps.google.com/maps?saddr="+ myLatitude + "," + myLongitude + "&daddr=" + shopLatitude + "," + shopLongitude;
        Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse ( address ));
        startActivity ( intent );
    }

    private void dialPhone() {

        startActivity ( new Intent (Intent.ACTION_DIAL, Uri.parse ( "tel:"+Uri.encode ( shopPhone )))  );
        Toast.makeText ( this , ""+shopPhone , Toast.LENGTH_SHORT ).show ( );

    }

    private void loadMyInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance ().getReference ("Users");
        ref.orderByChild ( "uid" ).equalTo ( firebaseAuth.getUid () )
                .addValueEventListener ( new ValueEventListener ( ) {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds: dataSnapshot.getChildren ()) {
                            String name = "" + ds.child ( "name" ).getValue ( );
                            String email = "" + ds.child ( "email" ).getValue ( );
                            myPhone = "" + ds.child ( "phone" ).getValue ( );
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
                shopLatitude = "" + snapshot.child ( "latitude" ).getValue ( );
                shopLongitude = "" + snapshot.child ( "longitude" ).getValue ( );
                deliveryFee = "" + snapshot.child ( "deliveryFee" ).getValue ( );
                String profileImage = "" + snapshot.child ( "profileImage" ).getValue ( );
                String shopOpen = "" + snapshot.child ( "shopOpen" ).getValue ( );

                // set data

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

    private void prepareNotificationMessage(String orderId){
        // when user places order, send notification to seller

        //prepare data for notification
        //String NOTIFICATION_TOPIC = "/topics/" + Constants.FCM_TOPIC;//must be same as subscribed by user
        String NOTIFICATION_TITLE = "New Order "+ orderId;
        String NOTIFICATIONS_MESSAGE = "Congratulations....! You have a new order";
        String NOTIFICATION_TYPE = "NewOrder";

        //prepare json (what to send and where to send)
        JSONObject notificationsJo = new JSONObject();
        JSONObject notificationBodyJo = new JSONObject();
        try {
            //what to send
            notificationBodyJo.put("notificationType", NOTIFICATION_TYPE);
            notificationBodyJo.put("buyerUid", firebaseAuth.getUid ());//current user uid is buyer uid
            notificationBodyJo.put("sellerUid", shopUid);
            notificationBodyJo.put("orderId", orderId);
            notificationBodyJo.put("notificationTitle", NOTIFICATION_TITLE);
            notificationBodyJo.put("notificationMessage", NOTIFICATIONS_MESSAGE);
            //where to send
           // notificationsJo.put("to", NOTIFICATION_TOPIC);//to all who subscribed to this topic
            notificationsJo.put("data", notificationBodyJo);


        }
        catch (Exception e){
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

        }

        sendFcmNotification(notificationsJo, orderId);
    }

    private void sendFcmNotification(JSONObject notificationsJo, String orderId) {
        //send volley request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("https://fcm.googleapis.com/fcm/send", notificationsJo, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // for sending fcm start order details activity

                Intent intent = new Intent(ShopDetailsActivity.this, OrderDetailsUsersActivity.class);
                intent.putExtra("orderTo", shopUid);
                intent.putExtra("orderId", orderId);
                startActivity(intent);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // if failed sending fcm, still  start order details activity

                Intent intent = new Intent(ShopDetailsActivity.this, OrderDetailsUsersActivity.class);
                intent.putExtra("orderTo", shopUid);
                intent.putExtra("orderId", orderId);
                startActivity(intent);

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                //put required headers
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type","application/json");
               // headers.put("Authorization","key=" +Constants.FCM_KEY);

                return headers;
            }
        };

        //enquire the volley request
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }


}