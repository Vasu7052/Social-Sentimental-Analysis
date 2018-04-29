package com.androfly.vasupc.androidapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CompareActivity extends AppCompatActivity {

    PieChart pieChart1 , pieChart2 ;
    LineChart lineChart;

    TextView tvTop,tvPie1, tvPie2, tvLine;

    Intent i ;

    String response2 = "" ;
    String response1 = "" ;

    String hashtag1 = "" ;
    String hashtag2 = "" ;

    JSONArray pos_line_data1 , neg_line_data1, pos_line_data2 , neg_line_data2 ;
    double avg_pos1, avg_neg1, avg_pos2, avg_neg2 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);

        pieChart1 = findViewById(R.id.pieChart1);
        pieChart2 = findViewById(R.id.pieChart2);

        lineChart = findViewById(R.id.lineChart);

        tvTop = findViewById(R.id.textTop);
        tvPie1 = findViewById(R.id.textPie1);
        tvPie2 = findViewById(R.id.textPie2);
        tvLine = findViewById(R.id.textLine);

        i = getIntent();
        response2 = i.getStringExtra("Response2");

        hashtag1 = i.getStringExtra("Hashtag1").replace(" " , "");
        hashtag2 = i.getStringExtra("Hashtag2").replace(" " , "") ;

        tvTop.setText("#" + hashtag1+ " vs " + "#" + hashtag2);

        tvPie1.setText("#" + hashtag1 + " Popularity");

        tvPie2.setText("#" + hashtag2+ " Popularity");

        tvLine.setText("Graph Comparison");

        try {
            FileInputStream fis = openFileInput("myfile");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                response1 += line;
            }
            Log.i("COMPARE" , response1);
        }catch (Exception e){
            Log.i("COMPARE" , "Read Error " + e.getMessage());
        }

        try {

            JSONObject obj1 = new JSONObject(response1);
            JSONObject obj2 = new JSONObject(response2);

            avg_pos1 = obj1.getDouble("avg_pos")*100.0;
            avg_neg1 = obj1.getDouble("avg_neg")*100.0;

            avg_pos2 = obj2.getDouble("avg_pos")*100.0;
            avg_neg2 = obj2.getDouble("avg_neg")*100.0;

            pos_line_data1 = obj1.getJSONArray("pos_line_data");
            neg_line_data1 = obj1.getJSONArray("neg_line_data");

            pos_line_data2 = obj2.getJSONArray("pos_line_data");
            neg_line_data2 = obj2.getJSONArray("neg_line_data");

            float[] yData1 = {(float) avg_pos1, (float) avg_neg1};
            float[] yData2 = {(float) avg_pos2, (float) avg_neg2};

            String[] xData = {"Positive Popularity","Negative Popularity" };

            setPieChart(yData1 , xData , "" , ""+(Math.round(avg_pos1))+"%" , pieChart1);
            setPieChart(yData2 , xData , "" , ""+(Math.round(avg_pos2))+"%" , pieChart2);

            setLineChart();


        } catch (Exception e) {
            Log.i("COMPARE" , "Error " + e.toString());
        }

    }

    private void setPieChart(float[] yData, String[] xData , String desc, String centerText, PieChart tempPieChart) {
        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<>();

        for(int i = 0; i < yData.length; i++){
            yEntrys.add(new PieEntry(yData[i] , xData[i]));
        }

        for(int i = 1; i < xData.length; i++){
            xEntrys.add(xData[i]);
        }

        //create the data set
        PieDataSet pieDataSet = new PieDataSet(yEntrys, desc);
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);


        Description description = new Description();
        description.setText("");
        tempPieChart.setDescription(description);
        tempPieChart.setRotationEnabled(true);
        tempPieChart.setUsePercentValues(true);
        tempPieChart.setHoleColor(Color.WHITE);
        //pieChart.setCenterTextColor(Color.BLACK);
        tempPieChart.setHoleRadius(50f);
        tempPieChart.setTransparentCircleAlpha(0);
        tempPieChart.setDrawEntryLabels(false);
        tempPieChart.setHighlightPerTapEnabled(true);

        tempPieChart.setEntryLabelTextSize(10f);

        tempPieChart.setCenterTextSize(25f);
        tempPieChart.setCenterText(centerText);

        //add colors to dataset
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#FF8153"));
        colors.add(Color.parseColor("#4ACAB4"));
        colors.add(Color.parseColor("#878BB6"));
        pieDataSet.setColors(colors);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            tempPieChart.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }


        //create pie data object
        PieData pieData = new PieData(pieDataSet);
        tempPieChart.animateY(1000, Easing.EasingOption.EaseInOutQuad);
        tempPieChart.setData(pieData);
        tempPieChart.invalidate();
    }

    public void setLineChart(){
        LineDataSet set1,set2,set3,set4;
        ArrayList posYVals1 = new ArrayList<>();
        ArrayList negYVals1 = new ArrayList<>();
        ArrayList posYVals2 = new ArrayList<>();
        ArrayList negYVals2 = new ArrayList<>();

        try{
            for (int i = 0 ; i < 10 ; i++){
                posYVals1.add(new Entry(i , pos_line_data1.getInt(i)));
                negYVals1.add(new Entry(i , neg_line_data1.getInt(i)));
                posYVals2.add(new Entry(i , pos_line_data2.getInt(i)));
                negYVals2.add(new Entry(i , neg_line_data2.getInt(i)));
            }
        }catch (Exception e){
            Toast.makeText(this, "Line error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        set1 = new LineDataSet(posYVals1, "1st Positive Line");
        set1.setFillAlpha(110);
        set1.setColor(Color.parseColor("#FF8153"));
        set1.setCircleColor(Color.parseColor("#FF8153"));
        set1.setLineWidth(2f);
        set1.setCircleRadius(4f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setDrawFilled(true);

        set2 = new LineDataSet(negYVals1, "1st Negative Line");
        set2.setFillAlpha(110);
        set2.setColor(Color.parseColor("#4ACAB4"));
        set2.setCircleColor(Color.parseColor("#4ACAB4"));
        set2.setLineWidth(2f);
        set2.setCircleRadius(4f);
        set2.setDrawCircleHole(false);
        set2.setValueTextSize(9f);
        set2.setDrawFilled(true);

        set3 = new LineDataSet(posYVals2, "2nd Positive Line");
        set3.setFillAlpha(110);
        set3.setColor(Color.parseColor("#c86cff"));
        set3.setCircleColor(Color.parseColor("#c86cff"));
        set3.setLineWidth(2f);
        set3.setCircleRadius(4f);
        set3.setDrawCircleHole(false);
        set3.setValueTextSize(9f);
        set3.setDrawFilled(true);

        set4 = new LineDataSet(negYVals2, "2nd Negative Line");
        set4.setFillAlpha(110);
        set4.setColor(Color.parseColor("#7ea9b6"));
        set4.setCircleColor(Color.parseColor("#7ea9b6"));
        set4.setLineWidth(2f);
        set4.setCircleRadius(4f);
        set4.setDrawCircleHole(false);
        set4.setValueTextSize(9f);
        set4.setDrawFilled(true);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        dataSets.add(set2);
        dataSets.add(set3);
        dataSets.add(set4);

        LineData data = new LineData(dataSets);
        lineChart.getRendererXAxis().getPaintAxisLabels().setTextAlign(Paint.Align.CENTER);

        lineChart.setData(data);
    }

}
