package com.example.jua_kaligo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hbb20.CountryCodePicker;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class VendorInformation extends AppCompatActivity {
    Spinner spinner, spinner2;
    TextInputEditText text;
    ScrollView scrollView;
    EditText phone, fullname, IDnumber;
    CountryCodePicker countryCodePicker;

    private ImageView profileIv;
    // variable for FirebaseAuth class
    private FirebaseAuth mAuth;

    // variable for our text input
    // field for phone and OTP.
    private EditText edtPhone, edtOTP;

    //permission constants
    private static final int LOCATION_REQUEST_CODE = 100;
    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 300;
    //image pick constants
    private static final int IMAGE_PICK_GALLERY_CODE = 400;
    private static final int IMAGE_PICK_CAMERA_CODE = 500;

    //permission arrays
    private String[] locationPermissions;
    private String[] cameraPermissions;
    private String[] storagePermissions;
    //image picked uri
    private Uri image_uri;

    // buttons for generating OTP and verifying OTP
    private Button verifyOTPBtn, generateOTPBtn;

    // string for storing our verification ID
    private String verificationId;

    // pprogress dilaogue
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_information);
        phone = findViewById(R.id.phone_number);

        countryCodePicker = findViewById(R.id.country_code_picker);


        scrollView = findViewById(R.id.sign_up_scrollview);

        profileIv = findViewById(R.id.profileIv);

        spinner = (Spinner) findViewById(R.id.spinner);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        // Turn off phone auth app verification.
        // START
        String country_code = countryCodePicker.getSelectedCountryCode().toString();

        // permissions
        locationPermissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        cameraPermissions = new String[]{Manifest.permission.CAMERA , Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Registering");
        progressDialog.setCanceledOnTouchOutside(false);

        edtPhone = findViewById(R.id.phone_number);
        edtOTP = findViewById(R.id.idEdtOtp);
        verifyOTPBtn = findViewById(R.id.idBtnVerify);
        generateOTPBtn = findViewById(R.id.idBtnGetOtp);
        // setting on click listener
        profileIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pick image
                showImagePickDialog();

            }
        });
        generateOTPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking whether the user has entered his phone number or not
                if (TextUtils.isEmpty(edtPhone.getText().toString())) {
                    // if the OTP text field is empty display a message
                    Toast.makeText(VendorInformation.this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
                } else {
                    // if text field is not empty we are calling our send OTP method
                    String phone = "+254" + edtPhone.getText().toString();
                    sendVerificationCode(phone);
                }
            }
        });
        // Initialize on click listener
        verifyOTPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // validating if the otp text field is empty or not
                if (TextUtils.isEmpty(edtOTP.getText().toString())) {
                    Toast.makeText(VendorInformation.this, "Please enter OTP", Toast.LENGTH_SHORT).show();
                } else {
                    // if otp is not empty call method to verify
                    verifyCode(edtOTP.getText().toString());
                    progressDialog.setMessage("Verifying OTP");
                    progressDialog.show();
                }
            }

        });

    }

    private void showImagePickDialog() {
        //options to display in dialog
        String[] options = {"Camera", "Gallery"};
        //dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Image")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            //camera clicked
                            if (checkCameraPermission()) {
                                //camera permissions allowed
                                pickFromCamera();

                            } else {
                                //not allowed, request
                                requestCameraPermission();

                            }

                        } else {
                            //gallery clicked
                            if (checkStoragePermission()) {
                                //storage permissions allowed
                                pickFromGallery();

                            } else {
                                //not allowed, request
                                requestStoragePermission();

                            }

                        }

                    }
                })
                .show();
    }
    private void pickFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);

    }

    private void pickFromCamera() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp_Image Title");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp_Image Description");

        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);

    }

    private boolean checkStoragePermission(){
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                (PackageManager.PERMISSION_GRANTED);

        return result;
    }

    private void requestStoragePermission(){
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE );


    }
    private boolean checkCameraPermission(){
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                (PackageManager.PERMISSION_GRANTED);

        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                (PackageManager.PERMISSION_GRANTED);

        return result && result1;
    }

    private void requestCameraPermission(){
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE );


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && storageAccepted) {
                        //permission allowed
                        pickFromCamera();


                    } else {
                        //permission denied
                        Toast.makeText(this, "Camera permissions are necessary...", Toast.LENGTH_SHORT).show();

                    }
                }
            }
            break;
            case STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0) {

                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted) {
                        //permission allowed
                        pickFromGallery();

                    } else {
                        //permission denied
                        Toast.makeText(this, "Storage permission is necessary...", Toast.LENGTH_SHORT).show();

                    }
                }
            }
            break;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode==RESULT_OK){
            if (requestCode == IMAGE_PICK_GALLERY_CODE){

                image_uri = data.getData();

                profileIv.setImageURI(image_uri);

            }
            else if (requestCode == IMAGE_PICK_CAMERA_CODE){
                profileIv.setImageURI(image_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        // checking if the code entered is correct
        //mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();

        String shopName = intent.getStringExtra("FullName");
        String kRApin = intent.getStringExtra("PhoneNumber");
        String iDNumber = intent.getStringExtra("ID_NUMBER");
        String gender = intent.getStringExtra("Location");
        String country = intent.getStringExtra("country");
        String state = intent.getStringExtra("state");
        String city = intent.getStringExtra("city");
        String address = intent.getStringExtra("address");
        String accountType = "Vendors";
        String online= "true";
        String phones = "+254"+phone.getText().toString().trim();
        String uid= mAuth.getUid();

        String profileImage = "";
        String shopOpen = "true";
        String deliveryFee = "100";
        final String timestamp = "" + System.currentTimeMillis ( );
        String latitude = intent.getStringExtra("latitude");
        String longitude = intent.getStringExtra("longitude");
        String name = "Dennis Ochieng";
        String email = "Juakalistore@gmail.com";
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    // if code is correct and task is succesful we are sending user to a new activity
                    //Vendor vendor = new Vendor(shopName,kRApin,iDNumber,gender,country,state,city,address,accountType,online,phones,uid,profileImage,shopOpen
                   // ,timestamp,latitude,longitude,deliveryFee,name,email);

                    // testing
                    //setup data to save
                    if (image_uri == null){
                        HashMap < String, Object > hashMap = new HashMap <> ( );
                        hashMap.put ( "uid" , "" + mAuth.getUid ( ) );
                        hashMap.put ( "email" , "Juakali@store.com" );
                        hashMap.put("kRApin",""+kRApin);
                        hashMap.put ( "name" , "Jua store"  );
                        hashMap.put("gender", ""+gender);
                        hashMap.put("iDNumber",""+iDNumber);
                        hashMap.put ( "shopName" , "" + shopName );
                        hashMap.put ( "phones" , "" + phones );
                        hashMap.put ( "deliveryFee" , "100" );
                        hashMap.put ( "country" , "" + country );
                        hashMap.put ( "city" , "" + city );
                        hashMap.put ( "state" , "" + state );
                        hashMap.put ( "address" , "" + address );
                        hashMap.put ( "latitude" , "36.8125" );
                        hashMap.put ( "longitude" , "1.3093");
                        hashMap.put ( "timestamp" , "" + timestamp );
                        hashMap.put ( "accountType" , "Vendors" );
                        hashMap.put ( "online" , "true" );
                        hashMap.put ( "shopOpen" , "true" );
                        hashMap.put ( "profileImage" , "" );



                        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    progressDialog.setMessage("Creating account...");
                                    progressDialog.show();

                                    Toast.makeText(VendorInformation.this, "You have been registered successfully ", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(VendorInformation.this, VendorScreen.class);
                                    startActivity(i);
                                    finish();
                                }else{
                                    progressDialog.dismiss();

                                    Toast.makeText(VendorInformation.this, "Failed to Register, Retry", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }else{
                        String filepathAndName = "profile_images/" + "" + mAuth.getUid ();
                        //upload image
                        StorageReference storageReference = FirebaseStorage.getInstance ().getReference (filepathAndName);
                        storageReference.putFile(image_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // get url of uploaded image
                                Task <Uri> uriTask = taskSnapshot.getStorage ().getDownloadUrl ();
                                while (! uriTask.isSuccessful ());
                                Uri downloadImageUri = uriTask.getResult ();

                                if (uriTask.isSuccessful ()){
                                    HashMap < String, Object > hashMap = new HashMap <> ( );
                                    hashMap.put ( "uid" , "" + mAuth.getUid ( ) );
                                    hashMap.put ( "email" , "Juakali@store.com" );
                                    hashMap.put("kRApin",""+kRApin);
                                    hashMap.put ( "name" , "Jua store"  );
                                    hashMap.put("gender", ""+gender);
                                    hashMap.put("iDNumber",""+iDNumber);
                                    hashMap.put ( "shopName" , "" + shopName );
                                    hashMap.put ( "phones" , "" + phones );
                                    hashMap.put ( "deliveryFee" , "100" );
                                    hashMap.put ( "country" , "" + country );
                                    hashMap.put ( "city" , "" + city );
                                    hashMap.put ( "state" , "" + state );
                                    hashMap.put ( "address" , "" + address );
                                    hashMap.put ( "latitude" , "" + latitude );
                                    hashMap.put ( "longitude" , "" + longitude );
                                    hashMap.put ( "timestamp" , "" + timestamp );
                                    hashMap.put ( "accountType" , "Vendors" );
                                    hashMap.put ( "online" , "true" );
                                    hashMap.put ( "shopOpen" , "true" );
                                    hashMap.put ( "profileImage" , ""+downloadImageUri);

                                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){

                                                Toast.makeText(VendorInformation.this, "You have been registered successfully ", Toast.LENGTH_SHORT).show();
                                                Intent i = new Intent(VendorInformation.this, VendorScreen.class);
                                                startActivity(i);
                                                finish();
                                            }else{
                                                Toast.makeText(VendorInformation.this, "Failed to Register, Retry", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                    }



                } else {
                    Toast.makeText(VendorInformation.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendVerificationCode(String number) {
        // method use on getting OTP FOR USER phonenumber
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)            // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallBack)           // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    // callback method is called on Phone auth provider.

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            // initializing our callbacks for on
            // verification callback method.

            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        // method when otp is sent from firebase

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            // when we receive the OTP it
            // contains a unique id which
            // we are storing in our string
            // which we have already created.
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            // below line is used for getting OTP code
            // which is sent in phone auth credentials.
            final String code = phoneAuthCredential.getSmsCode();

            // checking if the code
            // is null or not.
            if(code != null){
                // if the code is not null then
                // we are setting that code to
                // our OTP edittext field.
                edtOTP.setText(code);

                // after setting this code
                // to OTP edittext field we
                // are calling our verifycode method.
                verifyCode(code);
            }

        }

        // this method is called when firebase doesn't
        // sends our OTP code due to any error or issue.
        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            // displaying error message with firebase exception.
            Toast.makeText(VendorInformation.this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    };
    // below method is use to verify code from Firebase.
    private void verifyCode(String code) {
        // below line is used for getting
        // credentials from our verification id and code.
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // after getting credential we are
        // calling sign in method.
        signInWithCredential(credential);
    }

    //public void verifyOTP(View view) {

        // get values passed from previous screen
        //String _fullname = getIntent().getStringExtra("fullname");
        //String _IDnumber = getIntent().getStringExtra("IDnumber");

        //String _getUserenteredphonenumber = phone.getText().toString().trim();
        //String _getPhoneNumber = "+"+countryCodePicker.getFullNumber()+_getUserenteredphonenumber;
        //Intent intent = new Intent(getApplicationContext(), Verification.class);
        // pass all fields to the next activity
        //intent.putExtra("fullname", _fullname);
        //intent.putExtra("IDnumber",_IDnumber);
        //intent.putExtra("phoneNo",_getPhoneNumber);
        //startActivity(intent);
        // Transition
       // Pair[] pairs = new Pair[1];
        //pairs[0] = new Pair<View, String>(scrollView, "transition_OTP_Screen");
        //if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            //ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(VendorInformation.this, pairs);
            //startActivity(intent, options.toBundle());
       // }else{
          //  startActivity(intent);
       // }
        // Let's start here






    //}
}