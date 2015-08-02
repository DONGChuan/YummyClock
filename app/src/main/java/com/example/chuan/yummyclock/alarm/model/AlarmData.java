package com.example.chuan.yummyclock.alarm.model;

import java.util.Calendar;

/**
 * AlarmData is to present time by timeLabel
 * Created by Chuan on 8/2/2015.
 */
public class AlarmData {

    private String timeLabel = "";
    private long time = 0;
    private Calendar date;

    public AlarmData(long time) {
        this.time = time;

        date = Calendar.getInstance();
        date.setTimeInMillis(time);

        timeLabel = String.format("%d Month %d Day %d:%d",
                date.get(Calendar.MONTH)+1,
                date.get(Calendar.DAY_OF_MONTH),
                date.get(Calendar.HOUR_OF_DAY),
                date.get(Calendar.MINUTE));
    }

    public long getTime() { return time; }

    public String getTimeLabel() {
        return timeLabel;
    }

    // Using time as Id of each alarm. It is used to delete an existing alarm
    public int getId(){
        return (int)(getTime()/1000/60);
    }

    @Override
    public String toString() {
        return getTimeLabel();
    }

}
