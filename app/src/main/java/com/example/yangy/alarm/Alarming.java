package com.example.yangy.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

public class Alarming extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("clock", "闹钟响了........");

        Toast toast=new Toast(context);
        toast.makeText(context,"时间到了",Toast.LENGTH_LONG).show();

        MainActivity.vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        MainActivity.vibrator.vibrate(10 * 1000);//震动10s

        AudioManager audioManager
                = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.RINGER_MODE_NORMAL, 5, 0);

        MainActivity.mediaPlayer = MediaPlayer.create(context, R.raw.ring1);//选择音乐
        MainActivity.mediaPlayer.start();//播放音乐
    }
}
