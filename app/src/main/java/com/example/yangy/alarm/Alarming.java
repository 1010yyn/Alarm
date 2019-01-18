package com.example.yangy.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.util.Log;

public class Alarming extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("clock", "闹钟响了........");

        MainActivity.vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        MainActivity.vibrator.vibrate(10 * 1000);

        AudioManager audioManager
                = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.RINGER_MODE_NORMAL, 5, 0);

        MainActivity.mediaPlayer = MediaPlayer.create(context, R.raw.ring1);
        MainActivity.mediaPlayer.start();
    }
}
