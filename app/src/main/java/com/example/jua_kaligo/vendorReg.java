package com.example.jua_kaligo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputEditText;
import com.hbb20.CountryCodePicker;

public class vendorReg extends AppCompatActivity {
    private Spinner spinner, spinner2;
    TextInputEditText text;
    //EditText phone, fullname, IDnumber;
    //CountryCodePicker countryCodePicker;
    //Spinner spinnerLocation, Spinnertypeofbusiness;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_reg);


        spinner = (Spinner) findViewById(R.id.spinner);
        spinner2 = (Spinner) findViewById(R.id.spinner2);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.region, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.business, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);



    }
    public void login(View view)
    {
        //if(!validatePhone()){

        //}


        Intent intent = new Intent(vendorReg.this, VendorInformation.class);
        // passing fields to the next activity
        //intent.putExtra("fullname", fullname);
        // start activity
        startActivity(intent);

    }
}