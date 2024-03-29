package com.example.jua_kaligo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import p32929.androideasysql_library.Column;
import p32929.androideasysql_library.EasyDB;

public class AdapterCartItem extends RecyclerView.Adapter<AdapterCartItem.HolderCartItem>{

    private Context context;
    private ArrayList<ModelCartItem> cartItems;

    public AdapterCartItem(Context context, ArrayList<ModelCartItem> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public HolderCartItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate layout row_cartitem.xml
        View view = LayoutInflater.from(context).inflate(R.layout.row_cartitem, parent,false);
        return new HolderCartItem(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderCartItem holder, @SuppressLint("RecyclerView") int position) {
        //get data
        ModelCartItem modelCartItem = cartItems.get(position);
        String id = modelCartItem.getId();
        String getpId = modelCartItem.getpId();
        String title = modelCartItem.getName();
        final String cost = modelCartItem.getCost();
        String price = modelCartItem.getPrice();
        String quantity = modelCartItem.getQuantity();

        // set data
        holder.itemTitleIv.setText(""+title);
        holder.itemPriceIv.setText(""+cost);
        holder.itemQuantityTv.setText("["+quantity+"]");
        holder.itemPriceEachIv.setText(""+price);

        holder.itemRemoveTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EasyDB easyDB =  EasyDB.init(context,"ITEMS_DB")
                        .setTableName("ITEMS_TABLE")
                        .addColumn(new Column("Item_Id", new String[]{"text","unique"}))
                        .addColumn(new Column("Item_PID", new String[]{"text","not null"}))
                        .addColumn(new Column("Item_Name", new String[]{"text","not null"}))
                        .addColumn(new Column("Item_Price_Each", new String[]{"text","not null"}))
                        .addColumn(new Column("Item_Price", new String[]{"text","not null"}))
                        .addColumn(new Column("Item_Quantity", new String[]{"text","not null"}))
                        .doneTableColumn();

                easyDB.deleteRow(1,id);
                Toast.makeText(context, "Removed from cart...",Toast.LENGTH_SHORT).show();

                cartItems.remove(position);
                notifyItemChanged(position);
                notifyDataSetChanged();

                //adjust the subtotal after product remove
                double subTotalWithoutDiscount = ((ShopDetailsActivity)context).allTotalPrice;
                double subTotalAfterProductRemove = subTotalWithoutDiscount - Double.parseDouble(cost.replace("ksh",""));
                ((ShopDetailsActivity)context).allTotalPrice = subTotalAfterProductRemove;
                ((ShopDetailsActivity)context).sTotalTv.setText("ksh" + String.format("%.2f" ,((ShopDetailsActivity)context).allTotalPrice));

                // once subtotal updated ... check minimum order price of promo code
                double promoPrice = Double.parseDouble(((ShopDetailsActivity)context).promoPrice);
                double deliveryFee = Double.parseDouble(((ShopDetailsActivity)context).deliveryFee.replace("ksh",""));

                // check if promo code applied
                if(((ShopDetailsActivity)context).isPromoCodeApplied){
                    // applied
                    if (subTotalAfterProductRemove < Double.parseDouble(((ShopDetailsActivity)context).promoMinimumOrderPrice)){
                        // current order price is less than minimum required price
                        Toast.makeText(context, "This code is valid for order with minimum amount: ksh"+((ShopDetailsActivity)context).promoMinimumOrderPrice, Toast.LENGTH_SHORT).show();
                        ((ShopDetailsActivity)context).applyBtn.setVisibility(View.GONE);
                        ((ShopDetailsActivity)context).promoDescriptionTv.setVisibility(View.GONE);
                        ((ShopDetailsActivity)context).promoDescriptionTv.setText("");
                        ((ShopDetailsActivity)context).discountTv.setText("ksh0");
                        ((ShopDetailsActivity)context).isPromoCodeApplied = false;
                        // show new net total after delivery fee
                        ((ShopDetailsActivity)context).allTotalPriceTv.setText("ksh"+String.format("%.2f", Double.parseDouble(String.format("%.2f", subTotalAfterProductRemove +deliveryFee))));
                    }else {
                        ((ShopDetailsActivity)context).applyBtn.setVisibility(View.VISIBLE);
                        ((ShopDetailsActivity)context).promoDescriptionTv.setVisibility(View.VISIBLE);
                        ((ShopDetailsActivity)context).promoDescriptionTv.setText(((ShopDetailsActivity)context).promoDescription);
                        // show new total price after adding delivery fee and subtracting promo
                        ((ShopDetailsActivity)context).isPromoCodeApplied = true;
                        ((ShopDetailsActivity)context).allTotalPriceTv.setText("ksh"+String.format("%.2f", Double.parseDouble(String.format("%.2f", subTotalAfterProductRemove +deliveryFee - promoPrice))));
                    }
                }else {
                    // not applied
                    ((ShopDetailsActivity)context).allTotalPriceTv.setText("ksh"+String.format("%.2f",Double.parseDouble(String.format("%.2f",subTotalAfterProductRemove +deliveryFee))));

                }
                // after removing item from cart, update cart count
                ((ShopDetailsActivity)context).cartCount();
            }
        });

    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    // view holder class
    class HolderCartItem extends RecyclerView.ViewHolder{

        // ui views of raw cart.xml

        private TextView itemTitleIv, itemPriceIv, itemPriceEachIv, itemQuantityTv, itemRemoveTv;

        public HolderCartItem(@NonNull View itemView) {
            super(itemView);

            itemTitleIv = itemView.findViewById(R.id.itemTitleIv);
            itemPriceIv = itemView.findViewById(R.id.itemPriceIv);
            itemPriceEachIv = itemView.findViewById(R.id.itemPriceEachIv);
            itemQuantityTv = itemView.findViewById(R.id.itemQuantityTv);
            itemRemoveTv = itemView.findViewById(R.id.itemRemoveTv);
        }
    }
}
