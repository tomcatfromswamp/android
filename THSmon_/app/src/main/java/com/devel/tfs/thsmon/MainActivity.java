package com.devel.tfs.thsmon;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    TextView out;
    Button btnRefresh;
    String result;
    Sensors sensors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensors);
        System.out.println("oncreate");
        btnRefresh = (Button)findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(oclBtnRefresh);
        sensors = new Sensors();
        sensors.getSensors();
        while(sensors.update == false){
            System.out.println(sensors.update);
        }
        if(sensors.update == true){
            makeTextViews();
        }
    }

    View.OnClickListener oclBtnRefresh = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sensors.getSensors();
        }
    };

    protected void makeTextViews() {
        LinearLayout layout = (LinearLayout)findViewById(R.id.sensorsLL);
        layout.removeAllViewsInLayout();
        TextView updateTimeTV = new TextView(this);
        updateTimeTV.setText(sensors.updateTime);
        updateTimeTV.setGravity(Gravity.CENTER_HORIZONTAL);
        layout.addView(updateTimeTV);
            for (Sensors.Sensor sensor : sensors.sensorsList) {
                LinearLayout sensorView = new LinearLayout(this);
                sensorView.setGravity(Gravity.CENTER_HORIZONTAL);
                sensorView.setOrientation(LinearLayout.VERTICAL);
                sensorView.setId(Integer.parseInt(sensor.gpio));
                TextView title = new TextView(this);
                title.setTypeface(null, Typeface.BOLD);
                title.setText("Датчик: " + sensor.name);
                sensorView.addView(title);
                TextView tv = new TextView(this);
                tv.setText("Температура: " + sensor.currentTemp + "\nВлажность: " + sensor.currentHumi);
                sensorView.addView(tv);
                layout.addView(sensorView);
                Space space = new Space(this);
                space.setMinimumHeight(50);
                layout.addView(space);
            }
        System.out.println(layout.getChildCount());
    }


}