package com.example.chuan.yummyclock;

import android.content.Context;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.os.Handler;

import java.util.Calendar;

/**
 * Created by Chuan on 7/4/2015.
 */
public class TimeView extends LinearLayout{

    private TextView tvTime;
    public TimeView(Context context) {
        super(context);
    }

    public TimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        tvTime = (TextView) findViewById(R.id.tv_Time);
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if(getVisibility() == View.VISIBLE) {
            timerHandler.sendEmptyMessage(0);
        } else {
            timerHandler.removeMessages(0);
        }
    }

    private void refreshtime() {
        Calendar c = Calendar.getInstance();
        tvTime.setText(String.format("%d:%d:%d",
                c.get(Calendar.HOUR_OF_DAY),
                c.get(Calendar.MINUTE),
                c.get(Calendar.SECOND)));
    }

    private Handler timerHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            refreshtime();
            if(getVisibility() == View.VISIBLE) {
                timerHandler.sendEmptyMessageAtTime(0, 1000);
            }
        }
    }
}
