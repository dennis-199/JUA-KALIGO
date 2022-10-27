package com.example.jua_kaligo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class BarChartActivity extends AppCompatActivity {
    BarChart barChart;
    private ProgressDialog progressDialog;
    private ImageButton backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);
        BarChart barChart = findViewById(R.id.barChart);
        progressDialog = new ProgressDialog ( this );
        progressDialog.setTitle ( "Please Wait" );
        progressDialog.setCanceledOnTouchOutside ( false );

        backBtn = findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });


//        ArrayList<PieEntry> yValues = new ArrayList<>();
//        //read = progress;
//
//
//        yValues.add(new PieEntry(20f, "Completed"));
//        yValues.add(new PieEntry(30f, "In Progress"));
//        yValues.add(new PieEntry(24f, "Cancelled"));
//        barChart.setUsePercentValues(true);
//        pieChart.getDescription().setEnabled(false);
//        pieChart.setExtraOffsets(5,10,5,5);
//
//        pieChart.setDragDecelerationFrictionCoef(0.9f);
//
//        pieChart.setDrawHoleEnabled(true);
//        pieChart.setHoleColor(Color.WHITE);
//        pieChart.setTransparentCircleRadius(31f);

        ArrayList<BarEntry> orders = new ArrayList<>();
        orders.add(new BarEntry(1f,10f,"Jan" ));
        orders.add(new BarEntry(2f,15f ));
        orders.add(new BarEntry(3f,60f ));
        orders.add(new BarEntry(4f,130f ));
        orders.add(new BarEntry(5f,25f ));
        orders.add(new BarEntry(6f,120f ));
        orders.add(new BarEntry(7f,125f ));

        BarDataSet barDataSet= new BarDataSet(orders,"Orders");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);

        BarData barData = new BarData(barDataSet);

        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.getDescription().setText("Bar chart example");
        barChart.animateY(2000);


    }
}