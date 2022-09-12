package com.example.jua_kaligo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class vendornames extends AppCompatActivity {
    EditText fullnames, phoneNumber, IDNUMBER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendornames);

        // call
        fullnames = (EditText) findViewById(R.id.fullnames);
        phoneNumber = (EditText) findViewById(R.id.phoneNumber);
    }
    public void testing(View view) {
        String fullNames = fullnames.getText().toString();
        String phoneNum = phoneNumber.getText().toString();
        //String idNum = IDNUMBER.getText().toString();
        //String location = spinner.getSelectedItem().toString();

        if(fullNames.isEmpty()){
            fullnames.setError("Fullname is required ");
            fullnames.requestFocus();
            return;
        }
        if(phoneNum.isEmpty()){
            phoneNumber.setError("Phone number is required ");
            phoneNumber.requestFocus();
            return;
        }
        //if(idNum.isEmpty()){
        //IDNUMBER.setError("ID number is required ");
        //IDNUMBER.requestFocus();
        //return;
        //}
        // move to next activity
        Intent intent= new Intent(vendornames.this,Vendornext.class);
        intent.putExtra("FullName", fullNames);
        intent.putExtra("PhoneNumber",phoneNum);
        //intent.putExtra("ID_NUMBER",idNum);
        //intent.putExtra("Location",location);
        startActivity(intent);
    }
}