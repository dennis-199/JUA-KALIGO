package com.example.jua_kaligo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;


public class MainActivity extends AppCompatActivity {
    private Spinner spinner, spinner2;
    private TextView typeofBusiness, idnumber;
    private MaterialButton customerB, vendorB;

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

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.region, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }


    public void testing(View view) {
        Intent intent= new Intent(this,LoginActivity.class);
        startActivity(intent);
    }
}