package com.twisty.lootlib;

import android.app.Activity;
import android.content.Intent;

import java.util.List;

/**
 * Project : Loot<br>
 * Created by twisty on 2017/5/16.<br>
 */

public class Loot {

    private volatile static Loot loot;
    private int maxCount = 9;
    private boolean isSingle;
    private boolean hasCamera;
    int countPerRow = 3;
    private OnLootedCallback onLootedCallback;

    public static Loot getInstance() {
        if (loot == null) {
            synchronized (Loot.class) {
                if (loot == null) {
                    loot = new Loot();
                }
            }
        }
        return loot;
    }

    public void start(Activity activity, OnLootedCallback onLootedCallback) {
        this.onLootedCallback = onLootedCallback;
        if (isSingle) {
            // FIXME: 2017/5/16 跳转单选
        } else {
            activity.startActivity(new Intent(activity, LootMainActivity.class));
        }


    }

    public int getMaxCount() {
        return maxCount;
    }

    public Loot setMaxCount(int maxCount) {
        this.maxCount = maxCount;
        return loot;
    }

    public boolean isSingle() {
        return isSingle;
    }

    public Loot setSingle(boolean single) {
        isSingle = single;
        return loot;
    }

    public boolean isHasCamera() {
        return hasCamera;
    }

    public Loot setHasCamera(boolean hasCamera) {
        this.hasCamera = hasCamera;
        return loot;
    }

    OnLootedCallback getOnLootedCallback() {
        return onLootedCallback;
    }

    public interface OnLootedCallback {
        void onLooted(List<String> data);
    }
}
