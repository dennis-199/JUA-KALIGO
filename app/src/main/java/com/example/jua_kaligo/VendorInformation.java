package com.example.jua_kaligo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputEditText;
import com.hbb20.CountryCodePicker;

public class VendorInformation extends AppCompatActivity {
    Spinner spinner, spinner2;
    TextInputEditText text;
    EditText phone, fullname, IDnumber;
    CountryCodePicker countryCodePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_information);
        phone = findViewById(R.id.phone_number);
        fullname = findViewById(R.id.fullName);
        countryCodePicker = findViewById(R.id.country_code_picker);
        IDnumber = findViewById(R.id.idnumber);

        spinner = (Spinner) findViewById(R.id.spinner);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
    }

    public void verifyOTP(View view) {

        // get values passed from previous screen
        String _fullname = getIntent().getStringExtra("fullname");
        String _IDnumber = getIntent().getStringExtra("IDnumber");

        String _getUserenteredphonenumber = phone.getText().toString().trim();
        String _getPhoneNumber = "+"+countryCodePicker.getFullNumber()+_getUserenteredphonenumber;
        Intent intent = new Intent(VendorInformation.this, Verification.class);
        // pass all fields to the next activity
        intent.putExtra("fullname", _fullname);
        intent.putExtra("IDnumber",_IDnumber);
        intent.putExtra("phoneNo",_getPhoneNumber);
        startActivity(intent);


    }
}