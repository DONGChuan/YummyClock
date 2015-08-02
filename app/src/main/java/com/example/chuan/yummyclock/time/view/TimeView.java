package com.example.chuan.yummyclock.time.view;

import android.content.Context;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.os.Handler;

import com.example.chuan.yummyclock.R;

import java.util.Calendar;

/**
 * A Clock View to show current time
 * Created by Chuan on 7/4/2015.
 */
public class TimeView extends LinearLayout{

    public TimeView(Context context) {
        super(context);
    }

    public TimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private TextView tvTime; // TextView to show current time

    private Handler timerHandler = new Handler(){
        public void handleMessage(Message msg) {
            refreshTime();
            // If still in this tab
            if (getVisibility() == View.VISIBLE) {
                // By this way, refreshTime will be ran every second
                timerHandler.sendEmptyMessageDelayed(0, 1000);
            }
        }
    };

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        tvTime = (TextView) findViewById(R.id.tv_Time);
        timerHandler.sendEmptyMessage(0);
    }

    /**
     * If switch to other tab, handler will remove the messages
     * @param changedView
     * @param visibility
     */
    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if(getVisibility() == View.VISIBLE) {
            timerHandler.sendEmptyMessage(0);
        } else { // If View.GONE, so view not visible. Remove message
            timerHandler.removeMessages(0);
        }
    }

    /**
     * Refresh time shown on TextView
     */
    private void refreshTime() {
        Calendar c = Calendar.getInstance();
        // Set current time on TextView tvTime
        tvTime.setText(String.format("%d:%d:%d",
                c.get(Calendar.HOUR_OF_DAY),
                c.get(Calendar.MINUTE),
                c.get(Calendar.SECOND)));
    }
}
