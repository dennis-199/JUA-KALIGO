package com.example.jua_kaligo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Splash_Screen extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //make fullscreen
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        firebaseAuth = FirebaseAuth.getInstance ();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //FirebaseUser user = firebaseAuth.getCurrentUser();
                //if(user == null){
                    // user not logged in start login activity
                    startActivity(new Intent(Splash_Screen.this,ChooseRole.class));
                //}else{
                    // user is logged in
                   // checkUserType ();
                }
           // }
        },1000);



    }
    private void checkUserType(){
        String check;

        // if user is vendor, start vendor main screen
        // if user is buyer,start user main screen
        // test
        //DatabaseReference test;
        //if() {
        //    test = FirebaseDatabase.getInstance().getReference("Customers");
        //} else {
        //    test = FirebaseDatabase.getInstance().getReference("Customers");
        //}{
        //    test.child(firebaseAuth.getUid());
        //}
        // was working from here
        //DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Customers");
        //DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("Vendors");

//        ref.child(firebaseAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                startActivity(new Intent(Splash_Screen.this,MainScreen.class));
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        ref2.child(firebaseAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                startActivity(new Intent(Splash_Screen.this,VendorScreen.class));
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }
}