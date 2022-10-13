package com.example.jua_kaligo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ShopReviewsActivity extends AppCompatActivity {

    //ui views

    private ImageButton backBtn;
    private ImageView profileIv;
    private TextView shopNameTv,ratingsTv;
    private RatingBar ratingBar;
    private RecyclerView reviewsRv;


    private FirebaseAuth firebaseAuth;

    private ArrayList< ModelReview > reviewArrayList;
    private AdapterReview adapterReview;

    private String shopUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_reviews);

        //init ui views

        backBtn = findViewById ( R.id.backBtn );
        profileIv = findViewById ( R.id.profileIv );
        shopNameTv = findViewById ( R.id.shopNameTv );
        ratingsTv = findViewById ( R.id.ratingsTv );
        ratingBar = findViewById ( R.id.ratingBar );
        reviewsRv = findViewById ( R.id.reviewsRv );




        shopUid = getIntent ().getStringExtra ( "shopUid" );


        firebaseAuth = FirebaseAuth.getInstance ();
        loadShopDetails();// for shop name, image and rating
        loadReviews();

        backBtn.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                onBackPressed ();
            }
        } );

    }

    private float ratingSum =0;
    private void loadReviews() {
        //init list
        reviewArrayList = new ArrayList <> (  );
        DatabaseReference ref = FirebaseDatabase.getInstance ().getReference ("Users");
        ref.child ( shopUid ).child ( "Ratings" )
                .addValueEventListener ( new ValueEventListener ( ) {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // clear list before adding data into it
                        reviewArrayList.clear ();
                        ratingSum=0;
                        for(DataSnapshot ds: dataSnapshot.getChildren ()){
                            float rating = Float.parseFloat ( ""+ds.child ( "ratings" ).getValue () );
                            ratingSum = ratingSum+rating;

                            ModelReview modelReview = ds.getValue (ModelReview.class);
                            reviewArrayList.add ( modelReview );

                        }
                        // setup adapter
                        adapterReview = new AdapterReview ( ShopReviewsActivity.this,reviewArrayList );
                        // set to recycler view
                        reviewsRv.setAdapter ( adapterReview );

                        long numberOfReviews = dataSnapshot.getChildrenCount ();
                        float avgRating = ratingSum/numberOfReviews;

                        ratingsTv.setText ( String.format ( "%.2f",avgRating )+"["+numberOfReviews+"]" );
                        ratingBar.setRating ( avgRating );

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                } );
    }

    private void loadShopDetails() {
        DatabaseReference ref = FirebaseDatabase.getInstance ().getReference ("Users");
        ref.child ( shopUid )
                .addValueEventListener ( new ValueEventListener( ) {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String shopName = ""+dataSnapshot.child ( "shopName" ).getValue ();
                        String profileImage = ""+dataSnapshot.child ( "profileImage" ).getValue (  );

                        shopNameTv.setText ( shopName );

                        try{
                            Picasso.get ().load(profileImage).placeholder ( R.drawable.ic_baseline_store_gray ).into ( profileIv );
                        }
                        catch(Exception e) {
                            profileIv.setImageResource ( R.drawable.ic_baseline_store_gray );
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                } );
    }
}