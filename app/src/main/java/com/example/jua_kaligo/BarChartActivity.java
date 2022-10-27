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
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class BarChartActivity extends AppCompatActivity {
    //BarChart barChart;
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
        orders.add(new BarEntry(1f,0f ));
        orders.add(new BarEntry(2f,2f ));
        orders.add(new BarEntry(3f,0f ));
        orders.add(new BarEntry(4f,5f ));
        orders.add(new BarEntry(5f,6f ));
        orders.add(new BarEntry(6f,1f));
        orders.add(new BarEntry(7f,12f ));
        orders.add(new BarEntry(8f,5f ));
        orders.add(new BarEntry(9f,12f ));
        orders.add(new BarEntry(10f,15f ));
        orders.add(new BarEntry(11f,5f ));
        orders.add(new BarEntry(12f,0f ));

        BarDataSet barDataSet= new BarDataSet(orders,"Orders");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);

        BarData barData = new BarData(barDataSet);

        // test
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        final String[] labels = new String[] {"Dummy", "Jan", "Feb", "March", "April", "May",
                "June", "July", "Aug", "Sept", "Oct", "Nov", "Dec"};
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        // end

        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.getDescription().setText("Monthly orders");
        barChart.animateY(2000);


    }
}