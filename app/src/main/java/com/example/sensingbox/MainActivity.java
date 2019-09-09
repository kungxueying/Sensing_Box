package com.example.sensingbox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    DS_user newuser = new DS_user();
    DS_dataset newdata = new DS_dataset();
    DS_sensorall sensor = new DS_sensorall();
    firebase_upload fb = new firebase_upload();

    private DB_itemDAO itemDAO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setNewuser(newuser);
        //setNewsensor(sensor);

        //setNewdata(newdata);
        //setFb(fb);
        System.out.println("sqllite test");
        //sqllite test

        // 建立資料庫物件
        itemDAO = new DB_itemDAO(getApplicationContext());
        System.out.println("建立資料庫物件成功");
        // 如果資料庫是空的，就建立一些範例資料
        // 這是為了方便測試用的，完成應用程式以後可以拿掉
        //if (itemDAO.getCount() == 0) {
            System.out.println("資料庫是空的");
            itemDAO.sample();
       // }

        System.out.println("新增資料成功");
        List<DS_dataset> items = new ArrayList<>();
        // 取得所有記事資料
        items = itemDAO.getAll();

        //System.out.println(items);
        System.out.println("讀取資料成功");
        itemDAO.close();

    }
    /*
    @Override
    protected void onStop() {
        super.onStop();
        db.close(); // 關閉資料庫
    }
    // Button元件的事件處理 - 插入記錄
    public void btn1_Click(View view) {
        long id;
        ContentValues cv = new ContentValues();
        cv.put("_id", Integer.parseInt(txtID.getText().toString()));
        cv.put("name", txtName.getText().toString());
        cv.put("grade", Double.parseDouble(txtGrade.getText().toString()));
        id = db.insert(DATABASE_TABLE, null, cv);
        output.setText("新增記錄成功: " + id);
        command.setText("INSERT INTO Student _id,name,grade
                Values"+txtID.getText().toString()+","+txtName.getText().toString()
                        +","+txtGrade.getText().toString());
    }
    // 查詢所有記錄
    public void btn4_Click(View view) {
        // 查詢整個資料表
        SqlQuery("SELECT * FROM " + DATABASE_TABLE);
        command.setText("SELECT * FROM  Student");
    }
    // 執行SQL查詢
    public void SqlQuery(String sql) {
        String[] colNames;
        String str = "";
        Cursor c = db.rawQuery(sql, null);
        colNames = c.getColumnNames();
        // 顯示欄位名稱
        for (int i = 0; i < colNames.length; i++)
            str += colNames[i] + "\t\t";
        str += "\n";
        c.moveToFirst();  // 第1筆
        // 顯示欄位值
        for (int i = 0; i < c.getCount(); i++) {
            str += c.getString(0) + "\t\t";
            str += c.getString(1) + "\t\t";
            str += c.getString(2) + "\n";
            c.moveToNext();  // 下一筆
        }
        output.setText(str.toString());
    }
    */

    public void goTo_Login (View view){
        Intent intent = new Intent (this, main_screen.class);
        startActivity(intent);
    }

    public void goTo_Register (View view){
        Intent intent = new Intent (this, main_screen.class);
        startActivity(intent);
    }

    public void setNewuser(DS_user newuser) {
        this.newuser = newuser;
        newuser.id = "111";
        newuser.name = "Amy";
        newuser.email = "amy111@gmail.com";
        newuser.pwd = "11122";
    }

    public void setNewdata(DS_dataset newdata) {
        this.newdata = newdata;
        newdata.time ="2017070711";
        newdata.boxID ="2";
        newdata.data ="33:";
        newdata.locate ="民雄";
        newdata.sensor ="temperature";
        newdata.userID = "111";
        newdata.x = "20";
        newdata.y = "121";

    }

    public void setNewsensor(DS_sensorall sensor) {
        this.sensor = sensor;
        sensor.sensorName ="temperature";
    }
    public void setFb(firebase_upload fb) {
        this.fb = fb;
        //fb.insertuser(newuser);
        //fb.insertsensorall(sensor);
        fb.insertdata(newdata);
    }

}
