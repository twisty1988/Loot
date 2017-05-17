package com.twisty.lootlib;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class LootListActivity extends LootBaseActivity implements View.OnClickListener {
    private ImageAdapter adapter;
    Loot loot = Loot.getInstance();
    String albumPath;
    ArrayList<String> images;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loot_list);
        String albumName = getIntent().getStringExtra("AlbumName");
        images = getIntent().getStringArrayListExtra("Images");
        albumPath = getIntent().getStringExtra("AlbumPath");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(this, Loot.getInstance().countPerRow);
        recyclerView.setLayoutManager(layoutManager);
        findViewById(R.id.actionBack).setOnClickListener(this);
        findViewById(R.id.actionRight).setOnClickListener(this);
        View previewButton = findViewById(R.id.preview);
        View doneButton = findViewById(R.id.done);
        TextView imageCount = (TextView) findViewById(R.id.imageCount);
        previewButton.setOnClickListener(this);
        doneButton.setOnClickListener(this);
        ((TextView) findViewById(R.id.actionTitle)).setText(albumName);
        adapter = new ImageAdapter(images);
        recyclerView.setAdapter(adapter);
        findViewById(R.id.bottomBar).setVisibility(loot.isSingle() ? View.GONE : View.VISIBLE);
        adapter.setOnImageItemClickCallback(imagePath -> {
            Intent intent = new Intent(this, PreviewSingleActivity.class);
            intent.putExtra("ImagePath", imagePath);
            startActivity(intent);
        });
        adapter.setOnCheckListener(count -> {
            if (count > 0) {
                imageCount.setVisibility(View.VISIBLE);
                imageCount.setText(count + "");
            } else {
                imageCount.setVisibility(View.INVISIBLE);
            }
            previewButton.setEnabled(count > 0);
            doneButton.setEnabled(count > 0);
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        //Resource IDs cannot be used in a switch statement in Android library modules.
        if (id == R.id.actionBack) {
            finish();
        } else if (id == R.id.actionRight) {
            exitApp();
        } else if (id == R.id.preview) {
            Intent intent = new Intent(this, PreviewActivity.class);
            intent.putStringArrayListExtra("ImagePath", adapter.getSelectedImages());
            startActivity(intent);
        } else if (id == R.id.done) {
            Loot.OnLootedCallback onLootedCallback = loot.getOnLootedCallback();
            if (onLootedCallback != null) {
                onLootedCallback.onLooted(adapter.getSelectedImages());
            }
            exitApp();
        }
    }
}
