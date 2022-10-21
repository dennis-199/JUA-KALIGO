package com.example.jua_kaligo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class BarChartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);
        BarChart barChart = findViewById(R.id.barChart);

        ArrayList<BarEntry> orders = new ArrayList<>();
        orders.add(new BarEntry(1,10 ));
        orders.add(new BarEntry(2,15 ));
        orders.add(new BarEntry(3,60 ));
        orders.add(new BarEntry(4,130 ));
        orders.add(new BarEntry(5,25 ));

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