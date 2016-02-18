package com.devel.tfs.thsmon;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import com.androidplot.util.PixelUtils;
import com.androidplot.xy.CatmullRomInterpolator;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by tfs on 11.02.16.
 */
public class SensorActivity extends AppCompatActivity {

    LinearLayout.LayoutParams params;
    int horisontalSpaceWidth;
    private XYPlot plot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor);
        Intent intent = getIntent();
        Double currentTemp = (Double)intent.getSerializableExtra("currentTemp");
        Double currentHumi = (Double)intent.getSerializableExtra("currentHumi");
        String name = (String)intent.getSerializableExtra("name");
        ArrayList<Double> tempList = (ArrayList<Double>)intent.getSerializableExtra("tempList");
        ArrayList<String> timeList = (ArrayList<String>)intent.getSerializableExtra("timeList");
        setTitle("Датчик: " + name);
        horisontalSpaceWidth = ((this.getWindow().getDecorView().getWidth()*5)/100);
        System.out.println(horisontalSpaceWidth);
        LinearLayout layout = (LinearLayout)findViewById(R.id.sensorMainRL);
        Space spaceStart = new Space(this);
        spaceStart.setMinimumHeight(50);
        layout.addView(spaceStart);
        LinearLayout sensorView = new LinearLayout(this);
        sensorView.setGravity(Gravity.CENTER_HORIZONTAL);
        sensorView.setOrientation(LinearLayout.VERTICAL);
        sensorView.setBackgroundColor(Color.parseColor("#0985ff"));
        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.leftMargin = horisontalSpaceWidth;
        params.rightMargin = horisontalSpaceWidth;
        sensorView.setLayoutParams(params);
        TextView currentData = new TextView(this);
        currentData.setText(Html.fromHtml(currentTemp.toString() + "°C             " + currentHumi.toString() + "%"));
        currentData.setTextSize(32);
        currentData.setGravity(Gravity.CENTER_HORIZONTAL);
        sensorView.addView(currentData);
        layout.addView(sensorView);
        Space space = new Space(this);
        space.setMinimumHeight(50);
        layout.addView(space);
        //
        plot = (XYPlot) findViewById(R.id.plot);
        Number[] series1Numbers = new Number[tempList.size()];
        int i =0;
        for(Double temp : tempList){
            series1Numbers[i]=temp;
            i++;
        }
        XYSeries series1 = new SimpleXYSeries(Arrays.asList(series1Numbers),
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Series1");
        LineAndPointFormatter series1Format = new LineAndPointFormatter();
        series1Format.setPointLabelFormatter(new PointLabelFormatter());
        series1Format.configure(getApplicationContext(),
                R.xml.line_point_formatter_with_labels);
        LineAndPointFormatter series2Format = new LineAndPointFormatter();
        series2Format.setPointLabelFormatter(new PointLabelFormatter());
        series2Format.configure(getApplicationContext(),
                R.xml.line_point_formatter_with_labels_2);
        series2Format.getLinePaint().setPathEffect(
                new DashPathEffect(new float[]{

                        // always use DP when specifying pixel sizes, to keep things consistent across devices:
                        PixelUtils.dpToPix(20),
                        PixelUtils.dpToPix(15)}, 0));

        // just for fun, add some smoothing to the lines:
        // see: http://androidplot.com/smooth-curves-and-androidplot/
        series1Format.setInterpolationParams(
                new CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal));
        series2Format.setInterpolationParams(
                new CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal));
        // add a new series' to the xyplot:
        plot.addSeries(series1, series1Format);
        // reduce the number of range labels
        plot.setTicksPerRangeLabel(3);
        // rotate domain labels 45 degrees to make them more compact horizontally:
        plot.getGraphWidget().setDomainLabelOrientation(-45);
        //
    }
}