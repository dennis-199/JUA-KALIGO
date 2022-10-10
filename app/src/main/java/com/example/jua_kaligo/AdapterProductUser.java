package com.example.jua_kaligo;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class AdapterProductUser extends RecyclerView.Adapter<AdapterProductUser.HolderProductUser> implements Filterable {

    private Context context;
    public ArrayList<ModelProduct> productsList, filterList;
    private FilterProductUser filter;

    public AdapterProductUser(Context context, ArrayList<ModelProduct> productsList) {
        this.context = context;
        this.productsList = productsList;
        this.filterList = productsList;
    }

    @NonNull
    @Override
    public HolderProductUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate layout
        View view = LayoutInflater.from(context).inflate(R.layout.row_product_user,parent,false);
        return new HolderProductUser(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderProductUser holder, int position) {
        // get data
        // get data
        final ModelProduct modelProduct = productsList.get(position);
        String discountAvailable = modelProduct.getDiscountAvailable ( );
        String discountNote = modelProduct.getDiscountNote ( );
        String discountPrice = modelProduct.getDiscountPrice ( );
        String productId = modelProduct.getProductId ( );
        String productIcon = modelProduct.getProductIcon ( );
        String productTitle = modelProduct.getProductTitle ( );
        String originalPrice = modelProduct.getOriginalPrice ( );
        String productCategory = modelProduct.getProductCategory ( );
        String productDescription = modelProduct.getProductDescription ( );
        String productQuantity = modelProduct.getProductQuantity ( );
        String timestamp = modelProduct.getTimestamp ( );

        // set data
        holder.titleTv.setText ( productTitle );
        holder.discountedNoteTv.setText ( discountNote );
        holder.discountedPriceTv.setText ( "Ksh"+discountPrice );
        holder.originalPriceTv.setText ( "Ksh"+originalPrice );
        holder.descriptionTv.setText ( productDescription );

        if(discountAvailable.equals ( "true" )){
            // product is on discount
            holder.discountedPriceTv.setVisibility ( View.VISIBLE );
            holder.discountedNoteTv.setVisibility ( View.VISIBLE );
            holder.originalPriceTv.setPaintFlags ( holder.originalPriceTv.getPaintFlags () | Paint.STRIKE_THRU_TEXT_FLAG );
        }
        else {
            // product is not on discount
            holder.discountedPriceTv.setVisibility ( View.GONE );
            holder.discountedNoteTv.setVisibility ( View.GONE );
            holder.originalPriceTv.setPaintFlags ( 0 );
        }
        try {
            Picasso.get ().load ( productIcon ).placeholder ( R.drawable.ic_shopping_cart_primary ).into ( holder.productIconIv );
        }
        catch(Exception e){
            holder.productIconIv.setImageResource ( R.drawable.ic_shopping_cart_primary );
        }

        holder.addToCartTv.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                //add product to cart
            }
        } );

        holder.itemView.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                // show product details
               // showQuantityDialog(modelProduct);
            }
        } );

    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    @Override
    public Filter getFilter() {
        if(filter == null) {
            filter = new FilterProductUser ( this,filterList );
        }
        return filter;
    }

    class HolderProductUser extends RecyclerView.ViewHolder{

        //UI Views

        private ImageView productIconIv;
        private TextView discountedNoteTv, titleTv,descriptionTv,addToCartTv,
                discountedPriceTv,originalPriceTv;
        public HolderProductUser(@NonNull View itemView) {
            super(itemView);

            //init views

            productIconIv = itemView.findViewById ( R.id.productIconIv );
            discountedNoteTv = itemView.findViewById ( R.id.discountedNoteTv );
            titleTv = itemView.findViewById ( R.id.titleTv );
            descriptionTv = itemView.findViewById ( R.id.descriptionTv );
            addToCartTv = itemView.findViewById ( R.id.addToCartTv );
            discountedPriceTv = itemView.findViewById ( R.id.discountedPriceTv );
            originalPriceTv = itemView.findViewById ( R.id.originalPriceTv );
        }
    }
}
