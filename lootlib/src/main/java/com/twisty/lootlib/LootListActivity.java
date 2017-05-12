package com.twisty.lootlib;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LootListActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int LOAD_IMAGES = 1;
    private String albumPath;
    private RecyclerView recyclerView;
    private ImageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loot_list);
        String albumName = getIntent().getStringExtra("AlbumName");
        albumPath = getIntent().getStringExtra("AlbumPath");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        recyclerView.setLayoutManager(layoutManager);
        findViewById(R.id.actionBack).setOnClickListener(this);
        findViewById(R.id.actionRight).setOnClickListener(this);
        findViewById(R.id.edit).setOnClickListener(this);
        findViewById(R.id.preview).setOnClickListener(this);
        findViewById(R.id.done).setOnClickListener(this);
        ((TextView) findViewById(R.id.actionTitle)).setText(albumName);
        queryImages();

    }


    private void queryImages() {
        getSupportLoaderManager().initLoader(LOAD_IMAGES, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            private final String[] IMAGE_PROJECTION = {
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media.DATE_ADDED,
            };

            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return new CursorLoader(
                        LootListActivity.this,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        IMAGE_PROJECTION,
                        IMAGE_PROJECTION[0] + " like '%" + albumPath + "%'",
                        null,
                        IMAGE_PROJECTION[1] + " DESC");
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                List<String> images = new ArrayList<>();
                while (data.moveToNext()) {
                    File image = new File(data.getString(0));
                    images.add(image.getAbsolutePath());
                }
                adapter = new ImageAdapter(images);
                recyclerView.setAdapter(adapter);
                adapter.setActionListener(imagePath->{});
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
            }
        });
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        //Resource IDs cannot be used in a switch statement in Android library modules.
        if (id == R.id.actionBack) {
            finish();
        } else if (id == R.id.actionRight) {
            sendBroadcast(new Intent("lootLib.CloseMain"));
            finish();
        } else if (id == R.id.edit) {

        } else if (id == R.id.preview) {
            Toast.makeText(this, "Selected:"+adapter.getSelectedImages().size() + ">", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.done) {
            Toast.makeText(this, "Selected:"+adapter.getSelectedImages() + ">", Toast.LENGTH_SHORT).show();
        }
    }
}
