package com.devel.tfs.arduinoths;

import android.os.AsyncTask;
import android.widget.TextView;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;

/**
 * Created by tfs on 02.02.2016.
 */
public class getRequest extends AsyncTask<TextView, Void, String> {
    TextView t;
    String result = "";
    private ArrayList<Sensor> sensorList = new ArrayList<Sensor>();

    @Override
    protected String doInBackground(TextView... params) {
        this.t = params[0];
        return GetSomething();
    }

    final String GetSomething()
    {
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
            //result = buffer.toString();
            StringReader sw = new StringReader(buffer.toString());
            xpp.setInput(sw);
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    if (xpp.getName().compareTo("sensor") == 0) {
                        sensorList.add(new Sensor());
                        sensorList.get(sensorList.size()-1).gpio = (xpp.getAttributeValue(null, "gpio"));
                        sensorList.get(sensorList.size()-1).name = (xpp.getAttributeValue(null, "name"));
                        sensorList.get(sensorList.size()-1).currentTemp = (xpp.getAttributeValue(null, "currentTemp"));
                        sensorList.get(sensorList.size()-1).currentHumi = (xpp.getAttributeValue(null, "currentHumi"));
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
        for (Sensor sensor : sensorList){
            result = result + sensor.gpio;
        }
        return result;
    }

    protected void onPostExecute(String page)
    {
        t.setText(page);
    }
}
