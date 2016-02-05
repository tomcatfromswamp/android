package com.devel.tfs.thsmon;

import android.os.AsyncTask;
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
 * Created by tfs on 03.02.2016.
 */
public class Sensors {

    public ArrayList<Sensor> sensorsList;
    public String updateTime = "test";
    public Boolean update = false;

    public void getSensors(){
        Request request = new Request();
        request.execute();
    }

    class Sensor {
        public String gpio;
        public String name;
        public String currentTemp;
        public String currentHumi;
    }

    class Request extends AsyncTask<Void, Void, Void> {
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            update = false;
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
                sensorsList = new ArrayList<Sensor>();
                int eventType = xpp.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        if (xpp.getName().compareTo("sensor") == 0) {
                            sensorsList.add(new Sensor());
                            sensorsList.get(sensorsList.size()-1).gpio = (xpp.getAttributeValue(null, "gpio"));
                            sensorsList.get(sensorsList.size()-1).name = (xpp.getAttributeValue(null, "name"));
                            sensorsList.get(sensorsList.size()-1).currentTemp = (xpp.getAttributeValue(null, "currentTemp"));
                            sensorsList.get(sensorsList.size()-1).currentHumi = (xpp.getAttributeValue(null, "currentHumi"));
                        }
                        if (xpp.getName().compareTo("update") == 0) {
                            updateTime =  (xpp.getAttributeValue(null, "date")) + " " + (xpp.getAttributeValue(null, "time"));;
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
            update = true;
            super.onPostExecute(result);
        }

    }
}
