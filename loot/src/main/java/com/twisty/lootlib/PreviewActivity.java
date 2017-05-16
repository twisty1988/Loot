package com.twisty.lootlib;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;
import java.util.List;

public class PreviewActivity extends AppCompatActivity implements View.OnClickListener {
    ViewPager viewPager;
    ArrayList<String> imagePaths;
    List<String> removedImage = new ArrayList<>();
    View actionBar;
    View bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        imagePaths = getIntent().getStringArrayListExtra("ImagePath");

        bottomBar = findViewById(R.id.bottomBar);
        actionBar = findViewById(R.id.actionBar);
        View doneButton = findViewById(R.id.done);
        TextView imageCount = (TextView) findViewById(R.id.imageCount);
        CheckBox checkBox = (CheckBox) findViewById(R.id.checkbox);
        initBottomBar(doneButton, imageCount);
        CompoundButton.OnCheckedChangeListener onCheckedChangeListener = (buttonView, isChecked) -> {
            if (!isChecked) {
                removedImage.add(imagePaths.get(viewPager.getCurrentItem()));
            } else {
                removedImage.remove(imagePaths.get(viewPager.getCurrentItem()));
            }
            initBottomBar(doneButton, imageCount);
        };
        checkBox.setOnCheckedChangeListener(onCheckedChangeListener);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        doneButton.setOnClickListener(this);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (removedImage.contains(imagePaths.get(position))) {
                    checkBox.setOnCheckedChangeListener(null);
                    checkBox.setChecked(false);
                    checkBox.setOnCheckedChangeListener(onCheckedChangeListener);
                } else {
                    checkBox.setOnCheckedChangeListener(null);
                    checkBox.setChecked(true);
                    checkBox.setOnCheckedChangeListener(onCheckedChangeListener);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        findViewById(R.id.actionBack).setOnClickListener(this);
        viewPager.setAdapter(new PreviewAdapter());
        viewPager.setCurrentItem(0);
    }

    private void initBottomBar(View doneButton, TextView imageCount) {
        int deltaSize = imagePaths.size() - removedImage.size();
        doneButton.setEnabled(deltaSize > 0);
        if (deltaSize > 0) {
            imageCount.setVisibility(View.VISIBLE);
            imageCount.setText(deltaSize + "");
        } else {
            imageCount.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        int vId = v.getId();
        if (vId == R.id.actionBack) {
            finish();
        } else if (vId == R.id.done) {
            Loot.OnLootedCallback onLootedCallback = Loot.getInstance().getOnLootedCallback();
            if (onLootedCallback != null) {
                imagePaths.removeAll(removedImage);
                onLootedCallback.onLooted(imagePaths);
            }
            sendBroadcast(new Intent("lootLib.CloseMain"));
            sendBroadcast(new Intent("lootLib.CloseList"));
            finish();
        }
    }

    class PreviewAdapter extends PagerAdapter {
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = (PhotoView) getLayoutInflater().inflate(R.layout.image_preview_item, container, false);
            photoView.setOnPhotoTapListener((imageView, v, v1) -> {
                if (actionBar.getVisibility() == View.VISIBLE) {
                    actionBar.setVisibility(View.INVISIBLE);
                    bottomBar.setVisibility(View.INVISIBLE);
                } else {
                    actionBar.setVisibility(View.VISIBLE);
                    bottomBar.setVisibility(View.VISIBLE);
                }
            });
            Glide.with(PreviewActivity.this).load(imagePaths.get(position)).into(photoView);
            container.addView(photoView);
            return photoView;
        }

        @Override
        public int getCount() {
            return imagePaths.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
