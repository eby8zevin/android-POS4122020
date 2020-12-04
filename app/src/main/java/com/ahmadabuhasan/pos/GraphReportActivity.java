package com.ahmadabuhasan.pos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.ahmadabuhasan.pos.database.DBAccess;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Ahmad Abu Hasan on 4/12/2020
 */

public class GraphReportActivity extends AppCompatActivity {

    BarChart barChart;

    DecimalFormat f;
    int mYear = 2020;
    TextView txtNetSales;
    TextView txtSelectYear;
    TextView txtTotalDiscount;
    TextView txtTotalSales;
    TextView txtTotalTax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_report);

        this.f = new DecimalFormat("#0.00");
        this.barChart = (BarChart) findViewById(R.id.barchart);
        this.txtTotalSales = (TextView) findViewById(R.id.txt_total_sales);
        this.txtSelectYear = (TextView) findViewById(R.id.txt_select_year);
        this.txtTotalTax = (TextView) findViewById(R.id.txt_total_tax);
        this.txtTotalDiscount = (TextView) findViewById(R.id.txt_discount);
        this.txtNetSales = (TextView) findViewById(R.id.txt_net_sales);
        this.barChart.setDrawBarShadow(false);
        this.barChart.setDrawValueAboveBar(true);
        this.barChart.setMaxVisibleValueCount(50);
        this.barChart.setPinchZoom(false);
        this.barChart.setDrawGridBackground(true);
        String currentYear = new SimpleDateFormat("yyyy", Locale.ENGLISH).format(new Date());
        TextView textView = this.txtSelectYear;
        textView.setText(getString(R.string.year) + currentYear);
        getGraphData();
    }

    public void getGraphData() {

        DBAccess databaseAccess = DBAccess.getInstance(this);
        String[] monthNumber = {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            databaseAccess.open();
            String str = monthNumber[i];
            barEntries.add(new BarEntry((float) i, databaseAccess.getMonthlySalesAmount(str, "" + this.mYear)));
        }
        String[] monthList = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        XAxis xAxis = this.barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(monthList));
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1.0f);
        xAxis.setGranularityEnabled(true);
        xAxis.setLabelCount(12);
        BarDataSet barDataSet = new BarDataSet(barEntries, getString(R.string.monthly_sales_report));
        barDataSet.setColors(ColorTemplate.LIBERTY_COLORS);
        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.9f);
        this.barChart.setData(barData);
        this.barChart.setScaleEnabled(false);
        databaseAccess.open();
        String currency = databaseAccess.getCurrency();
        databaseAccess.open();
        double sub_total = databaseAccess.getTotalOrderPrice("YEARLY");
        TextView textView = this.txtTotalSales;
        textView.setText(getString(R.string.total_sales) + currency + this.f.format(sub_total));
        databaseAccess.open();
        double get_tax = databaseAccess.getTotalTax("YEARLY");
        TextView textView2 = this.txtTotalTax;
        StringBuilder sb = new StringBuilder();
        String[] strArr = monthNumber;
        sb.append(getString(R.string.total_tax));
        sb.append("(+) : ");
        sb.append(currency);
        sb.append(this.f.format(get_tax));
        textView2.setText(sb.toString());
        databaseAccess.open();
        double get_discount = databaseAccess.getTotalDiscount("YEARLY");
        TextView textView3 = this.txtTotalDiscount;
        StringBuilder sb2 = new StringBuilder();
        DBAccess databaseAccess2 = databaseAccess;
        sb2.append(getString(R.string.total_discount));
        sb2.append("(-) : ");
        sb2.append(currency);
        sb2.append(this.f.format(get_discount));
        textView3.setText(sb2.toString());
        TextView textView4 = this.txtNetSales;
        ArrayList<BarEntry> arrayList = barEntries;
        StringBuilder sb3 = new StringBuilder();
        String[] strArr2 = monthList;
        sb3.append(getString(R.string.net_sales));
        sb3.append(": ");
        sb3.append(currency);
        sb3.append(this.f.format((sub_total + get_tax) - get_discount));
        textView4.setText(sb3.toString());
    }


}