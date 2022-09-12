package com.example.jua_kaligo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class Vendornext extends AppCompatActivity {
    private Spinner spinner, spinner2;
    EditText  IDNUMBER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendornext);
        spinner = (Spinner) findViewById(R.id.spinner);
        IDNUMBER = (EditText) findViewById(R.id.IDNUMBER1);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.region, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
    public void nextActivity(View view) {


        //String fullNames = fullnames.getText().toString();
        //String phoneNum = phoneNumber.getText().toString();
        String idNum = IDNUMBER.getText().toString();
        String location = spinner.getSelectedItem().toString();

        if(idNum.isEmpty()){
            IDNUMBER.setError("ID number is required ");
            IDNUMBER.requestFocus();
            return;
        }
        Intent i = new Intent(Vendornext.this, LocationVenActivity.class);
        i.putExtras(getIntent().getExtras());

        i.putExtra("ID_NUMBER",idNum);
        i.putExtra("Location",location);
        startActivity(i);
    }
}