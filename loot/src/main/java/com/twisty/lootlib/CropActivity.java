package com.twisty.lootlib;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class CropActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);
    }

    public void done(View view) {
    }

    public void cancel(View view) {
        finish();
    }
}
