package com.example.sensingbox;

public class DS_dataset {
    public String id;
    public String sensor;
    public String time;
    public String userID;
    public String data;
    public String locate;
    public String boxID;
    public String x;
    public String y;

    public DS_dataset(){

    }

    public DS_dataset(String id,String BOXID,String DATA,String USERID,String LOCATE,String SENSOR,String TIME
            ,String X,String Y){
        this.id = id;
        this.boxID = BOXID;
        this.data = DATA;
        this.userID = USERID;
        this.locate = LOCATE;
        this.sensor = SENSOR;
        this.time = TIME;
        this.x = X;
        this.y = Y;

    }
}
