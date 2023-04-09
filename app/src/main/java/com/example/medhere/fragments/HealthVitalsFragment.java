package com.example.medhere.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.medhere.R;
import com.example.medhere.utils.ViewAnimation;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;

public class HealthVitalsFragment extends Fragment {

    private FloatingActionButton addVital, startSession, addFab;
    private final BarChart[] charts = new BarChart[6];
    private boolean isRotate = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_health_vitals, null);

        addVital = v.findViewById(R.id.add_fab);
        startSession = v.findViewById(R.id.new_session);
        addFab = v.findViewById(R.id.calls_fab);
        ViewAnimation.init(addVital);
        ViewAnimation.init(startSession);

        charts[0] = v.findViewById(R.id.chart1);
        charts[1] = v.findViewById(R.id.chart2);
        charts[2] = v.findViewById(R.id.chart3);
        charts[3] = v.findViewById(R.id.chart4);
        charts[4] = v.findViewById(R.id.chart5);
        charts[5] = v.findViewById(R.id.chart6);

        ArrayList NoOfEmp = new ArrayList();

        NoOfEmp.add(new BarEntry(945f, 0));
        NoOfEmp.add(new BarEntry(1040f, 1));
        NoOfEmp.add(new BarEntry(1133f, 2));
        NoOfEmp.add(new BarEntry(1240f, 3));
        NoOfEmp.add(new BarEntry(1369f, 4));
        NoOfEmp.add(new BarEntry(1487f, 5));
        NoOfEmp.add(new BarEntry(1501f, 6));
        NoOfEmp.add(new BarEntry(1645f, 7));
        NoOfEmp.add(new BarEntry(1578f, 8));
        NoOfEmp.add(new BarEntry(1695f, 9));

        ArrayList year = new ArrayList();

        year.add("2008");
        year.add("2009");
        year.add("2010");
        year.add("2011");
        year.add("2012");
        year.add("2013");
        year.add("2014");
        year.add("2015");
        year.add("2016");
        year.add("2017");

        BarDataSet bardataset = new BarDataSet(NoOfEmp, "Vitals");
        charts[0].animateY(5000);
        charts[0].getXAxis().setValueFormatter(new IndexAxisValueFormatter(year));
        BarData data = new BarData(bardataset);
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        charts[0].setData(data);

        charts[1].animateY(5000);
        charts[1].getXAxis().setValueFormatter(new IndexAxisValueFormatter(year));
        BarData data2 = new BarData(bardataset);        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        charts[1].setData(data2);

        charts[2].animateY(5000);
        charts[2].getXAxis().setValueFormatter(new IndexAxisValueFormatter(year));
        BarData data3 = new BarData(bardataset);        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        charts[2].setData(data3);

        charts[3].animateY(5000);
        charts[3].getXAxis().setValueFormatter(new IndexAxisValueFormatter(year));
        BarData data4 = new BarData(bardataset);        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        charts[3].setData(data4);

        charts[4].animateY(5000);
        charts[4].getXAxis().setValueFormatter(new IndexAxisValueFormatter(year));
        BarData data5 = new BarData(bardataset);        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        charts[4].setData(data5);

        charts[5].animateY(5000);
        charts[5].getXAxis().setValueFormatter(new IndexAxisValueFormatter(year));
        BarData data6 = new BarData(bardataset);        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        charts[5].setData(data6);

        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRotate = ViewAnimation.rotateFab(v, !isRotate);
                if (isRotate) {
                    ViewAnimation.showIn(addVital);
                    ViewAnimation.showIn(startSession);
                } else {
                    ViewAnimation.showOut(addVital);
                    ViewAnimation.showOut(startSession);
                }
            }
        });

        return v;
    }
}