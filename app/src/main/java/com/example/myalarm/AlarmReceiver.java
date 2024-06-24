// AlarmReceiver.java
package com.example.myalarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "AlarmReceiver";
    private PowerManager.WakeLock wakeLock;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Alarm triggered!");

        if (intent != null) {
            Log.i(TAG, "Action: " + intent.getAction());
            Log.i(TAG, "Data: " + intent.getDataString());
            Log.i(TAG, "Component: " + intent.getComponent());
            Log.i(TAG, "Flags: " + intent.getFlags());

            Bundle extras = intent.getExtras();
            if (extras != null) {
                for (String key : extras.keySet()) {
                    Object value = extras.get(key);
                    Log.i(TAG, "Extras: Key = " + key + ", Value = " + value);
                }

                // Lấy giờ và phút từ extras
                int hour = extras.getInt("hour", 0);
                int minute = extras.getInt("minute", 0);
                int alarmId = extras.getInt("alarmId", 0);
                int isSnooze = extras.getInt("isSnooze", 0);

                // Đánh thức thiết bị và giữ cho màn hình sáng
                PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
                wakeLock = powerManager.newWakeLock(
                        PowerManager.SCREEN_BRIGHT_WAKE_LOCK |
                                PowerManager.ACQUIRE_CAUSES_WAKEUP |
                                PowerManager.ON_AFTER_RELEASE,
                        "MyApp:AlarmWakeLock");
                wakeLock.acquire(10*60*1000L /*10 minutes*/);

                // Mở AlarmActivity
                Intent alarmIntent = new Intent(context, AlarmActivity.class);
                alarmIntent.putExtra("hour", hour);
                alarmIntent.putExtra("minute", minute);
                alarmIntent.putExtra("alarmId", alarmId);
                alarmIntent.putExtra("isSnooze", isSnooze);
                alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                context.startActivity(alarmIntent);
            } else {
                Log.i(TAG, "No extras in the Intent");
            }
        } else {
            Log.i(TAG, "Received null Intent");
        }
    }
}


