package com.example.chuan.yummyclock;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.os.Handler;
import android.os.Message;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Chuan on 8/1/2015.
 */
public class StopWatchView extends LinearLayout {

    public StopWatchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private TextView tvHour, tvMin, tvSec, tvMSec;
    private Button btnStart, btnResume, btnReset, btnPause, btnLap;
    private ListView mListView;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        tvHour = (TextView) findViewById(R.id.timeHour);
        tvHour.setText("0");
        tvMin = (TextView) findViewById(R.id.timeMin);
        tvMin.setText("0");
        tvSec = (TextView) findViewById(R.id.timeSec);
        tvSec.setText("0");
        tvMSec = (TextView) findViewById(R.id.timeMSec);
        tvMSec.setText("0");

        btnStart = (Button) findViewById(R.id.btnSWStart);
        btnResume = (Button) findViewById(R.id.btnSWResume);
        btnReset = (Button) findViewById(R.id.btnSWReset);
        btnPause = (Button) findViewById(R.id.btnSWPause);
        btnLap = (Button) findViewById(R.id.btnSWLap);

        btnResume.setVisibility(View.GONE);
        btnReset.setVisibility(View.GONE);
        btnPause.setVisibility(View.GONE);
        btnLap.setVisibility(View.GONE);

        mListView = (ListView) findViewById(R.id.lvWatchTime);
        adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1);
        mListView.setAdapter(adapter);

        btnStart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
                btnStart.setVisibility(View.GONE);
                btnPause.setVisibility(View.VISIBLE);
                btnLap.setVisibility(View.VISIBLE);
            }
        });

        btnPause.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTimer();
                btnPause.setVisibility(View.GONE);
                btnResume.setVisibility(View.VISIBLE);
                btnLap.setVisibility(View.GONE);
                btnReset.setVisibility(View.VISIBLE);
            }
        });

        btnResume.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
                btnResume.setVisibility(View.GONE);
                btnPause.setVisibility(View.VISIBLE);
                btnReset.setVisibility(View.GONE);
                btnLap.setVisibility(View.VISIBLE);
            }
        });

        btnReset.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTimer();
                tenMSec = 0;
                adapter.clear();

                btnReset.setVisibility(View.GONE);
                btnPause.setVisibility(View.GONE);
                btnLap.setVisibility(View.GONE);
                btnResume.setVisibility(View.GONE);
                btnStart.setVisibility(View.VISIBLE);
            }
        });

        btnLap.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
               adapter.insert(String.format("%d:%d:%d.%d",
                       tenMSec/100/60/60,
                       tenMSec/100/60%60,
                       tenMSec/100%60,
                       tenMSec%100), 0);
            }
        });
        showTimerTask = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(MSG_WHAT_SHOW_TIME);
            }
        };
        timer.schedule(showTimerTask, 200, 200);
    }

    private void startTimer() {
        if(timerTask != null) {
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    tenMSec++;
                }
            };

            timer.schedule(timerTask, 10, 10);
        }
    }

    private void stopTimer() {
        if(timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }

    private int tenMSec = 0;
    private Timer timer = new Timer();
    private TimerTask timerTask = null;
    private TimerTask showTimerTask = null;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_WHAT_SHOW_TIME:
                    tvHour.setText(tenMSec/100/60/60 + "");
                    tvMin.setText(tenMSec/100/60%60 + "");
                    tvSec.setText(tenMSec/100%60 + "");
                    tvMSec.setText(tenMSec%60 + "");
                    break;
                default:
                    break;
            }
        }
    };

    private static final int MSG_WHAT_SHOW_TIME = 1;

    public void onDestroy() {
        timer.cancel();
    }
}
