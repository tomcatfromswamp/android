package com.devel.tfs.thermometer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private static final int W = 200;
    private static final int H = 2 * W;

    public ThermometerDemo(double value) {
        this.setLayout(new GridLayout());
        DefaultValueDataset dataset = new DefaultValueDataset(value);
        ThermometerPlot plot = new ThermometerPlot(dataset);
        plot.setSubrangePaint(0, Color.green.darker());
        plot.setSubrangePaint(1, Color.orange);
        plot.setSubrangePaint(2, Color.red.darker());
        JFreeChart chart = new JFreeChart("Demo",
                JFreeChart.DEFAULT_TITLE_FONT, plot, true);
        this.add(new ChartPanel(chart, W, H, W, H, W, H,
                false, true, true, true, true, true));
    }
}
