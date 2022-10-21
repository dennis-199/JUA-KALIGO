package com.example.jua_kaligo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class BarChartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);
        BarChart barChart = findViewById(R.id.barChart);
        String w = "inProgress";

        ArrayList<BarEntry> orders = new ArrayList<>();
        orders.add(new BarEntry(1f,10f ));
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