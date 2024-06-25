package com.example.myalarm;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.Vibrator;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

public class AlarmActivity extends Activity {
    private SQL dbHelper;
    private int alarmId;
    private int hour;
    private int minute;
    private boolean isSnooze;
    private PowerManager.WakeLock wakeLock;
    private KeyguardManager keyguardManager;
    private KeyguardManager.KeyguardLock keyguardLock;
    private Vibrator vibrator;
    private String repeatDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        dbHelper = new SQL(this);
        TextView alarmTimeTextView = findViewById(R.id.alarm_time_text_view);

        // Nhận giờ và phút từ Intent
        hour = getIntent().getIntExtra("hour", 0);
        minute = getIntent().getIntExtra("minute", 0);
        alarmId = getIntent().getIntExtra("alarmId", 0);
        isSnooze = getIntent().getIntExtra("isSnooze", 0) == 1;
        repeatDays = getIntent().getStringExtra("repeat");

        // Hiển thị giờ và phút
        String alarmTime = String.format("%02d:%02d", hour, minute);
        alarmTimeTextView.setText(alarmTime);

        // Phát âm thanh báo thức
        dbHelper.playAudio(String.valueOf(alarmId));

        // Khởi động rung
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null && vibrator.hasVibrator()) {
            vibrator.vibrate(1000); // Rung 1 giây
        }

        // Thiết lập sự kiện cho nút Stop
        Button stopButton = findViewById(R.id.stop_button);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.stopAudio();
                if (vibrator != null) {
                    vibrator.cancel();
                }
                handleButtonClick();
            }
        });

        // Thiết lập sự kiện cho nút Snooze
        Button snoozeButton = findViewById(R.id.snooze_button);
        if (isSnooze) {
            snoozeButton.setVisibility(View.VISIBLE);
            snoozeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dbHelper.stopAudio();
                    if (vibrator != null) {
                        vibrator.cancel();
                    }
                    snoozeAlarm();
                    handleButtonClick();
                }
            });
        } else {
            snoozeButton.setVisibility(View.GONE);
        }
    }

    private void handleButtonClick() {
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);

        // Check if the keyguard is active (screen is locked)
        if (keyguardManager.isKeyguardLocked()) {
            // Use window flags to temporarily keep the screen on
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            // Finish the activity after a short delay to allow the screen to react
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 1000); // Adjust delay as needed
        } else {
            // If screen is already unlocked, simply finish the activity
            finish();
        }
    }

    private void snoozeAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("hour", hour);
        intent.putExtra("minute", minute);
        intent.putExtra("alarmId", alarmId);
        intent.putExtra("isSnooze", 1);
        intent.putExtra("repeat", repeatDays);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 5);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(
                PowerManager.SCREEN_BRIGHT_WAKE_LOCK |
                        PowerManager.ACQUIRE_CAUSES_WAKEUP |
                        PowerManager.ON_AFTER_RELEASE,
                "MyApp:AlarmWakeLock");
        wakeLock.acquire(10 * 60 * 1000L /*10 minutes*/);
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseWakeLock();
    }

    private void releaseWakeLock() {
        if (wakeLock != null && wakeLock.isHeld()) {
            wakeLock.release();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (keyguardLock != null) {
            keyguardLock.reenableKeyguard();
        }
    }
}
