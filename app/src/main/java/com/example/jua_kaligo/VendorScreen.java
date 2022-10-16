package com.example.jua_kaligo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;

public class VendorScreen extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private FirebaseAuth mAuth;
    private DatabaseReference reference, reference2;
    private FirebaseUser user;
    private String userID;
    private ImageButton logoutBtn,moreBtn;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private ImageView profileIv, orderview;


    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 300;
    //image pick constants
    private static final int IMAGE_PICK_GALLERY_CODE = 400;
    private static final int IMAGE_PICK_CAMERA_CODE = 500;

    private String[] cameraPermissions;
    private String[] storagePermissions;
    //image picked uri
    private Uri image_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_screen);

        mAuth = FirebaseAuth.getInstance();
        logoutBtn = findViewById ( R.id.logoutBtn);
        firebaseAuth = FirebaseAuth.getInstance ();
        progressDialog = new ProgressDialog( this );
        progressDialog.setTitle ( "Please Wait" );
        progressDialog.setCanceledOnTouchOutside ( false );
        profileIv = findViewById ( R.id.profileIv );
        orderview = findViewById(R.id.orderview);

        orderview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =new Intent(VendorScreen.this, VendorViewOrdersActivity.class);
                startActivity(intent);
            }
        });

        cameraPermissions = new String[]{Manifest.permission.CAMERA , Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        user = FirebaseAuth.getInstance().getCurrentUser();
       reference2 = FirebaseDatabase.getInstance().getReference("Users");
       userID = user.getUid();



        final TextView greetingTextView = (TextView) findViewById(R.id.greetings2);
        final TextView full_nameTextview = (TextView) findViewById(R.id.names2);

        reference2.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Vendor userprofile = snapshot.getValue(Vendor.class);

                Calendar c = Calendar.getInstance();
                int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
                String fullname = userprofile.shopName;
                String profileImage = userprofile.profileImage;

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

                try {
                    Picasso.get().load(profileImage).placeholder(R.drawable.ic_baseline_store_gray).into(profileIv);

                }catch (Exception e){
                    profileIv.setImageResource(R.drawable.ic_baseline_store_gray);

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

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //make offline
                //sign out
                //go to login activity
                makeMeOffline();
            }
        });



    }



    private void makeMeOffline() {
        // after logging out, make user offline
        progressDialog.setMessage ( "Logging out user..." );

        HashMap<String, Object> hashMap = new HashMap <> (  );
        hashMap.put("online","false");

        //update value to db
        DatabaseReference ref = FirebaseDatabase.getInstance ().getReference ("Users");
        ref.child(firebaseAuth.getUid()).updateChildren(hashMap)
                .addOnSuccessListener ( new OnSuccessListener< Void >( ) {
                    @Override
                    public void onSuccess(Void unused) {
                        // update successfully
                        firebaseAuth.signOut ();

                    }
                } )
                .addOnFailureListener ( new OnFailureListener( ) {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failed updating
                        progressDialog.dismiss ();
                        Toast.makeText ( VendorScreen.this , ""+e.getMessage () , Toast.LENGTH_SHORT ).show ( );
                    }
                } );
    }

//    private void checkUser() {
//        FirebaseUser user= firebaseAuth.getCurrentUser ();
//        if (user==null){
//            startActivity ( new Intent( VendorScreen.this, ChooseRole.class ) );
//            finish ();
//        } else {
//            loadMyInfo();
//        }
//    }

//    private void loadMyInfo() {
//        DatabaseReference ref = FirebaseDatabase.getInstance ().getReference ("Users");
//        ref.orderByChild ( "uid" ).equalTo ( firebaseAuth.getUid () )
//                .addValueEventListener ( new ValueEventListener ( ) {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        for (DataSnapshot ds: dataSnapshot.getChildren ()){
//                            //get data from db
//                            //String name = ""+ds.child ( "name" ).getValue ();
//                            //String accountType = ""+ds.child ( "accountType" ).getValue ();
//                           // String email = ""+ds.child ( "email" ).getValue ();
//                            //String shopName = ""+ds.child ( "shopName" ).getValue ();
//                            //String profileImage = ""+ds.child ( "profileImage" ).getValue ();
//
//                            //set data to UI
//                           // nameTv.setText (name);
//                            //shopNameTv.setText(shopName);
//                            //emailTv.setText(email);
//                           // try {
//                             //   Picasso.get().load(profileImage).placeholder(R.drawable.ic_store_gray).into(profileIv);
//                          //  }
//                           // catch (Exception e){
//                             //   profileIv.setImageResource(R.drawable.ic_store_gray);
//
//                           // }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                } );
//    }

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