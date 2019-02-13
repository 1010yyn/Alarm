package com.example.yangy.alarm;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;

public class DB extends SQLiteOpenHelper {
    static String TAG = "MY";

    public SQLiteDatabase DB;

    private static final int VERSION = 1;
    private static final String Database = "database";
    String c1 = "create table alarm1 (hour_id integer,minute integer,Mon integer,Tue integer,Wed integer,Thur integer,Fri integer,Sat integer,Sun integer)";//序号，时分，重复时间
    String c2 = "create table alarm2 (month_id integer,date integer,hour integer,minute integer)";//序号，月日时分
    String c3 = "insert into alarm2 (month_id,date,hour,minute) values (2,12,13,12)";
    String c4 = "insert into alarm2 (month_id,date,hour,minute) values (2,12,12,12)";

    public DB(Context context) {
        super(context, Database, null, VERSION);
    }


    public SQLiteDatabase init() {
        DB = this.getWritableDatabase();
        try {
            Log.i(TAG, "查询数据库是否存在");
            Cursor cursor1 = DB.query("alarm1", null, null, null, null, null, null);
            Cursor cursor2 = DB.query("alarm2", null, null, null, null, null, null);
        } catch (Exception e) {
            Log.i(TAG, "创建数据库");
            try {
                Log.i(TAG, "创建表项");
                DB.execSQL(c1);//表1
                DB.execSQL(c2);//表2
//                DB.execSQL(c3);//表2加入数据
//                DB.execSQL(c4);//表2加入数据
                Log.i(TAG, "创建成功");
            } catch (Exception e1) {
                Log.e(TAG, e.getMessage());
                Log.i(TAG, "创建失败");
            }
        }
        Log.i(TAG, "返回");
        return DB;//返回一个可读写的数据库对象
    }

    @Override
    public void onCreate(SQLiteDatabase db)//创建数据库
    {
        Log.i(TAG, "DB");
    }

    @Override//更新
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "onUpgrade: data");
    }
}
