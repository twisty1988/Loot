package com.twisty.lootlib;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;

public class LootBaseActivity extends AppCompatActivity {
    LocalBroadcastManager localBroadcastManager;


    BroadcastReceiver receiverExit;
    public String RECEIVER_EXIT_ACTION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        RECEIVER_EXIT_ACTION = getPackageName() + "lootLib.exitAll";
        receiverExit = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                finish();
            }
        };
        localBroadcastManager.registerReceiver(receiverExit, new IntentFilter(RECEIVER_EXIT_ACTION));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(receiverExit);
    }

    protected void exitApp() {
        localBroadcastManager.sendBroadcast(new Intent(RECEIVER_EXIT_ACTION));
    }
}
