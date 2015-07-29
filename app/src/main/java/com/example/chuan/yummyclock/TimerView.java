package com.example.chuan.yummyclock;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by dchuan on 28/07/2015.
 */
public class TimerView extends LinearLayout {

    private Timer timer = new Timer();
    private TimerTask timerTask = null;
    private Button btnStart, btnPause, btnResume, btnReset;
    private EditText etHour, etMin, etSec;
    private int allTimerCount = 0;
    private static final int MSG_WHAT_TIME_IS_UP = 1;

    public TimerView(Context context) {
        super(context);
    }

    public TimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        btnStart = (Button) findViewById(R.id.btnStart);
        btnPause = (Button) findViewById(R.id.btnPause);
        btnResume = (Button) findViewById(R.id.btnResume);
        btnReset = (Button) findViewById(R.id.btnReset);

        etHour = (EditText) findViewById(R.id.etHour);
        etMin = (EditText) findViewById(R.id.etMin);
        etSec = (EditText) findViewById(R.id.etSecond);

        etHour.setText("00");
        etHour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {
                if (!TextUtils.isEmpty(s)) {
                    int value = Integer.parseInt(s.toString());

                    if (value > 59) {
                        etHour.setText("59");
                    } else if (value < 0) {
                        etHour.setText("0");
                    }
                }
                checkToEnableBtnStart();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etMin.setText("00");
        etMin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {
                if (!TextUtils.isEmpty(s)) {
                    int value = Integer.parseInt(s.toString());

                    if (value > 59) {
                        etMin.setText("59");
                    } else if (value < 0) {
                        etMin.setText("0");
                    }
                }
                checkToEnableBtnStart();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etSec.setText("00");
        etSec.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {
                if (!TextUtils.isEmpty(s)) {
                    int value = Integer.parseInt(s.toString());

                    if (value > 59) {
                        etSec.setText("59");
                    } else if (value < 0) {
                        etSec.setText("0");
                    }
                }
                checkToEnableBtnStart();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btnStart.setVisibility(View.VISIBLE);
        btnStart.setEnabled(false);
        btnPause.setVisibility(View.GONE);
        btnResume.setVisibility(View.GONE);
        btnReset.setVisibility(View.GONE);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
            }
        });
    }

    private void startTimer() {
        if(timerTask == null) {
            allTimerCount = Integer.parseInt(etHour.getText().toString())*60*60 +
                    Integer.parseInt(etMin.getText().toString())*60 +
                    Integer.parseInt(etSec.getText().toString());
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    allTimerCount--;

                    if(allTimerCount <= 0) {
                        handler.sendEmptyMessage(MSG_WHAT_TIME_IS_UP);
                        stopTimer();
                    }
                }
            };
            timer.schedule(timerTask, 1000, 1000); // Run after 1s then every 1s

            btnStart.setVisibility(View.GONE);
            btnPause.setVisibility(View.VISIBLE);
            btnResume.setVisibility(View.VISIBLE);
        }
    }

    private void stopTimer() {
        if(timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_WHAT_TIME_IS_UP:
                    new AlertDialog.Builder(getContext()).setTitle("Time is up").setMessage("Timer is up").setNegativeButton("Cancel", null).show();
                    break;
                default:
                    break;
            }
        };
    };

    private void checkToEnableBtnStart() {
        btnStart.setEnabled( (!TextUtils.isEmpty(etHour.getText()) && Integer.parseInt(etHour.getText().toString()) > 0) ||
                (!TextUtils.isEmpty(etMin.getText()) && Integer.parseInt(etMin.getText().toString()) > 0) ||
                (!TextUtils.isEmpty(etSec.getText()) && Integer.parseInt(etSec.getText().toString()) > 0));
    }
}
