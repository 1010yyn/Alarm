package com.example.yangy.alarm;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
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
    private AlarmManager alm = null;
    private Calendar cld = Calendar.getInstance();//时间
    private Intent intent;
    private PendingIntent pi;

    @Override //创建活动时调用，进行重定义
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);//调用旧定义
        setContentView(R.layout.activity_main);//使用XML填充活动内容
        initAlarm();//初始化闹钟
        initview();//布局设置
        list();//显示闹钟列表
    }

    //布局设置
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
        if (cld.getTimeInMillis() >= System.currentTimeMillis())//设定时间在当前时间或之后
            alm.set(AlarmManager.RTC_WAKEUP, cld.getTimeInMillis(), pi);//设置闹钟时间(当天)
        else
            alm.set(AlarmManager.RTC_WAKEUP, cld.getTimeInMillis()+24*60*60*1000, pi);//设置闹钟时间（第二天）
    }

    //显示闹钟列表
    private void list() {
        list_alm = (ListView) findViewById(R.id.list_alarm);//选定listview
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, a_alm);
        list_alm.setAdapter(adapter);
    }
}