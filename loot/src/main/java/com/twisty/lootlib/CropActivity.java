package com.twisty.lootlib;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.isseiaoki.simplecropview.CropImageView;
import com.isseiaoki.simplecropview.callback.CropCallback;
import com.isseiaoki.simplecropview.callback.LoadCallback;
import com.isseiaoki.simplecropview.callback.SaveCallback;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class CropActivity extends LootBaseActivity {
    private CropImageView cropImageView;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);
        String imagePath = getIntent().getStringExtra("ImagePath");
        cropImageView = (CropImageView) findViewById(R.id.cropImageView);
        cropImageView.startLoad(Uri.parse("file://" + imagePath), new LoadCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {

            }
        });
        findViewById(R.id.leftRotation).setOnClickListener(v -> cropImageView.rotateImage(CropImageView.RotateDegrees.ROTATE_M90D));
        findViewById(R.id.rightRotation).setOnClickListener(v -> cropImageView.rotateImage(CropImageView.RotateDegrees.ROTATE_90D));
    }

    public void done(View view) {
        File cacheDir = getCacheDir();
        if (!cacheDir.exists()) cacheDir.mkdir();
        File saveFile = new File(cacheDir.getPath() + "/" + "Loot_" + System.currentTimeMillis() + ".png");
        if (!saveFile.exists()) try {
            saveFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }

        pd = ProgressDialog.show(this, null, "正在处理..", true);

        new Thread(() -> cropImageView.startCrop(Uri.fromFile(saveFile), new CropCallback() {
            @Override
            public void onSuccess(Bitmap cropped) {

            }

            @Override
            public void onError() {

            }
        }, new SaveCallback() {
            @Override
            public void onSuccess(Uri outputUri) {
                if (pd != null && pd.isShowing()) pd.dismiss();
//                Toast.makeText(CropActivity.this, outputUri.toString(), Toast.LENGTH_LONG).show();
                ArrayList<String> data = new ArrayList<>();
                data.add(outputUri.getPath());
                Loot.getInstance().getOnLootedCallback().onLooted(data);
                exitApp();
            }

            @Override
            public void onError() {

            }
        })).start();

    }

    public void cancel(View view) {
        finish();
    }
}
