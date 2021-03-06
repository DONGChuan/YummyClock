package com.example.chuan.yummyclock.alarm.view;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TimePicker;

import com.example.chuan.yummyclock.alarm.service.AlarmReceiver;
import com.example.chuan.yummyclock.R;
import com.example.chuan.yummyclock.alarm.model.AlarmData;

import java.util.Calendar;

/**
 * Created by Chuan on 7/5/2015.
 */
public class AlarmView extends LinearLayout {

    /*********** Variables ***********/
    private Button btnAddAlarm; // Button to add an alarm
    private ListView lvAlarmList; // ListView to show every alarm
    private ArrayAdapter<AlarmData> adapter;
    private static final String KEY_ALARM_LIST = "alarmList";
    private AlarmManager alarmManager;

    /*********** Constructors ***********/
    public AlarmView(Context context) {
        super(context);
        init();
    }

    public AlarmView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AlarmView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /*********** Functions ***********/
    private void init(){
        alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        btnAddAlarm = (Button) findViewById(R.id.btn_add_alarm);
        lvAlarmList = (ListView) findViewById(R.id.lv_alarm);
        adapter = new ArrayAdapter<AlarmData>(getContext(), android.R.layout.simple_list_item_1);
        lvAlarmList.setAdapter(adapter);

        readSavedAlarmList();

        btnAddAlarm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                addAlarm();
            }
        });
        // Long click to pop-up a Dialog to delete an alarm
        lvAlarmList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Options")
                        .setItems(new CharSequence[]{"Delete"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        deleteAlarm(position);
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }).setNegativeButton("Cancel", null)
                        .show();
                return true;
            }
        });
    }

    /**
     * Pop-up a time picker to select alarm
     */
    private void addAlarm() {
        Calendar c = Calendar.getInstance();

        new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                Calendar calendar = Calendar.getInstance();

                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                Calendar currentTime = Calendar.getInstance();
                // If setting time is earlier than current time, day + 1
                if (calendar.getTimeInMillis() <= currentTime.getTimeInMillis()) {
                    calendar.setTimeInMillis(calendar.getTimeInMillis()+24*60*60*1000);
                }

                AlarmData ad = new AlarmData(calendar.getTimeInMillis());
                adapter.add(ad); // Add in the list
                // Setting alarm
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                        ad.getTime(),
                        5 * 60 * 1000,
                        PendingIntent.getBroadcast(getContext(), ad.getId(), new Intent(getContext(), AlarmReceiver.class), 0));
                // Save alarm in sharedPreference
                saveAlarmList();
            }
        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
    }

    /**
     * Save alarm in sharedPreference
     */
    private void saveAlarmList() {
        SharedPreferences.Editor editor = getContext().getSharedPreferences(AlarmView.class.getName(), Context.MODE_PRIVATE).edit();

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < adapter.getCount(); i++) {
            sb.append(adapter.getItem(i).getTime()).append(",");
        }

        if (sb.length() > 1) {
            String content = sb.toString().substring(0, sb.length()-1);
            editor.putString(KEY_ALARM_LIST, content);
        }else{
            editor.putString(KEY_ALARM_LIST, null);
        }

        editor.commit();
    }

    /**
     * Read alarms from sharedPreference
     */
    private void readSavedAlarmList(){
        SharedPreferences sp = getContext().getSharedPreferences(AlarmView.class.getName(), Context.MODE_PRIVATE);
        String content = sp.getString(KEY_ALARM_LIST, null);

        if (content != null) {
            String[] timeStrings = content.split(",");
            for (String string : timeStrings) {
                adapter.add(new AlarmData(Long.parseLong(string)));
            }
        }
    }

    /**
     * Delete an Alarm
     * @param position
     */
    private void deleteAlarm(int position){
        AlarmData ad = adapter.getItem(position);
        adapter.remove(ad);
        saveAlarmList();

        alarmManager.cancel(PendingIntent.getBroadcast(getContext(),
                ad.getId(),
                new Intent(getContext(), AlarmReceiver.class),
                0));
    }
}
