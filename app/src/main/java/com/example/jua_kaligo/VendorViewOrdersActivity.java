package com.example.jua_kaligo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VendorViewOrdersActivity extends AppCompatActivity {
    private TextView nameTv, shopNameTv, emailTv, tabProductsTv, tabOrdersTv,filteredProductsTv, filteredOrdersTv;
    private ImageButton backBtn,editProfileBtn, addProductBtn,filterProductsBtn,filterOrderBtn, reviewsBtn, settingsBtn,moreBtn;;
    private RecyclerView productsRv, ordersRv;

    private ArrayList<ModelOrderShop> orderShopArrayList;
    private AdapterOrderShop adapterOrderShop;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    //TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_view_orders);
        moreBtn = findViewById(R.id.moreBtn);

        filteredOrdersTv = findViewById(R.id.filteredOrdersTv);
        filterOrderBtn = findViewById(R.id.filterOrderBtn);
        ordersRv = findViewById(R.id.ordersRv);
        TextView textView = (TextView) this.findViewById(R.id.textView);

        backBtn = findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // pop up menu
        PopupMenu popupMenu = new PopupMenu(VendorViewOrdersActivity.this,moreBtn);
        // add menu items to our menu
        popupMenu.getMenu().add("BarGraph");
        popupMenu.getMenu().add("PieChart");
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuitem) {
                if(menuitem.getTitle() == "BarGraph"){
                    startActivity(new Intent(VendorViewOrdersActivity.this,BarChartActivity.class));

                }else if(menuitem.getTitle() == "PieChart"){
                    Intent intent = new Intent(VendorViewOrdersActivity.this, PieChartActivity.class);
                    startActivity(intent);

                }
                return false;
            }
        });
        moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // show popupmenu
                popupMenu.show();

            }
        });

        firebaseAuth = FirebaseAuth.getInstance ();

        loadAllOrders();

        filterOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] options = {"All", "In progress", "Completed", "Cancelled"};

                AlertDialog.Builder builder = new AlertDialog.Builder(VendorViewOrdersActivity.this);
                builder.setTitle("Filter Orders:")
                        .setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (which==0){

                                    filteredOrdersTv.setText("Showing All Orders");
                                    adapterOrderShop.getFilter().filter("");
                                    //int i = adapterOrderShop.getItemCount(); // not working

                                }
                                else{
                                    String optionClicked = options[which];
                                    filteredOrdersTv.setText("showing"+optionClicked+"Orders");
                                    adapterOrderShop.getFilter().filter(optionClicked);
                                    //adapterOrderShop.getItemCount();
                                }
                            }
                        })
                        .show();

            }
        });
        
    }

    private void loadAllOrders() {
        // init array list
        orderShopArrayList = new ArrayList();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child ( firebaseAuth.getUid () ).child ( "Orders" )
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        orderShopArrayList.clear();
                        for(DataSnapshot ds: dataSnapshot.getChildren()){
                            ModelOrderShop modelOrderShop = ds.getValue(ModelOrderShop.class);

                            orderShopArrayList.add(modelOrderShop);

                        }
                        adapterOrderShop = new AdapterOrderShop(VendorViewOrdersActivity.this,orderShopArrayList);

                        ordersRv.setAdapter(adapterOrderShop);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}