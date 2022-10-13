package com.example.jua_kaligo;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

public class AdapterReview extends RecyclerView.Adapter<AdapterReview.HolderReview>{
    private Context context;
    private ArrayList< ModelReview > reviewArrayList;

    public AdapterReview(Context context, ArrayList<ModelReview> reviewArrayList) {
        this.context = context;
        this.reviewArrayList = reviewArrayList;
    }

    @NonNull
    @Override
    public HolderReview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        View view = LayoutInflater.from ( context ).inflate ( R.layout.row_review,parent,false );
        return new HolderReview ( view );
    }

    @Override
    public void onBindViewHolder(@NonNull HolderReview holder, int position) {
        // get data at position
        ModelReview modelReview = reviewArrayList.get (position );
        String uid = modelReview.getUid ();
        String review = modelReview.getReview ();
        String ratings = modelReview.getRatings ();
        String timestamp = modelReview.getTimestamp ();

        //need user info of the person who wrote review

        loadUserDetails(modelReview,holder);

        // convert timestamp to proper format
        Calendar calendar = Calendar.getInstance ();
        calendar.setTimeInMillis ( Long.parseLong ( timestamp ) );
        String dateFormat = DateFormat.format ( "dd/MM/yyyy",calendar ).toString ();

        // set data
        holder.ratingBar.setRating ( Float.parseFloat ( ratings ) );
        holder.reviewTv.setText ( review );
        holder.dateTv.setText ( dateFormat );

    }

    private void loadUserDetails(ModelReview modelReview, HolderReview holder) {
        // uid of user who wrote review
        String uid = modelReview.getUid ();

        DatabaseReference ref = FirebaseDatabase.getInstance ().getReference ("Users");
        ref.child ( uid )
                .addValueEventListener ( new ValueEventListener( ) {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //get user info
                        String name = ""+dataSnapshot.child ( "name" ).getValue ();
                        String profileImage = ""+dataSnapshot.child ( "profileImage" ).getValue ();

                        //set data
                        holder.nameTv.setText ( name );
                        try{
                            Picasso.get ().load(profileImage).placeholder ( R.drawable.ic_baseline_person ).into ( holder.profileIv );
                        }
                        catch(Exception e) {
                            holder.profileIv.setImageResource ( R.drawable.ic_baseline_store_gray );
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                } );
    }

    @Override
    public int getItemCount() {
        return reviewArrayList.size ();
    }

    // view holder class, holds/inits view of recyclerview
    class  HolderReview extends RecyclerView.ViewHolder{
        // ui views of row_review

        private ImageView profileIv;
        private TextView nameTv,dateTv,reviewTv;
        private RatingBar ratingBar;

        public HolderReview(@NonNull View itemView) {
            super(itemView);

            profileIv = itemView.findViewById ( R.id.profileIv );
            nameTv = itemView.findViewById ( R.id.nameTv );
            dateTv = itemView.findViewById ( R.id.dateTv );
            reviewTv = itemView.findViewById ( R.id.reviewTv );
            ratingBar = itemView.findViewById ( R.id.ratingBar );
        }
    }
}
