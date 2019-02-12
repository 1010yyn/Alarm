package com.example.yangy.alarm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;

public class DB extends SQLiteOpenHelper{
    public SQLiteDatabase sqldb;

    private static final int VERSION =1;
    private static final String Database="database";
    private static final String Name1="alarm1.db";//普通闹钟
    private static final String Name2="alarm2.db";//日程
    private static final String DBpath="/sdcard/alarm1.db";

    public DB(Context context)
    {
    super(context,Database,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)//创建数据库
    {
        //File file=new File(DBpath);
        //if(file.exists())//数据库已经存在，直接返回
        {
        //    return ;
        }
        //else
        {
            //否则创建数据表项
        db.execSQL("create table "+Name1+"(id integer primary key,hour integer,minute integer,Mon integer,Tue integer,Wed integer,Thur integer,Fri integer,Sat integer,Sun integer)");//序号，时分，重复时间
        db.execSQL("create table "+Name2+"(id integer primary key,month integer,date integer,hour integer,minute integer)");//序号，月日时分
        db.execSQL("insert into "+Name2+"(id,month,date,hour,minute)values('1','2','12','12','12')");
        db.execSQL("insert into "+Name2+"(id,month,date,hour,minute)values('2','2','12','12','12')");
        System.out.print("123");
        }
    }

    @Override//更新
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("更新", "onUpgrade: data");
    }
}
