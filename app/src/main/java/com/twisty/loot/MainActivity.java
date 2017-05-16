package com.twisty.loot;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.twisty.lootlib.Loot;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onclick(View view) {
        Loot.getInstance()
                .setSingle(false)
                .setHasCamera(true)
                .setMaxCount(9)
                .start(this, data -> Log.e("X", data.toString()));
    }
}
