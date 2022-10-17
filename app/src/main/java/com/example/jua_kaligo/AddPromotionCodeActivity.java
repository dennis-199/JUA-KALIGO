package com.example.jua_kaligo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AddPromotionCodeActivity extends AppCompatActivity {
    private ImageButton backBtn;
    private EditText promoCodeEt, promoDescriptionEt, promoPriceEt, minimumOrderPriceEt;
    private TextView expireDateTv,titleTv;
    private Button addBtn;

    // firebase auth
    FirebaseAuth firebaseAuth;
    // progress dialog
    ProgressDialog progressDialog;

    private String promoId;
    private boolean isUpdating = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_promotion_code);

        // init ui views
        backBtn = findViewById(R.id.backBtn);
        promoCodeEt = findViewById(R.id.promoCodeEt);
        promoDescriptionEt = findViewById(R.id.promoDescriptionEt);
        promoPriceEt = findViewById(R.id.promoPriceEt);
        minimumOrderPriceEt = findViewById(R.id.minimumOrderPriceEt);
        expireDateTv = findViewById(R.id.expireDateTv);
        addBtn = findViewById(R.id.addBtn);
        titleTv = findViewById(R.id.titleTv);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        // get promo id from intent
        Intent intent = getIntent();
        if(intent.getStringExtra("promoId")!= null){
            // update record
            promoId = intent.getStringExtra("promoId");

            titleTv.setText("Update promotion Code");
            addBtn.setText("Update");

            isUpdating = true;

            loadpromoInfo(); // load promotion code info to set in our vies so we can also update single values


        }else {
            // came here from promo codes list activity to add new promo code
            titleTv.setText("Add promotion Code");
            addBtn.setText("Add");

            isUpdating = false;

        }

        // handle click go back
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        // handle click, pick date
        expireDateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog();

            }
        });
        // handle click , add promotion code to firebase db
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputData();

            }
        });
    }

    private void loadpromoInfo() {
        // db path to promo code
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid()).child("Promotions").child(promoId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // get info of promo
                        String id = ""+snapshot.child("id").getValue();
                        String timestamp = ""+snapshot.child("timestamp").getValue();
                        String description = ""+snapshot.child("description").getValue();
                        String promoCode = ""+snapshot.child("promoCode").getValue();
                        String promoPrice = ""+snapshot.child("promoPrice").getValue();
                        String minimumOrderPrice = ""+snapshot.child("minimumOrderPrice").getValue();
                        String expireDate = ""+snapshot.child("expireDate").getValue();

                        // set data
                        promoCodeEt.setText(promoCode);
                        promoDescriptionEt.setText(description);
                        promoPriceEt.setText(promoPrice);
                        minimumOrderPriceEt.setText(minimumOrderPrice);
                        expireDateTv.setText(expireDate);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    private void datePickerDialog() {
        // get current date set on calender
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        // Date picker dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                DecimalFormat mFormat = new DecimalFormat("00");
                String pDay = mFormat.format(dayOfMonth);
                String pMonth = mFormat.format(monthOfYear+1);
                String pYear = ""+year;
                String pDate = pDay + "/"+pMonth+"/"+pYear;// eg 27/04/2022

                expireDateTv.setText(pDate);


            }
        }, mYear, mMonth, mDay);
        // show dialog
        datePickerDialog.show();
        // disable past days section
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);

    }
    private String description, promoCode, promoPrice, minimumOrderPrice, expireDate;
    private void inputData() {
        // input data
        promoCode = promoCodeEt.getText().toString().trim();
        description = promoDescriptionEt.getText().toString().trim();
        promoPrice = promoPriceEt.getText().toString().trim();
        minimumOrderPrice = minimumOrderPriceEt.getText().toString().trim();
        expireDate = expireDateTv.getText().toString().trim();

        // validate form data
        if(TextUtils.isEmpty(promoCode)){
            Toast.makeText(this, "Enter discount code...", Toast.LENGTH_SHORT).show();
            return;// don't proceed further
        }
        if(TextUtils.isEmpty(description)){
            Toast.makeText(this, "Enter description...", Toast.LENGTH_SHORT).show();
            return;// don't proceed further
        }
        if(TextUtils.isEmpty(promoPrice)){
            Toast.makeText(this, "Enter promotion price ..", Toast.LENGTH_SHORT).show();
            return;// don't proceed further
        }
        if(TextUtils.isEmpty(minimumOrderPrice)){
            Toast.makeText(this, "Enter minimum order price  ..", Toast.LENGTH_SHORT).show();
            return;// don't proceed further
        }
        if(TextUtils.isEmpty(expireDate)){
            Toast.makeText(this, "Choose expired date  ..", Toast.LENGTH_SHORT).show();
            return;// don't proceed further
        }

        // all fields entered add to db
        if(isUpdating){
            // update data
            updateDataToDb();

        }else {
            // add data
            addDataToDb();

        }

    }

    private void updateDataToDb() {
        progressDialog.setMessage("Updating Promotion Code...");
        progressDialog.show();

        // setup data to add to db
        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("description", ""+ description);
        hashMap.put("promoCode", ""+ promoCode);
        hashMap.put("promoPrice", ""+ promoPrice);
        hashMap.put("minimumOrderPrice", ""+ minimumOrderPrice);
        hashMap.put("expireDate", ""+ expireDate);

        // init db reference Users > current user > promotions > promo id > promo date
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid()).child("Promotions").child(promoId)
                .updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        // updated
                        progressDialog.dismiss();
                        Toast.makeText(AddPromotionCodeActivity.this, "Updated...", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // failed to update
                        progressDialog.dismiss();
                        Toast.makeText(AddPromotionCodeActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void addDataToDb() {
        // all fields entered , show progress bar
        progressDialog.setMessage("Adding Promotion Code...");
        progressDialog.show();

        String timestamp = ""+System.currentTimeMillis();
        // setup data to add to db
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id",""+timestamp);
        hashMap.put("timestamp", ""+ timestamp);
        hashMap.put("description", ""+ description);
        hashMap.put("promoCode", ""+ promoCode);
        hashMap.put("promoPrice", ""+ promoPrice);
        hashMap.put("minimumOrderPrice", ""+ minimumOrderPrice);
        hashMap.put("expireDate", ""+ expireDate);

        // init db reference Users > current user > promotions > promo id > promo date
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid()).child("Promotions").child(timestamp)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        // code added
                        progressDialog.dismiss();
                        Toast.makeText(AddPromotionCodeActivity.this, "Promotion code added... ", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // adding code failed
                        progressDialog.dismiss();
                        Toast.makeText(AddPromotionCodeActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

}