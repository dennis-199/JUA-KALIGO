package com.example.jua_kaligo;

import android.Manifest;
import android.app.ProgressDialog;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;


public class account_Ven extends Fragment {
    private ImageButton backBtn, gpsBtn;
    private ImageView profileIv;
    private EditText nameEt, shopNameEt, deliveryFeeEt, phoneEt, countryEt, stateEt, cityEt, addressEt;
    private SwitchCompat shopOpenSwitch;
    private Button updateBtn;

    private static final int LOCATION_REQUEST_CODE = 100;
    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 300;

    private static final int IMAGE_PICK_GALLERY_CODE = 400;
    private static final int IMAGE_PICK_CAMERA_CODE = 500;

    private String[] locationPermission;
    private String[] cameraPermission;
    private String[] storagePermission;

    private Uri image_uri;

    private double latitude = 0.0;
    private double longitude = 0.0;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private LocationManager locationManager;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_account__ven, container, false);

        //backBtn = view.findViewById(R.id.backBtn);
        gpsBtn = view.findViewById(R.id.gpsBtn);
        profileIv = view.findViewById(R.id.profileIv);
        //nameEt = findViewById(R.id.nameEt);
        shopNameEt = view.findViewById(R.id.shopNameEt);
        phoneEt = view.findViewById(R.id.phoneEt);
        deliveryFeeEt = view.findViewById(R.id.deliveryFeeEt);
        countryEt = view.findViewById(R.id.countryEt);
        stateEt = view.findViewById(R.id.stateEt);
        cityEt = view.findViewById(R.id.cityEt);
        addressEt = view.findViewById(R.id.addressEt);
        shopOpenSwitch = view.findViewById(R.id.shopOpenSwitch);
        updateBtn = view.findViewById(R.id.updateBtn);

        locationPermission = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        firebaseAuth = FirebaseAuth.getInstance();

        return view;
    }
}