package com.example.jua_kaligo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;


public class MainActivity extends AppCompatActivity {
    private Spinner spinner, spinner2;
    private TextView typeofBusiness, idnumber;
    private MaterialButton customerB, vendorB;
    EditText fullnames, phoneNumber, IDNUMBER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner = (Spinner) findViewById(R.id.spinner);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        idnumber = findViewById(R.id.idnumber);
        typeofBusiness = findViewById(R.id.typeofbusiness);
        customerB = findViewById(R.id.customerButton);
        vendorB = findViewById(R.id.vendorButton);

        // call
        fullnames = (EditText) findViewById(R.id.fullnames);
        phoneNumber = (EditText) findViewById(R.id.phoneNumber);
        //IDNUMBER = (EditText) findViewById(R.id.IDNUMBER1);

        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.region, android.R.layout.simple_spinner_item);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinner.setAdapter(adapter);

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
        Intent intent= new Intent(MainActivity.this,CustomerNext.class);
        intent.putExtra("FullName", fullNames);
        intent.putExtra("PhoneNumber",phoneNum);
        //intent.putExtra("ID_NUMBER",idNum);
        //intent.putExtra("Location",location);
        startActivity(intent);
    }
}