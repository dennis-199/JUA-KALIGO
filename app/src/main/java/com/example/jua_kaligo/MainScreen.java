package com.example.jua_kaligo;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;

public class MainScreen extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private NavController navController;
    private FirebaseAuth mAuth;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private DatabaseReference reference, reference2;
    private String userID;
    private TextView textView, fullname;
    FloatingActionButton postbutton;
    private ImageButton logoutBtn;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        postbutton = findViewById(R.id.postButton);
        logoutBtn = findViewById(R.id.logoutBtn);
        mAuth = FirebaseAuth.getInstance();

        // try here
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference2 = FirebaseDatabase.getInstance().getReference("Vendors");
        userID = user.getUid();

        firebaseAuth = FirebaseAuth.getInstance ();
        progressDialog = new ProgressDialog ( this );
        progressDialog.setTitle ( "Please Wait" );
        progressDialog.setCanceledOnTouchOutside ( false );

        final TextView greetingTextView = (TextView) findViewById(R.id.greetings);
        final TextView full_nameTextview = (TextView) findViewById(R.id.names);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Customer userprofile = snapshot.getValue(Customer.class);

                Calendar c = Calendar.getInstance();
                int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
                String fullname = userprofile.name;

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
                postbutton.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });


        //if(mAuth.getCurrentUser().equals(rootRef.child("Customers"))){
           // postbutton.setVisibility(View.INVISIBLE);
        //}
        //if(mAuth.getCurrentUser().getUid().equals("Customers")){
          //  postbutton.setVisibility(View.INVISIBLE);
        //}

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do this
                // after logging out, make user offline
                progressDialog.setMessage ( "Logging out user..." );

                HashMap<String, Object> hashMap = new HashMap <> (  );
                hashMap.put("online","false");
                // update value to db
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                ref.child(firebaseAuth.getUid()).updateChildren(hashMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                firebaseAuth.signOut();
                                startActivity(new Intent(MainScreen.this, SignIn.class));
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss ();
                                Toast.makeText(MainScreen.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });


        bottomNavigationView = findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavMethod);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new Homefragment()).commit();

    }
    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavMethod = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment fragment=null;
            switch (item.getItemId())
            {
                case R.id.home:
                    fragment=new Homefragment();
                    break;

                case R.id.categories:
                    fragment=new categoryFragment();
                    break;

                case R.id.services:
                    fragment=new servicesFragment();
                    break;

                case R.id.trade:
                    fragment=new TradeFragment();
                    break;

                case R.id.account:
                    fragment=new AccountFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment).commit();
            return true;
        }
    };

    public void postactivity(View view) {
        Intent intent = new Intent(this,PostActivity.class);
        startActivity(intent);

    }
}