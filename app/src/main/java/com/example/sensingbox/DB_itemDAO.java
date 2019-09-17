package com.example.sensingbox;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

// 資料功能類別
public class DB_itemDAO {
    // 表格名稱
    public static final String TABLE_NAME = "itemTB";

    // 編號表格欄位名稱，固定不變
    public static final String KEY_ID = "_id";

    // 其它表格欄位名稱
    public static final String BOXID_COLUMN = "boxid";
    public static final String DATA_COLUMN = "data";
    public static final String ID_COLUMN = "id";
    public static final String LOCATE_COLUMN = "locate";
    public static final String SENSOR_COLUMN = "sensor";
    public static final String TIME_COLUMN = "time";
    public static final String X_COLUMN = "x";
    public static final String Y_COLUMN = "y";

    // 使用上面宣告的變數建立表格的SQL指令
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    BOXID_COLUMN + " TEXT NOT NULL, " +
                    DATA_COLUMN + " TEXT NOT NULL, " +
                    ID_COLUMN + " TEXT NOT NULL, " +
                    LOCATE_COLUMN + " TEXT NOT NULL, " +
                    SENSOR_COLUMN + " TEXT NOT NULL, " +
                    TIME_COLUMN + " TEXT NOT NULL, " +
                    X_COLUMN + " TEXT NOT NULL, " +
                    Y_COLUMN + " TEXT NOT NULL)";

    // 資料庫物件
    private SQLiteDatabase db;

    // 建構子，一般的應用都不需要修改
    public DB_itemDAO(Context context) {

        db = DB_MyDBHelper.getDatabase(context);

    }

    // 關閉資料庫，一般的應用都不需要修改
    public void close() {
        db.close();
    }

    // 新增參數指定的物件
    public DS_dataset insert(DS_dataset item) {
        // 建立準備新增資料的ContentValues物件
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的新增資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料

        cv.put(BOXID_COLUMN, item.boxID);
        cv.put(DATA_COLUMN, item.data);
        cv.put(ID_COLUMN, item.id);
        cv.put(LOCATE_COLUMN, item.locate);
        cv.put(SENSOR_COLUMN, item.sensor);
        cv.put(TIME_COLUMN, item.time);
        cv.put(X_COLUMN, item.x);
        cv.put(Y_COLUMN, item.y);

        // 新增一筆資料並取得編號
        // 第一個參數是表格名稱
        // 第二個參數是沒有指定欄位值的預設值
        // 第三個參數是包裝新增資料的ContentValues物件
        long id = db.insert(TABLE_NAME, null, cv);

        // 設定編號
        //item.setId(id);
        // 回傳結果
        return item;
    }

    // 刪除參數指定編號的資料
    public boolean delete(long id){
        // 設定條件為編號，格式為「欄位名稱=資料」
        String where = KEY_ID + "=" + id;
        // 刪除指定編號資料並回傳刪除是否成功
        return db.delete(TABLE_NAME, where , null) > 0;
    }

    // 讀取所有記事資料
    public List<DS_dataset> getAll() {
        List<DS_dataset> result = new ArrayList<>();
        Cursor cursor = db.query(
                TABLE_NAME, null, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            result.add(getRecord(cursor));
        }

        cursor.close();
        return result;
    }

    // 把Cursor目前的資料包裝為物件
    public DS_dataset getRecord(Cursor cursor) {
        // 準備回傳結果用的物件
        DS_dataset result = new DS_dataset();

        result.id = cursor.getString(0);
        result.boxID = cursor.getString(1);
        result.data = cursor.getString(2);
        result.userID = cursor.getString(3);
        result.locate = cursor.getString(4);
        result.sensor = cursor.getString(5);
        result.time = cursor.getString(6);
        result.x = cursor.getString(7);
        result.y = cursor.getString(8);

        // 回傳結果
        System.out.println(result.id+", "+result.boxID+", "+result.data+", "+result.userID+", "+result.locate+", "+
                result.sensor+", "+result.time+", "+result.x+", "+result.y);
        return result;
    }

    // 取得資料數量
    public int getCount() {
        int result = 0;
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null);

        if (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }

        return result;
    }

    // 建立範例資料
    public void sample() {
        //image 的data是路徑

        //DS_dataset item = new DS_dataset(1, new Date().getTime(), Colors.RED, "關於Android Tutorial的事情.", "Hello content", "", 0, 0, 0);
        DS_dataset item2 = new DS_dataset("111","box1", "/storage/emulated/0/Android/data/com.android.browser/files/Download/2019-08-18-19-23-28--1786833669.jpg","user1", "民雄", "temperature", "201908071122", "25.04719", "121.516981");
        //DS_dataset item3 = new DS_dataset("112","box1", "22C","user2", "民雄", "co2", "201908071121", "25.04719", "121");
        //DS_dataset item4 = new DS_dataset("113","box2", "23C", "user3", "嘉義", "image", "201908071123", "25.04719", "121");


        //insert(item2);
        //insert(item3);
        //insert(item4);
        //delete(1);
        //上傳到firebase
        firebase_upload fb = new firebase_upload();
        fb.uploadImg(item2.data);



    }
    // 取得指定編號的資料物件
    public DS_dataset get(long id) {
        // 準備回傳結果用的物件
        DS_dataset item = null;
        // 使用編號為查詢條件
        String where = KEY_ID + "=" + id;
        // 執行查詢
        Cursor result = db.query(
                TABLE_NAME, null, where, null, null, null, null, null);

        // 如果有查詢結果
        if (result.moveToFirst()) {
            // 讀取包裝一筆資料的物件
            item = getRecord(result);
        }

        // 關閉Cursor物件
        result.close();
        // 回傳結果
        return item;
    }
/*
    // 修改參數指定的物件
    public boolean update(Item item) {
        // 建立準備修改資料的ContentValues物件
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的修改資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        cv.put(DATETIME_COLUMN, item.getDatetime());
        cv.put(COLOR_COLUMN, item.getColor().parseColor());
        cv.put(TITLE_COLUMN, item.getTitle());
        cv.put(CONTENT_COLUMN, item.getContent());
        cv.put(FILENAME_COLUMN, item.getFileName());
        cv.put(LATITUDE_COLUMN, item.getLatitude());
        cv.put(LONGITUDE_COLUMN, item.getLongitude());
        cv.put(LASTMODIFY_COLUMN, item.getLastModify());

        // 設定修改資料的條件為編號
        // 格式為「欄位名稱＝資料」
        String where = KEY_ID + "=" + item.getId();

        // 執行修改資料並回傳修改的資料數量是否成功
        return db.update(TABLE_NAME, cv, where, null) > 0;
    }
    */

}