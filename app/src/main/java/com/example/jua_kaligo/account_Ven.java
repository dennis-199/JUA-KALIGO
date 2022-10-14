package com.example.jua_kaligo;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class account_Ven extends Fragment {
    private ImageButton backBtn, gpsBtn,editProfileBtn, reviewsBtn;
    private ImageView profileIv;
    private TextView nameEt, idnumber, phoneEt, countryEt, stateEt, cityEt, addressEt;
    private SwitchCompat shopOpenSwitch;
    private Button updateBtn;
    private TextView shopNameEt;


    private FirebaseAuth firebaseAuth;
    private LocationManager locationManager;

    private DatabaseReference reference, reference2;
    private FirebaseUser user;
    private String userID;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_account__ven, container, false);

        //backBtn = view.findViewById(R.id.backBtn);
        gpsBtn = view.findViewById(R.id.gpsBtn);
        profileIv = view.findViewById(R.id.profileIv);
        //nameEt = findViewById(R.id.nameEt);
        //shopNameEt = view.findViewById(R.id.shopNameEt);
        //phoneEt = view.findViewById(R.id.phoneEt);
        //idnumber = view.findViewById(R.id.deliveryFeeEt);
        //countryEt = view.findViewById(R.id.countryEt);
        //stateEt = view.findViewById(R.id.stateEt);
        //cityEt = view.findViewById(R.id.cityEt);
        //addressEt = view.findViewById(R.id.addressEt);
        editProfileBtn = view.findViewById ( R.id.editProfileBtn);

        reviewsBtn= view.findViewById(R.id.reviewsBtn);

        reviewsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ShopReviewsActivity.class);
                intent.putExtra("shopUid",""+firebaseAuth.getUid());
                startActivity(intent);
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference2 = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        final TextView shopNameEt = (TextView) view.findViewById(R.id.shopNameEt);
        final TextView phoneEt = (TextView) view.findViewById(R.id.phoneEt);
        final TextView idnumber = (TextView) view.findViewById(R.id.deliveryFeeEt);
        final TextView countryEt = (TextView) view.findViewById(R.id.countryEt);
        final TextView stateEt = (TextView) view.findViewById(R.id.stateEt);
        final TextView cityEt = (TextView) view.findViewById(R.id.cityEt);
        final TextView addressEt = (TextView) view.findViewById(R.id.addressEt);

        reference2.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Vendor userprofile = snapshot.getValue(Vendor.class);

                String shopname = userprofile.shopName;
                String phoneno = userprofile.phones;
                String Idnumber = userprofile.iDNumber;
                String Country = userprofile.country;
                String county = userprofile.state;
                String City = userprofile.city;
                String Adress = userprofile.address;

                shopNameEt.setText(shopname);
                phoneEt.setText(phoneno);
                idnumber.setText(Idnumber);
                countryEt.setText(Country);
                stateEt.setText(county);
                cityEt.setText(City);
                addressEt.setText(Adress);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileEditSellerActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }






}