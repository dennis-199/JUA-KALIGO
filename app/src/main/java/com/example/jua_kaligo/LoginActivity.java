package com.example.jua_kaligo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {
    EditText phone, fullname, IDnumber;
    CountryCodePicker countryCodePicker;

    // variable for FirebaseAuth class
    private FirebaseAuth mAuth;

    // variable for our text input
    // field for phone and OTP.
    private EditText edtPhone, edtOTP;

    // buttons for generating OTP and verifying OTP
    private Button verifyOTPBtn, generateOTPBtn;

    // string for storing our verification ID
    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        phone = findViewById(R.id.phone_number2);
        fullname = findViewById(R.id.fullnames);
        countryCodePicker = findViewById(R.id.country_code_picker2);
        IDnumber = findViewById(R.id.IDNUMBER1);

        String country_code = countryCodePicker.getDefaultCountryCode().toString();


        mAuth = FirebaseAuth.getInstance();
        edtPhone = findViewById(R.id.phone_number2);
        edtOTP = findViewById(R.id.idEdtOtp2);
        verifyOTPBtn = findViewById(R.id.idBtnVerify2);
        generateOTPBtn = findViewById(R.id.idBtnGetOtp2);
        //setting on click listener
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
                }
            }
        });
    }
    private void signInWithCredential(PhoneAuthCredential credential){
        // checking if the code entered is correct
        Intent intent = getIntent();
        String fullNames = intent.getStringExtra("FullName");
        String phoneNum = intent.getStringExtra("PhoneNumber");
        String idNum = intent.getStringExtra("ID_NUMBER");
        String Location = intent.getStringExtra("Location");
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    // if code is correct and task is succesful we are sending user to a new activity

                    Customer customer=new Customer(fullNames,phoneNum,idNum,Location);

                    FirebaseDatabase.getInstance().getReference("Customers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(customer).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(LoginActivity.this, "You have been registered successfully ", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(LoginActivity.this,MainScreen.class);
                                startActivity(i);
                                finish();
                            }else {
                                Toast.makeText(LoginActivity.this, "Failed to Register, Retry", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

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
}