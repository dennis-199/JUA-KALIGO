package com.example.jua_kaligo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class VendorScreen extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private FirebaseAuth mAuth;
    private DatabaseReference reference, reference2;
    private FirebaseUser user;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_screen);

        mAuth = FirebaseAuth.getInstance();

        user = FirebaseAuth.getInstance().getCurrentUser();
       reference2 = FirebaseDatabase.getInstance().getReference("Vendors");
       userID = user.getUid();

        final TextView greetingTextView = (TextView) findViewById(R.id.greetings2);
        final TextView full_nameTextview = (TextView) findViewById(R.id.names2);

        reference2.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Vendor userprofile = snapshot.getValue(Vendor.class);

                Calendar c = Calendar.getInstance();
                int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
                String fullname = userprofile.fullN;

                if(timeOfDay >= 0 && timeOfDay < 12){
                    greetingTextView.setText("Good Morning ");
                    full_nameTextview.setText(fullname+"!");
                }else if(timeOfDay >= 12 && timeOfDay < 16){
                    greetingTextView.setText("Good Afternoon ");
                    full_nameTextview.setText(fullname+"!");
                }else if(timeOfDay >= 16 && timeOfDay < 21){
                    greetingTextView.setText("Good Evening " );
                    full_nameTextview.setText(fullname+"!");
                }else if(timeOfDay >= 21 && timeOfDay < 24){
                    greetingTextView.setText("Good Night ");
                    full_nameTextview.setText(fullname+"!");
                }
                //postbutton.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        bottomNavigationView = findViewById(R.id.navigation2);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavMethod);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment2, new HomeVendorFragment()).commit();

    }
    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavMethod = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment=null;
            switch (item.getItemId())
            {
                case R.id.home1:
                    fragment=new HomeVendorFragment();
                    break;

                case R.id.postven:
                    fragment = new post_ven_Fragment();
                    break;

                case R.id.profile:
                    fragment = new account_Ven();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment2, fragment).commit();
            return true;
        }
    };
}