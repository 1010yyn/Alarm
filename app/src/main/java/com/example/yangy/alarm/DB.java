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
                Log.i(TAG, "创建成功");
            } catch (Exception e1) {
                Log.e(TAG, e.getMessage());
                Log.i(TAG, "创建失败");
            }
        }
        Log.i(TAG, "返回");
        return DB;//返回一个可读写的数据库对象
    }

    //添加数据
    public void add(int table, int month, int date, int hour, int minute, int mon, int tue, int wed, int thur, int fri, int sat, int sun) {
        String s1 = "insert into alarm1 (hour_id ,minute ,Mon ,Tue ,Wed ,Thur ,Fri ,Sat ,Sun ) values (" + hour + "," + minute + "," + mon + "," + tue + "," + wed + "," + thur + "," + fri + "," + sat + "," + sun + ")";
        String s2 = "insert into alarm2 (month_id,date,hour,minute) values (" + month + "," + date + "," + hour + "," + minute + ")";
        try {
            if (table == 1)
                DB.execSQL(s1);//表1插入数据
            else if (table == 2)
                DB.execSQL(s2);//表2插入数据
            Log.i(TAG, "插入新闹钟数据");
        } catch (Exception e) {
            Log.i(TAG, "数据插入失败");
            Log.e(TAG, e.getMessage());
        }
    }

    //删除数据
    public void delete(int table, int month, int date, int hour, int minute, int mon, int tue, int wed, int thur, int fri, int sat, int sun) {
        String s1 = "delete from alarm1 where hour_id=" + hour + " minute=" + minute + " Mon=" + mon + " Tue=" + tue + " Wed=" + wed + " Thur=" + thur + " Fri=" + fri + " Sat=" + sat + " Sun=" + sun;
        String s2 = "delete from alarm2 where month_id=" + month + " and date=" + date + " and hour=" + hour + " and minute=" + minute;
        try {
            if (table == 1)
                DB.execSQL(s1);
            else if (table == 2)
                DB.execSQL(s2);
            Log.i(TAG, "数据库删除成功");
        } catch (Exception e) {
            Log.e(TAG, "数据库删除失败");
            Log.e(TAG, e.getMessage());
        }
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
