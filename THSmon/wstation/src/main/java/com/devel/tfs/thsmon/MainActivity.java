package com.devel.tfs.thsmon;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ActionMenuView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.sufficientlysecure.htmltextview.HtmlTextView;
import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.EmptyStackException;

public class MainActivity extends AppCompatActivity {
    Button btnRefresh;
    Sensors sensors;
    LinearLayout.LayoutParams params;
    int horisontalSpaceWidth;
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensors);
        System.out.println("oncreate");
        btnRefresh = (Button)findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(oclBtnRefresh);
        Request request = new Request();
        request.execute();
    }

    View.OnClickListener oclBtnRefresh = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Request request = new Request();
            request.execute();
        }
    };

    View.OnClickListener oclSensorView = new View.OnClickListener() {
        @Override
        public  void onClick(View v){
            Intent intent = new Intent(MainActivity.this, SensorActivity.class);
            intent.putExtra("clickSensorId", sensors.clickSensorId);
            intent.putExtra("currentTemp", sensors.sensorsList.get(v.getId()).currentTemp);
            intent.putExtra("currentHumi", sensors.sensorsList.get(v.getId()).currentHumi);
            intent.putExtra("name", sensors.sensorsList.get(v.getId()).name);
            intent.putExtra("timeList", sensors.sensorsList.get(v.getId()).timeList);
            intent.putExtra("tempList", sensors.sensorsList.get(v.getId()).tempList);
            try {
                startActivity(intent);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    };

    protected void makeTextViews() {
        LinearLayout layout = (LinearLayout)findViewById(R.id.mainLL);
        layout.removeAllViewsInLayout();
        horisontalSpaceWidth = ((this.getWindow().getDecorView().getWidth()*5)/100);
        TextView updateLabel = (TextView)findViewById(R.id.lastUpdateTV);
        updateLabel.setText(Html.fromHtml("<small>Данные от: " + sensors.updateTime + "<small>"));
        Space spaceStart = new Space(this);
        spaceStart.setMinimumHeight(50);
        layout.addView(spaceStart);
        count = 0;
        for (Sensors.Sensor sensor : sensors.sensorsList) {
            LinearLayout sensorView = new LinearLayout(this);
            sensorView.setGravity(Gravity.CENTER_HORIZONTAL);
            sensorView.setOrientation(LinearLayout.VERTICAL);
            sensorView.setBackgroundColor(Color.parseColor("#0985ff"));
            params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.leftMargin = horisontalSpaceWidth;
            params.rightMargin = horisontalSpaceWidth;
            sensorView.setLayoutParams(params);
            TextView title = new TextView(this);
            title.setText(Html.fromHtml("<b>Датчик:</b> " + sensor.name));
            title.setGravity(Gravity.CENTER_HORIZONTAL);
            sensorView.addView(title);
            TextView currentData = new TextView(this);
            currentData.setText(Html.fromHtml(sensor.currentTemp.toString() + "°C             " + sensor.currentHumi.toString() + "%"));
            currentData.setTextSize(32);
            currentData.setGravity(Gravity.CENTER_HORIZONTAL);
            sensorView.addView(currentData);
            sensorView.setOnClickListener(oclSensorView);
            sensorView.setId(count);
            layout.addView(sensorView);
            Space space = new Space(this);
            space.setMinimumHeight(50);
            layout.addView(space);
            count++;
        }
        System.out.println(layout.getChildCount());
    }

    class Request extends AsyncTask<Void, Void, Void> {
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            String url = "http://weather-station.x5x.ru:8080/show-xml.jsp";
            BufferedReader inStream = null;
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpRequest = new HttpGet(url);
                HttpResponse response = httpClient.execute(httpRequest);
                inStream = new BufferedReader(
                        new InputStreamReader(
                                response.getEntity().getContent()));

                StringBuffer buffer = new StringBuffer("");
                String line = "";
                String NL = System.getProperty("line.separator");
                while ((line = inStream.readLine()) != null) {
                    buffer.append(line + NL);
                }
                inStream.close();
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                StringReader sw = new StringReader(buffer.toString());
                xpp.setInput(sw);
                int eventType = xpp.getEventType();
                sensors = new Sensors();
                int currentSensor;
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        if (xpp.getName().compareTo("sensor") == 0) {
                            sensors.addSensor();
                            sensors.setGpio(Integer.parseInt(xpp.getAttributeValue(null, "gpio")));
                            sensors.setName(xpp.getAttributeValue(null, "name"));
                            sensors.setCurrentTemp(Double.parseDouble(xpp.getAttributeValue(null, "currentTemp")));
                            sensors.setCurrentHumi(Double.parseDouble(xpp.getAttributeValue(null, "currentHumi")));
                        }
                        if (xpp.getName().compareTo("update") == 0) {
                            sensors.setUpdateTime(xpp.getAttributeValue(null, "date") + " " + xpp.getAttributeValue(null, "time"));
                        }
                        if (xpp.getName().compareTo("values") == 0) {
                            sensors.addTime(xpp.getAttributeValue(null, "time"));
                            sensors.addTemp(Double.parseDouble(xpp.getAttributeValue(null, "temp")));
                        }
                    }
                    eventType = xpp.next();
                }
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            makeTextViews();
        }
    }
}