package com.example.myalarm;

import java.io.Serializable;

public class AlarmClockRecord implements Serializable {
    public String id;
    public String label;

    public AlarmClockRecord() {}

    public String hour;
    public String minute;
    public String days;
    public String weekly;
    public byte[] tone;
    public String isSnooze;
    public String isEnable;

    public AlarmClockRecord(String id, String label, String hour, String minute, String days, String weekly, byte[] tone, String isSnooze, String isEnable) {
        this.id = id;
        this.label = label;
        this.hour = hour;
        this.minute = minute;
        this.days = days;
        this.weekly = weekly;
        this.tone = tone;
        this.isSnooze = isSnooze;
        this.isEnable = isEnable;
    }
}

