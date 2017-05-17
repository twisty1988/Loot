package com.twisty.lootlib;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class LootBaseActivity extends AppCompatActivity {
    BroadcastReceiver receiverExit;
    public static final String RECEIVER_EXIT_ACTION = "lootLib.exitAll";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        receiverExit = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                finish();
            }
        };
        registerReceiver(receiverExit, new IntentFilter(RECEIVER_EXIT_ACTION));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiverExit);
    }

    protected void exitApp() {
        sendBroadcast(new Intent(RECEIVER_EXIT_ACTION));
    }
}
