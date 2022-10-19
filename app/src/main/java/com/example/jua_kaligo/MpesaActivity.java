package com.example.jua_kaligo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.jua_kaligo.utils.ApiClient;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MpesaActivity extends AppCompatActivity{
    private ApiClient mApiClient;
    private ProgressDialog mProgressDialog;
    private ImageButton backBtn;

    @BindView(R.id.etAmount)
    EditText mAmount;
    @BindView(R.id.etPhone)
    EditText mPhone;
    @BindView(R.id.btnPay)
    Button mPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpesa);

        ButterKnife.bind(this);

        String amount = getIntent().getStringExtra("orderCost");
        mAmount.setText(amount);

        backBtn = findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}