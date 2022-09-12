package com.example.jua_kaligo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

public class ChooseRole extends AppCompatActivity {
    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_role);


    }

    public void customermainActivity(View view) {
        Intent intent = new Intent(ChooseRole.this, MainActivity.class);
        startActivity(intent);


    }

    public void vendorsMainactivity(View view) {
        Intent intent = new Intent(ChooseRole.this, vendornames.class);
        startActivity(intent);
    }
}