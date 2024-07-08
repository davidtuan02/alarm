package com.example.myalarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            int hour = intent.getIntExtra("hour", 0);
            int minute = intent.getIntExtra("minute", 0);
            int alarmId = intent.getIntExtra("alarmId", 0);
            boolean isSnooze = intent.getBooleanExtra("isSnooze", false);
//            Toast.makeText(context, "" + isSnooze, Toast.LENGTH_SHORT).show();
            String repeatDays = intent.getStringExtra("repeat");

            Intent alarmIntent = new Intent(context, AlarmActivity.class);
            alarmIntent.putExtra("hour", hour);
            alarmIntent.putExtra("minute", minute);
            alarmIntent.putExtra("alarmId", alarmId);
            alarmIntent.putExtra("isSnooze", isSnooze);
            alarmIntent.putExtra("repeat", repeatDays);
            alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(alarmIntent);

            if (!isSnooze && repeatDays != null && !repeatDays.equals("không")) {
                int nextDayOffset = getNextDayOffset(repeatDays);
                if (nextDayOffset > 0) {
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    Calendar nextAlarmTime = Calendar.getInstance();
                    nextAlarmTime.add(Calendar.DAY_OF_YEAR, nextDayOffset);
                    nextAlarmTime.set(Calendar.HOUR_OF_DAY, hour);
                    nextAlarmTime.set(Calendar.MINUTE, minute);
                    nextAlarmTime.set(Calendar.SECOND, 0);

                    Intent newIntent = new Intent(context, AlarmReceiver.class);
                    newIntent.putExtra("hour", hour);
                    newIntent.putExtra("minute", minute);
                    newIntent.putExtra("alarmId", alarmId);
                    newIntent.putExtra("isSnooze", false);
                    newIntent.putExtra("repeat", repeatDays);

                    PendingIntent pendingIntent = PendingIntent.getBroadcast(
                            context, alarmId, newIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, nextAlarmTime.getTimeInMillis(), pendingIntent);
                }
            }
        }
    }

    private int getNextDayOffset(String repeatDays) {
        if (repeatDays.equalsIgnoreCase("mỗi ngày")) {
            return 1;
        }

        Calendar today = Calendar.getInstance();
        int todayIndex = today.get(Calendar.DAY_OF_WEEK) - 1;

        boolean[] repeatDaysArray = new boolean[7];
        if (repeatDays.startsWith("Mỗi")) {
            String[] days = repeatDays.substring(4).split(" ");
            for (String day : days) {
                switch (day.toLowerCase()) {
                    case "t2":
                        repeatDaysArray[1] = true;
                        break;
                    case "t3":
                        repeatDaysArray[2] = true;
                        break;
                    case "t4":
                        repeatDaysArray[3] = true;
                        break;
                    case "t5":
                        repeatDaysArray[4] = true;
                        break;
                    case "t6":
                        repeatDaysArray[5] = true;
                        break;
                    case "t7":
                        repeatDaysArray[6] = true;
                        break;
                    case "cn":
                        repeatDaysArray[0] = true;
                        break;
                }
            }
        }

        for (int i = 1; i <= 7; i++) {
            int dayIndex = (todayIndex + i) % 7;
            if (repeatDaysArray[dayIndex]) {
                return i;
            }
        }
        return 0;
    }
}
