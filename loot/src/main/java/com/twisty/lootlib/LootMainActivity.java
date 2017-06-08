package com.twisty.lootlib;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.FileProvider;
import android.support.v4.content.Loader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class LootMainActivity extends LootBaseActivity {
    private static final int REQUEST_CODE_CAMERA = 0x001;
    private RecyclerView recyclerView;
    private static final int LOAD_ALBUM = 0;
    private static final int REQUEST_PERMISSION = 0;
    File tmpFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loot_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PERMISSION_GRANTED) {
            queryAlbum();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION && grantResults[0] == PERMISSION_GRANTED && grantResults[1] == PERMISSION_GRANTED) {
            queryAlbum();
        } else {
            // FIXME: 2017/5/16 没有得到授权,给出提示
        }
    }

    private void queryAlbum() {
        getSupportLoaderManager().initLoader(LOAD_ALBUM, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            private final String[] IMAGE_PROJECTION = {
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media.DATE_ADDED,
                    MediaStore.Images.Media.MIME_TYPE,
                    MediaStore.Images.Media.SIZE
            };

            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return new CursorLoader(
                        LootMainActivity.this,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        IMAGE_PROJECTION,
                        IMAGE_PROJECTION[3] + " > 0 AND " + IMAGE_PROJECTION[2] + "=? OR " + IMAGE_PROJECTION[2] + "=?",// OR " + IMAGE_PROJECTION[2] + "=? ",
                        new String[]{"image/jpeg", "image/png"/*, "image/gif"*/},
                        IMAGE_PROJECTION[1] + " DESC");
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                List<Album> folderList = new ArrayList<>();
                Album allImagesFolder = new Album("所有照片");
                allImagesFolder.setImageCount(data.getCount());

                data.moveToFirst();//为了获取所以照片的第一张,不加入循环

                allImagesFolder.setLatestImagePath(data.getString(0));
                folderList.add(allImagesFolder);
                do {
                    String imagePath = data.getString(0);
                    String folderName = new File(imagePath).getParentFile().getName();
                    Album album = new Album(folderName);

                    allImagesFolder.getImagePaths().add(imagePath);//全部图片路径
                    if (folderList.contains(album)) {
                        int index = folderList.indexOf(album);
                        Album albumInArray = folderList.get(index);
//                        albumInArray.setLatestImagePath(imagePath);
                        albumInArray.setImageCount(albumInArray.getImageCount() + 1);
                        albumInArray.getImagePaths().add(imagePath);
                    } else {
                        album.setImageCount(1);
                        album.setLatestImagePath(imagePath);
                        album.getImagePaths().add(imagePath);
                        folderList.add(album);
                    }
                } while (data.moveToNext());

                AlbumAdapter adapter = new AlbumAdapter(folderList);
                adapter.setActionCallback(album -> {
                    Intent intent = new Intent(LootMainActivity.this, LootListActivity.class);
                    intent.putExtra("AlbumName", album.getAlbumName());
                    intent.putExtra("AlbumPath", new File(album.getLatestImagePath()).getParent());
                    intent.putStringArrayListExtra("Images", album.getImagePaths());
                    startActivity(intent);
                });
                adapter.setOnCameraClickCallback(s -> {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    Uri contentUri = null;
                    try {
                        contentUri = FileProvider.getUriForFile(LootMainActivity.this, getPackageName(), tmpFile = FileUtil.createTmpFile());
                    } catch (IOException e) {
                        Toast.makeText(LootMainActivity.this, "无法存储SD卡", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        return;
                    }
                    intent.setFlags(FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setFlags(FLAG_GRANT_WRITE_URI_PERMISSION);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
                    startActivityForResult(intent, REQUEST_CODE_CAMERA);
                });
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        if (requestCode == REQUEST_CODE_CAMERA) {
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(tmpFile)));
            if (Loot.getInstance().isSingle()) {
                Intent intent = new Intent(this, PreviewSingleActivity.class);
                intent.putExtra("ImagePath", tmpFile.getPath());
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void cancel(View view) {
        finish();
    }
}
