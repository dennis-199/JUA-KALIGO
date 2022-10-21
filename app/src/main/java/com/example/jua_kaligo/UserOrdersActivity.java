package com.example.jua_kaligo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserOrdersActivity extends AppCompatActivity {
    private RecyclerView shopsRv, ordersRv;

    private ArrayList<ModelOrderUser> ordersList;
    private AdapterOrderUser adapterOrderUser;

    private ImageButton backBtn,writeReviewBtn,moreBtn;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_orders);

        ordersRv = findViewById(R.id.ordersRv);
        moreBtn = findViewById(R.id.moreBtn);
        firebaseAuth = FirebaseAuth.getInstance ();
        progressDialog = new ProgressDialog ( this );
        progressDialog.setTitle ( "Please Wait" );
        progressDialog.setCanceledOnTouchOutside ( false );

        backBtn = findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        //init order list
        ordersList = new ArrayList<>();
        // pop up menu
        PopupMenu popupMenu = new PopupMenu(UserOrdersActivity.this,moreBtn);
        // add menu items to our menu
        popupMenu.getMenu().add("BarGraph");
        popupMenu.getMenu().add("Reviews");
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuitem) {
                if(menuitem.getTitle() == "BarGraph"){
                    startActivity(new Intent(UserOrdersActivity.this,BarChartActivity.class));

                }else if(menuitem.getTitle() == "Reviews"){
                    Intent intent = new Intent(UserOrdersActivity.this, ShopReviewsActivity.class);
                    intent.putExtra("shopUid",""+firebaseAuth.getUid());
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

        //get orders
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ordersList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    String uid = ""+ds.getRef().getKey();

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("Orders");
                    ref.orderByChild("orderBy").equalTo(firebaseAuth.getUid())
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()){
                                        for (DataSnapshot ds: dataSnapshot.getChildren()){
                                            ModelOrderUser modelOrderUser = ds.getValue(ModelOrderUser.class);

                                            //add to list
                                            ordersList.add(modelOrderUser);


                                        }
                                        //setup adapter
                                        adapterOrderUser = new AdapterOrderUser(UserOrdersActivity.this, ordersList);
                                        //set to recyclerview
                                        ordersRv.setAdapter(adapterOrderUser);
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}