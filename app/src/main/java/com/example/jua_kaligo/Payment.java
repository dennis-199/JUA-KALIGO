package com.example.jua_kaligo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


//import com.flutterwave.raveandroid.RaveConstants;
import com.flutterwave.raveandroid.RavePayActivity;



//import com.flutterwave.raveandroid.rave_java_commons.RaveConstants;
import com.flutterwave.raveandroid.RaveUiManager;

//import com.flutterwave.raveandroid.RavePayManager;


public class Payment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        //RaveUiManager =

    }

    public void paybutton(View view) {
        makepayment();

    }

    private void makepayment() {
        new RaveUiManager(this)//.setAmount(100)
                .setCurrency("Ksh")
                .setCountry("KEN")
                .setEmail("otieno.dennis strathmore.edu")
                .setfName("Denis")
                .setlName("Otieno")
                .setNarration("Payment of product")
                .setPublicKey("FLWPUBK-02cc2b1445e7627ac7c780cab7f9c9e4-X")
                .setEncryptionKey("746042488465c9e1a2858e38")
               // .setPhoneNumber("+254792935763", true)
                .setTxRef(System.currentTimeMillis()+"Ref")
                .acceptAccountPayments(true)
                .acceptCardPayments(true)
                .acceptMpesaPayments(true)
                .onStagingEnv(true)
                .allowSaveCardFeature(true)

                //.shouldDisplayFee(true)
                //.showStagingLabel(true)

                .initialize();

    }
    //@Override
    /*protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*
         *  We advise you to do a further verification of transaction's details on your server to be
         *  sure everything checks out before providing service or goods.
         */
        /*if (requestCode == RaveConstants.RAVE_REQUEST_CODE && data != null) {
            String message = data.getStringExtra("response");
            if (resultCode == RavePayActivity.RESULT_SUCCESS) {
                Toast.makeText(this, "SUCCESS " + message, Toast.LENGTH_SHORT).show();
            }
            else if (resultCode == RavePayActivity.RESULT_ERROR) {
                Toast.makeText(this, "ERROR " + message, Toast.LENGTH_SHORT).show();
            }
            else if (resultCode == RavePayActivity.RESULT_CANCELLED) {
                Toast.makeText(this, "CANCELLED " + message, Toast.LENGTH_SHORT).show();
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    } */

}