package com.twisty.lootlib;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class LootMainActivity extends AppCompatActivity {
    private BroadcastReceiver receiver;
    private RecyclerView recyclerView;
    private static final int LOAD_ALBUM = 0;
    private static final int REQUEST_PERMISSION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loot_main);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                finish();
            }
        };
        registerReceiver(receiver, new IntentFilter("lootLib.CloseMain"));

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(layoutManager);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PERMISSION_GRANTED) {
            queryAlbum();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION && grantResults[0] == PERMISSION_GRANTED) {
            queryAlbum();
        }
    }

    private void queryAlbum() {
        getSupportLoaderManager().initLoader(LOAD_ALBUM, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            private final String[] IMAGE_PROJECTION = {
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media.DATE_ADDED,
                    MediaStore.Images.Media.MIME_TYPE
            };

            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return new CursorLoader(
                        LootMainActivity.this,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        IMAGE_PROJECTION,
                        IMAGE_PROJECTION[2] + "=? OR " + IMAGE_PROJECTION[2] + "=? OR " + IMAGE_PROJECTION[2] + "=? ",
                        new String[]{"image/jpeg", "image/png", "image/gif"},
                        IMAGE_PROJECTION[1] + " DESC");
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                List<Album> folderList = new ArrayList<>();
                while (data.moveToNext()) {
                    File imageFile = new File(data.getString(0));
                    String folderName = imageFile.getParentFile().getName();
                    Album album = new Album(folderName);
                    if (folderList.contains(album)) {
                        int index = folderList.indexOf(album);
                        Album albumInArray = folderList.get(index);
                        albumInArray.setLatestImagePath(imageFile.getPath());
                        albumInArray.setImageCount(albumInArray.getImageCount() + 1);
                    } else {
                        album.setImageCount(1);
                        album.setLatestImagePath(imageFile.getPath());
                        folderList.add(album);
                    }
                }
                AlbumAdapter adapter = new AlbumAdapter(folderList);
                adapter.setActionListener(album -> {
                    Intent intent = new Intent(LootMainActivity.this, LootListActivity.class);
                    intent.putExtra("AlbumName", album.getAlbumName());
                    intent.putExtra("AlbumPath", new File(album.getLatestImagePath()).getParent());
                    startActivity(intent);
                });
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    public void cancel(View view) {
        finish();
    }
}
