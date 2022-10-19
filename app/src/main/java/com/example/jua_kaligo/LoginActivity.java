package com.example.jua_kaligo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {
    EditText phone, fullname, IDnumber;
    private ImageView profileIv;
    CountryCodePicker countryCodePicker;

    // variable for FirebaseAuth class
    private FirebaseAuth mAuth;

    // variable for our text input
    // field for phone and OTP.
    private EditText edtPhone, dtOTP;
    private PinView edtOTP;

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
        setContentView(R.layout.activity_login);
        phone = findViewById(R.id.phone_number2);
        fullname = findViewById(R.id.fullnames);
        countryCodePicker = findViewById(R.id.country_code_picker2);
        IDnumber = findViewById(R.id.IDNUMBER1);
        profileIv = findViewById(R.id.profileIv);

        String country_code = countryCodePicker.getDefaultCountryCode().toString();

        // permissions
        locationPermissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        cameraPermissions = new String[]{Manifest.permission.CAMERA , Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Registering");
        progressDialog.setCanceledOnTouchOutside(false);
        edtPhone = findViewById(R.id.phone_number2);
        edtOTP = findViewById(R.id.idEdtOtp2);
        verifyOTPBtn = findViewById(R.id.idBtnVerify2);
        generateOTPBtn = findViewById(R.id.idBtnGetOtp2);
        //setting on click listener

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
                if(TextUtils.isEmpty(edtPhone.getText().toString())){
                    Toast.makeText(LoginActivity.this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
                }else {
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
                if(TextUtils.isEmpty(edtOTP.getText().toString())){
                    Toast.makeText(LoginActivity.this, "Please enter OTP", Toast.LENGTH_SHORT).show();
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

    private void signInWithCredential(PhoneAuthCredential credential){
        // checking if the code entered is correct
        Intent intent = getIntent();
        String fullNames = intent.getStringExtra("FullName");
        String phoneNum = intent.getStringExtra("PhoneNumber");
        String idNum = intent.getStringExtra("ID_NUMBER");
        String Gender = intent.getStringExtra("Location");
        String country = intent.getStringExtra("country");
        String state = intent.getStringExtra("state");
        String city = intent.getStringExtra("city");
        String address = intent.getStringExtra("address");
        String phones = "+254"+phone.getText().toString().trim();
        //String latitude = intent.getStringExtra("latitude");
        //String longitude = intent.getStringExtra("longitude");

        String accountType = "Customer";
        String online= "true";
        String Phone = "+254"+phone.getText().toString().trim();
        String uid= mAuth.getUid();
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    // if code is correct and task is succesful we are sending user to a new activity


                    //Customer customer=new Customer(fullNames,phoneNum,idNum,Location,country,state,city,address,accountType,online,Phone,uid);

                    final String timestamp = "" + System.currentTimeMillis ( );

                    if (image_uri == null){
                        //setup data to save
                        HashMap < String, Object > hashMap = new HashMap <> ( );
                        hashMap.put ( "uid" , "" + mAuth.getUid ( ) );
                        hashMap.put ( "idnumberuser" , "" + idNum );
                        hashMap.put ( "name" , "" + fullNames );
                        hashMap.put ( "phone" , "" + phones );
                        hashMap.put ( "country" , "" + country );
                        hashMap.put ( "city" , "" + city );
                        hashMap.put ( "state" , "" + state );
                        hashMap.put ( "address" , "" + address );
                        hashMap.put ( "genderuser" , "" + Gender );
                        hashMap.put ( "latitude" , "36.8219" );
                        hashMap.put ( "longitude" , "1.2921" );
                        hashMap.put ( "timestamp" , "" + timestamp );
                        hashMap.put ( "accountType" , "Customer" );
                        hashMap.put ( "online" , "true" );
                        hashMap.put ( "profileImage" , "" );

                        DatabaseReference ref = FirebaseDatabase.getInstance ().getReference ("Users");
                        ref.child( mAuth.getUid ( ) ).setValue ( hashMap ).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                progressDialog.setMessage("Creating account...");
                                progressDialog.show();

                                Toast.makeText(LoginActivity.this, "You have been registered successfully ", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(LoginActivity.this,MainScreen.class);
                                startActivity(i);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Failed to Register, Retry", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }else{
                        String filepathAndName = "profile_images/" + "" + mAuth.getUid ();
                        //upload image
                        StorageReference storageReference = FirebaseStorage.getInstance ().getReference (filepathAndName);
                        storageReference.putFile(image_uri)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        // get url of uploaded image
                                        Task <Uri> uriTask = taskSnapshot.getStorage ().getDownloadUrl ();
                                        while (! uriTask.isSuccessful ());
                                        Uri downloadImageUri = uriTask.getResult ();

                                        if (uriTask.isSuccessful ()){
                                            //setup data to save
                                            HashMap < String, Object > hashMap = new HashMap <> ( );
                                            hashMap.put ( "uid" , "" + mAuth.getUid ( ) );
                                            hashMap.put ( "idnumberuser" , "" + idNum );
                                            hashMap.put ( "name" , "" + fullNames );
                                            hashMap.put ( "phone" , "" + phones );
                                            hashMap.put ( "country" , "" + country );
                                            hashMap.put ( "city" , "" + city );
                                            hashMap.put ( "state" , "" + state );
                                            hashMap.put ( "address" , "" + address );
                                            hashMap.put ( "genderuser" , "" + Gender );
                                            hashMap.put ( "latitude" , "36.8219" );
                                            hashMap.put ( "longitude" , "1.2921" );
                                            hashMap.put ( "timestamp" , "" + timestamp );
                                            hashMap.put ( "accountType" , "Customer" );
                                            hashMap.put ( "online" , "true" );
                                            hashMap.put ( "profileImage" , "" +downloadImageUri);

                                            DatabaseReference ref = FirebaseDatabase.getInstance ().getReference ("Users");
                                            ref.child( mAuth.getUid ( ) ).setValue ( hashMap ).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    progressDialog.setMessage("Creating account...");
                                                    progressDialog.show();

                                                    Toast.makeText(LoginActivity.this, "You have been registered successfully ", Toast.LENGTH_SHORT).show();
                                                    Intent i = new Intent(LoginActivity.this,MainScreen.class);
                                                    startActivity(i);
                                                    finish();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(LoginActivity.this, "Failed to Register, Retry", Toast.LENGTH_SHORT).show();
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


//                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(customer).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if(task.isSuccessful()){
//                                progressDialog.setMessage("Creating account...");
//                                progressDialog.show();
//
//                                Toast.makeText(LoginActivity.this, "You have been registered successfully ", Toast.LENGTH_SHORT).show();
//                                Intent i = new Intent(LoginActivity.this,MainScreen.class);
//                                startActivity(i);
//                                finish();
//                                //saveFirebaseData();
//                            }else {
//                                progressDialog.dismiss();
//                                Toast.makeText(LoginActivity.this, "Failed to Register, Retry", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });

                }else {
                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void sendVerificationCode(String number){
        // this method is used for getting
        // OTP on user phone number.
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
        // below method is used when
        // OTP is sent from Firebase
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken){
            super.onCodeSent(s, forceResendingToken);
            // when we receive the OTP it
            // contains a unique id which
            // we are storing in our string
            // which we have already created.
            verificationId = s;
        }
        // this method is called when user
        // receive OTP from Firebase.
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            // below line is used for getting OTP code
            // which is sent in phone auth credentials.
            final String code = phoneAuthCredential.getSmsCode();
            // lets test here
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

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    };
    private void verifyCode(String code){
        // below line is used for getting
        // credentials from our verification id and code.
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

        // after getting credential we are
        // calling sign in method.
        signInWithCredential(credential);
    }

    public void RegisterPage(View view) {
        Intent intent = new Intent(LoginActivity.this, ChooseRole.class);
        startActivity(intent);
    }
}