package com.devel.tfs.thsmon;

import java.util.ArrayList;

/**
 * Created by tfs on 03.02.2016.
 */
public class Sensors {

    protected String updateTime;
    protected ArrayList<Sensor> sensorsList = new ArrayList<Sensor>();
    protected int clickSensorId;

    public void setUpdateTime(String time){
        updateTime = time;
    }

    public void addSensor(){
        sensorsList.add(new Sensor());
    }

    public int getLastSensor(){
        return sensorsList.size()-1;
    }

    public void setClickSensorId(int id){
        clickSensorId = id;
    }

    public void setGpio(int gpio){
        if(sensorsList.size() > 0) {
            sensorsList.get(getLastSensor()).gpio = gpio;
        }
    }

    public void setName(String name){
        if(sensorsList.size() > 0) {
            sensorsList.get(getLastSensor()).name = name;
        }
    }
    public void setCurrentTemp(double currentTemp){
        if(sensorsList.size() > 0) {
            sensorsList.get(getLastSensor()).currentTemp = currentTemp;
        }
    }

    public void setCurrentHumi(double currentHumi){
        if(sensorsList.size() > 0) {
            sensorsList.get(getLastSensor()).currentHumi = currentHumi;
        }
    }

    public void addTemp(double temp){
        if(sensorsList.size() > 0) {
            sensorsList.get(getLastSensor()).tempList.add(temp);
        }
    }

    public void addTime(String time){
        if(sensorsList.size() > 0) {
            sensorsList.get(getLastSensor()).timeList.add(time);
        }
    }

    public class Sensor {
        protected int gpio;
        protected String name;
        protected Double currentTemp;
        protected Double currentHumi;
        protected ArrayList<Double> tempList = new ArrayList<Double>();
        protected ArrayList<String> timeList = new ArrayList<String>();
    }
}
