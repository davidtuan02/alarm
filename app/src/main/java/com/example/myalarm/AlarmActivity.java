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
import android.widget.LinearLayout;
import android.widget.TextView;



import java.util.Arrays;
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
    private AlarmClockRecord alarm;
    private LinearLayout snooze, stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        dbHelper = new SQL(this);
        TextView tvSnooze = findViewById(R.id.tvSnooze);
        TextView tvStop = findViewById(R.id.tvStop);
        snooze = findViewById(R.id.Snooze);
        stop = findViewById(R.id.Stop);

        // Nhận giờ và phút từ Intent
        hour = getIntent().getIntExtra("hour", 0);
        minute = getIntent().getIntExtra("minute", 0);
        alarmId = getIntent().getIntExtra("alarmId", 0);
        isSnooze = getIntent().getBooleanExtra("isSnooze", false);
        repeatDays = getIntent().getStringExtra("repeat");

        // Phát âm thanh báo thức
        dbHelper.playAudio(String.valueOf(alarmId));

        // Khởi động rung
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null && vibrator.hasVibrator()) {
            vibrator.vibrate(1000); // Rung 1 giây
        }

        if (isSnooze) {
            snooze.setVisibility(View.VISIBLE);
            tvSnooze.setOnClickListener(new View.OnClickListener() {
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
            snooze.setVisibility(View.GONE);
        }

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.stopAudio();
                if (vibrator != null) {
                    vibrator.cancel();
                }
                handleButtonClick();
                //set ngay tiep theo
            }
        });


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
        intent.putExtra("isSnooze", true);
        intent.putExtra("repeat", repeatDays);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 5);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    private void setRepeatingAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("hour", hour);
        intent.putExtra("minute", minute);
        intent.putExtra("alarmId", alarmId);
        intent.putExtra("isSnooze", false);
        intent.putExtra("repeat", repeatDays);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        // Tìm ngày kế tiếp để lặp lại
        boolean[] isEnableArray = new boolean[7]; // 0: Chủ Nhật, 1: Thứ Hai, ..., 6: Thứ Bảy
        Arrays.fill(isEnableArray, false);

        if ("Mỗi ngày".equals(repeatDays)) {
            Arrays.fill(isEnableArray, true);
        } else if (!"Không".equals(repeatDays)) {
            String[] repeatDaysArray = repeatDays.split(" ");
            for (String day : repeatDaysArray) {
                int dayOfWeek = convertDayStringToInt(day) - 1;
                isEnableArray[dayOfWeek] = true;
            }

            Calendar todayCalendar = Calendar.getInstance();
            int today = todayCalendar.get(Calendar.DAY_OF_WEEK) - 1;

            boolean foundNextRepeat = false;
            for (int i = 1; i <= 7; i++) {
                int nextDay = (today + i) % 7;
                if (isEnableArray[nextDay]) {
                    calendar.add(Calendar.DAY_OF_YEAR, i);
                    foundNextRepeat = true;
                    break;
                }
            }

            if (!foundNextRepeat) {
                Arrays.fill(isEnableArray, false);
            }
        }

        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }

        StringBuilder isEnableBuilder = new StringBuilder();
        for (boolean isEnabled : isEnableArray) {
            isEnableBuilder.append(isEnabled ? "1" : "0").append(" ");
        }
        String isEnable = isEnableBuilder.toString().trim();

        if (alarmId != -1) {
            alarm = dbHelper.getAlarmById(String.valueOf(alarmId));
            if (alarm != null) {
                AlarmClockRecord updatedAlarm = new AlarmClockRecord(
                        String.valueOf(alarmId),
                        alarm.label,
                        alarm.hour,
                        alarm.minute,
                        alarm.days,
                        alarm.weekly,
                        alarm.tone,
                        alarm.isSnooze,
                        isEnable
                );
                dbHelper.updateRecord(updatedAlarm);
            }
        }
    }

    private int convertDayStringToInt(String day) {
        switch (day) {
            case "T2":
                return Calendar.MONDAY;
            case "T3":
                return Calendar.TUESDAY;
            case "T4":
                return Calendar.WEDNESDAY;
            case "T5":
                return Calendar.THURSDAY;
            case "T6":
                return Calendar.FRIDAY;
            case "T7":
                return Calendar.SATURDAY;
            case "CN":
                return Calendar.SUNDAY;
            default:
                throw new IllegalArgumentException("Invalid day: " + day);
        }
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
