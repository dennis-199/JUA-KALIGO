package com.example.jua_kaligo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.chaos.view.PinView;

public class Verification extends AppCompatActivity {
    PinView pinfromUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        pinfromUser = findViewById(R.id.pin_view);

        String _phoneNo = getIntent().getStringExtra("phoneNo");

        sendVerificationCodeToUser(_phoneNo);
    }

    private void sendVerificationCodeToUser(String phoneNo) {

    }
}