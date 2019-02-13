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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    static String TAG = "MY";

    static MediaPlayer mediaPlayer;
    static Vibrator vibrator;
    private List<String> a_alm = new ArrayList<>();//闹钟列表
    //private ArrayAdapter<String> adapter;
    private BaseAdapter adapter;
    private ListView list_alm;//闹钟列表
    private Button btn_set, btn_stop;//设定时间和停止闹钟
    private Button btn_agenda, btn_alarm;//闹钟状态切换
    private AlertDialog.Builder delete_alarm;
    private AlarmManager alm = null;
    private Calendar cld = Calendar.getInstance();//时间
    private Intent intent;
    private PendingIntent pi;

    private DB db;//DB对象
    private String ID = "id";
    private String Month = "month";
    private String Date = "date";
    private String Hour = "hour";
    private String Minute = "minute";

    @Override //创建活动时调用，进行重定义
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);//调用旧定义
        setContentView(R.layout.activity_main);//使用XML填充活动内容
        db = new DB(MainActivity.this);//创建数据库
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
        btn_set = (Button) findViewById(R.id.btn_set);
        btn_set.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pick();//显示timepicker,进行时间选择
            }
        });
        btn_agenda = (Button) findViewById(R.id.agenda);
        btn_agenda.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //切换到日程界面
            }
        });
        btn_alarm = (Button) findViewById(R.id.alarm);
        btn_alarm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //切换到普通闹钟
            }
        });
        //delete_alarm=new AlertDialog.Builder(MainActivity.this);//确认删除对话框
    }

    //普通闹钟时间选择
    private void pick() {
        Random random = new Random();
        pi = PendingIntent.getBroadcast(this, random.nextInt(100000), intent, 0);//发送广播
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                intent.setAction("com.westsoft.alarmtime.ACTION");//
                Log.i(TAG, "TimeInMillis_1" + cld.getTimeInMillis());
                setAlarm(hourOfDay, minute);//设定闹钟
                addlist(2, 0, 0, hourOfDay, minute, 0, 0, 0, 0, 0, 0, 0);//加入数据库
                if (hourOfDay < 10) {
                    if (minute < 10)
                        a_alm.add("0" + hourOfDay + ":" + "0" + minute);
                    else
                        a_alm.add("0" + hourOfDay + ":" + minute);
                } else {
                    if (minute < 10)
                        a_alm.add(hourOfDay + ":" + "0" + minute);
                    else
                        a_alm.add(hourOfDay + ":" + minute);
                }
                list_alm.setAdapter(adapter);//刷新列表
            }
        }, 0, 0, true);
        timePickerDialog.setTitle("选择时间");
        timePickerDialog.show();
    }

    //日期选择


    //初始化
    private void initAlarm() {
        alm = (AlarmManager) getSystemService(ALARM_SERVICE);
        intent = new Intent(this, Alarming.class); //创建Intent对象
    }

    //设定时间
    private void setAlarm(int hour, int minute) {
        cld.set(Calendar.HOUR_OF_DAY, hour);
        cld.set(Calendar.MINUTE, minute);
        cld.set(Calendar.SECOND, 0);
        //普通闹钟
        Log.i(TAG, "设置单次闹钟");
        if (cld.getTimeInMillis() >= System.currentTimeMillis())//设定时间在当前时间或之后
            alm.set(AlarmManager.RTC_WAKEUP, cld.getTimeInMillis(), pi);//设置闹钟时间(当天)
        else
            alm.set(AlarmManager.RTC_WAKEUP, cld.getTimeInMillis() + 24 * 60 * 60 * 1000, pi);//设定时间早于当前时间，推后到第二天
        Log.i(TAG, "设置重复闹钟");
        //alm.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,cld.getTimeInMillis(),24*60*60*1000,pi);//重复

        //日程闹钟

        Toast toast = new Toast(this);
        toast.makeText(this, "成功设置闹钟！", Toast.LENGTH_LONG).show();//显示闹钟设定成功
        Log.i(TAG, "闹钟设定成功");
    }

    //显示闹钟列表
    private void list() {
        //获取数据库内数据
        String tmp;
        try {
            Log.i("MY", "读取数据");
            Cursor cursor = db.init().query("alarm2", null, null, null, null, null, null);
            cursor.moveToFirst();
            while (cursor.moveToNext()) {
                tmp = cursor.getString(0) + " 月" + cursor.getString(1) + " 日  " + cursor.getString(2) + "：" + cursor.getString(3);
                a_alm.add(tmp);
            }
            cursor.close();
            Log.i("MY", "读取成功");
            //Log.i("MY",tmp);
        } catch (Exception e) {
            Log.e("MYERROR", e.getMessage());
            Log.i("MYINFO", "读取失败");
        }
        list_alm = (ListView) findViewById(R.id.list_alarm);//选定listview
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, a_alm);
        list_alm.setAdapter(adapter);

        //设置滑动监听
        list_alm.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case SCROLL_STATE_IDLE://停止滑动
                        Log.w(TAG, "停止滑动");
                        break;
                    case SCROLL_STATE_TOUCH_SCROLL://正在滑动
                        Log.w(TAG, "正在滑动");
                        break;
                    case SCROLL_STATE_FLING://滑动ListView离开后，由于惯性继续滑动
                        Log.w(TAG, "滑动ListView离开后，由于惯性继续滑动");
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });

        //设置单击事件


        //设置长按监听,长按删除闹钟
//        list_alm.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                delete_alarm.setTitle("系统提示：");
//                delete_alarm.setMessage("确定要删除该闹钟吗？");
//                delete_alarm.setNegativeButton("取消",null);
//                delete_alarm.setPositiveButton("确定",null);
//                delete_alarm.show();
//                return false;
//            }
//        });
        //同时按照时间顺序，把最近的一个设定闹钟
    }

    private void addlist(int table, int month, int date, int hour, int minute, int mon, int tue, int wed, int thur, int fri, int sat, int sun) {
        String s1 = "insert into alarm1 (hour_id ,minute ,Mon ,Tue ,Wed ,Thur ,Fri ,Sat ,Sun ) values (" + hour + "," + minute + "," + mon + "," + tue + "," + wed + "," + thur + "," + fri + "," + sat + "," + sun + ")";
        String s2 = "insert into alarm2 (month_id,date,hour,minute) values (" + month + "," + date + "," + hour + "," + minute + ")";
        try {
            if (table == 1)
                db.getWritableDatabase().execSQL(s1);//表1插入数据
            else if (table == 2)
                db.getWritableDatabase().execSQL(s2);//表2插入数据
            Log.i("MY", "插入新闹钟数据");
        } catch (Exception e) {
            Log.e("MY", e.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close();//关闭数据库连接
        }
    }
}