package com.example.yangy.alarm;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static MediaPlayer mediaPlayer;
    static Vibrator vibrator;
    private List<String> a_alm = new ArrayList<>();//闹钟列表
    private ArrayAdapter<String> adapter;
    private ListView list_alm;//闹钟列表
    private Button btn_set, btn_stop;//设定时间和停止闹钟
    //private Button agenda,alarm;//闹钟状态切换
    private AlarmManager alm = null;
    private Calendar cld = Calendar.getInstance();//时间
    private Intent intent;
    private PendingIntent pi;

    private DB db;//DB对象
    private String ID="id";
    private String Month="month";
    private String Date="date";
    private String Hour="hour";
    private String Minute="minute";

    @Override //创建活动时调用，进行重定义
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);//调用旧定义
        setContentView(R.layout.activity_main);//使用XML填充活动内容
        db=new DB(MainActivity.this);//创建数据库
        initAlarm();//初始化闹钟
        initview();//布局设置
        list();//显示闹钟列表
    }

    private void initview() {


        btn_stop = (Button) findViewById(R.id.btn_stop);//获取布局文件中的btn_stop按钮
        btn_stop.setOnClickListener(new OnClickListener() {
            @Override//按下停止时，音乐停止播放，震动停止
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.release();
                vibrator.cancel();
            }
        });
        btn_set = (Button) findViewById(R.id.btn_set);//获取布局文件中的btn_set按钮
        btn_set.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pick();//显示timepicker,进行时间选择
            }
        });
    }

    //时间选择
    private void pick() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                intent.setAction("com.westsoft.alarmtime.ACTION");//Activity   不懂
                //pi 下面
                Log.i("TimeInMillis", "TimeInMillis_1" + cld.getTimeInMillis() + "");
                setAlarm(hourOfDay, minute);//设定闹钟
                a_alm.add("闹钟\t" + hourOfDay + ":" + minute);//添加闹钟列表
                list_alm.setAdapter(adapter);//刷新列表
            }
        }, 0, 0, true);
        timePickerDialog.setTitle("选择时间");
        timePickerDialog.show();
    }

    //初始化
    private void initAlarm() {
        alm = (AlarmManager) getSystemService(ALARM_SERVICE);
        intent = new Intent(this, Alarming.class); //创建Intent对象
        pi = PendingIntent.getBroadcast(this, 0, intent, 0);//??暂时不懂
    }

    //设定时间
    private void setAlarm(int hour, int minute) {
        cld.set(Calendar.HOUR_OF_DAY, hour);
        cld.set(Calendar.MINUTE, minute);
        cld.set(Calendar.SECOND, 0);
        //普通闹钟
        if (cld.getTimeInMillis() >= System.currentTimeMillis())//设定时间在当前时间或之后
            alm.set(AlarmManager.RTC_WAKEUP, cld.getTimeInMillis(), pi);//设置闹钟时间(当天)
        else
            alm.set(AlarmManager.RTC_WAKEUP, cld.getTimeInMillis()+24*60*60*1000, pi);//设定时间早于当前时间，推后到第二天

        //alm.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,cld.getTimeInMillis(),24*60*60*1000,pi);//重复
        //日程闹钟

        Toast toast = new Toast(this);
        toast.makeText(this,"成功设置闹钟！",Toast.LENGTH_LONG).show();//显示闹钟设定成功
    }

    //显示闹钟列表
    private void list() {
        //获取数据库内数据
        String tmp;
       Cursor cursor=db.getWritableDatabase().query("alarm2.db",null,null,null,null,null,null);
       // cursor.moveToFirst();
//        while(cursor.moveToNext())
//        {
//            tmp=cursor.getString(0)+"."+cursor.getString(1)+"."+cursor.getString(2)+"."+cursor.getString(3);
//            a_alm.add(tmp);
//        }
        cursor.close();
        list_alm = (ListView) findViewById(R.id.list_alarm);//选定listview
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, a_alm);
        list_alm.setAdapter(adapter);
    }

    private void addlist()
    {

    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if(db!=null)
        {
            db.close();//关闭数据库连接
        }
    }
}