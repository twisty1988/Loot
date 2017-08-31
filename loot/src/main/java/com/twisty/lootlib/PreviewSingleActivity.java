package com.twisty.lootlib;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;

public class PreviewSingleActivity extends LootBaseActivity {
    PhotoView photoView;
    Loot loot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_single);
        View actionBar = findViewById(R.id.actionBar);
        loot = Loot.getInstance();
        String imagePath = getIntent().getStringExtra("ImagePath");

        if (loot.isSingle()) {
            View doneView = findViewById(R.id.done);
            doneView.setVisibility(View.VISIBLE);
            doneView.setOnClickListener(v -> {
                if (loot.isHasCrop()) {
                    Intent intent = new Intent(this, CropActivity.class);
                    intent.putExtra("ImagePath", imagePath);
                    startActivity(intent);
                } else {
                    ArrayList<String> data = new ArrayList<>();
                    data.add(imagePath);
                    loot.getOnLootedCallback().onLooted(data);
                    exitApp();
                }
            });
        }
        photoView = (PhotoView) findViewById(R.id.photoView);
        photoView.setOnPhotoTapListener((imageView, v, v1) -> {
            if (actionBar.getVisibility() == View.GONE) {
                actionBar.setVisibility(View.VISIBLE);
            } else {
                actionBar.setVisibility(View.GONE);
            }
        });
        GlideApp.with(this).load(imagePath).into(photoView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void cancel(View view) {
        finish();
    }
}
