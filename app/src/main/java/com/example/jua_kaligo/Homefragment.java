package com.example.jua_kaligo;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import p32929.androideasysql_library.EasyDB;


public class Homefragment extends Fragment {
    private static final String TAG = "RecyclerViewFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;
    private static final int DATASET_COUNT = 60;

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected LayoutManagerType mCurrentLayoutManagerType;

    protected RadioButton mLinearLayoutRadioButton;
    protected RadioButton mGridLayoutRadioButton;

    protected RecyclerView mRecyclerView;
    protected Adapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected String[] mDataset;



    // declare UI views

    private ImageView shopIv;
    private TextView shopNameTv, phoneTv,emailTv, openClosedTv,
            deliveryFeeTv, addressTv, filteredProductsTv, cartCountTv;
    private ImageButton callBtn, mapBtn,cartBtn,backBtn,filterProductsBtn,reviewBtn;
    private EditText searchProductEt;
    private RecyclerView productsRv;
    private RatingBar ratingBar;

    private String shopUid;
    private String myLatitude, myLongitude, myPhone;
    private String shopName, shopEmail, shopPhone, shopAddress, shopLatitude, shopLongitude;
    public String deliveryFee;

    private FirebaseAuth firebaseAuth;
    private EasyDB easyDB;

    //progress dialog
    private ProgressDialog progressDialog;
    private AdapterProductUser adapterProductUser;
    private ArrayList< ModelProduct > productsList;

    private ArrayList<ModelCartItem> cartItemsList;
    private AdapterCartItem adapterCartItem;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_homefragment, container, false);

        //init UI views

        shopIv = rootView.findViewById ( R.id.shopIv );
        shopNameTv = rootView.findViewById ( R.id.shopNameTv );
        phoneTv = rootView.findViewById ( R.id.phoneTv );
        emailTv = rootView.findViewById ( R.id.emailTv );
        openClosedTv = rootView.findViewById ( R.id.openClosedTv );
        deliveryFeeTv = rootView.findViewById ( R.id.deliveryFeeTv );
        addressTv = rootView.findViewById ( R.id.addressTv );
        callBtn = rootView.findViewById ( R.id.callBtn );
        mapBtn = rootView.findViewById ( R.id.mapBtn );
        cartBtn = rootView.findViewById ( R.id.cartBtn );
        backBtn = rootView.findViewById ( R.id.backBtn );
        searchProductEt = rootView.findViewById ( R.id.searchProductEt );
        filterProductsBtn = rootView.findViewById ( R.id.filterProductsBtn );
        filteredProductsTv = rootView.findViewById ( R.id.filteredProductsTv );
        productsRv = rootView.findViewById ( R.id.productsRv );
        cartCountTv = rootView.findViewById ( R.id.cartCountTv );
        reviewBtn = rootView.findViewById ( R.id.reviewBtn );
        ratingBar = rootView.findViewById ( R.id.ratingBar );

        //init progress dialog
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        //shopUid = getIntent ().getStringExtra ( "shopUid" );
        firebaseAuth = FirebaseAuth.getInstance ();








        return rootView;
    }


}