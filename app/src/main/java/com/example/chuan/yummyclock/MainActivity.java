package com.example.chuan.yummyclock;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TabHost;

import com.example.chuan.yummyclock.stopwatch.view.StopWatchView;

public class MainActivity extends Activity {

    private TabHost tabHost;
    private StopWatchView stopWatchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup(); // Call setup() before adding tabs if loading TabHost using findViewById().

        tabHost.addTab(tabHost.newTabSpec("tabTime").setIndicator("Time").setContent(R.id.tabTime));
        tabHost.addTab(tabHost.newTabSpec("tabAlarm").setIndicator("Alarm").setContent(R.id.tabAlarm));
        tabHost.addTab(tabHost.newTabSpec("tabTimer").setIndicator("Timer").setContent(R.id.tabTimer));
        tabHost.addTab(tabHost.newTabSpec("tabStopWatch").setIndicator("StopWatch").setContent(R.id.tabStopWatch));

        stopWatchView = (StopWatchView) findViewById(R.id.tabStopWatch);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopWatchView.onDestroy();
    }
}
