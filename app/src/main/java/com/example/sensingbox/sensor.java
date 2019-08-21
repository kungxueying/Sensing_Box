package com.example.sensingbox;

import java.io.Serializable;

public class sensor implements Serializable {

    private String sensor_name;
    private String sensor_code;
    private int collection_cycle;
    private String path;
    private String status;

    public sensor(){
        this.sensor_name="Click to add sensor";
        this.sensor_code="Click to add sensor";
        this.collection_cycle=0;
    }

    public String getSensorName() {
        return sensor_name;
    }

    public String getSensorCode() {
        return sensor_code;
    }

    public int getCycle() {
        return collection_cycle;
    }

    public String getPath() {
        return path;
    }

    public String getStatus() {
        return status;
    }

    public void setSensorName(String temp) {
        sensor_name=temp;
    }

    public void setSensorCode(String temp) {
        sensor_code=temp;
    }

    public void setCycle(int temp) {
        collection_cycle=temp;
    }

    public void setPath(String temp) {
        path=temp;
    }

    public void setStatus(String temp) { status=temp; }
}
