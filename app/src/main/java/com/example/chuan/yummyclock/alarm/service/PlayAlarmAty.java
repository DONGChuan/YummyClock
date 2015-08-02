package com.example.chuan.yummyclock.alarm.service;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;

import com.example.chuan.yummyclock.R;

/**
 * Created by Chuan on 7/21/2015.
 */
public class PlayAlarmAty extends Activity {

    private MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_player_aty);

        mMediaPlayer = MediaPlayer.create(this, R.raw.music);
        mMediaPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMediaPlayer.stop();
        mMediaPlayer.release();
    }
}
