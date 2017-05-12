package com.twisty.lootlib;

/**
 * Project : Loot<br>
 * Created by twisty on 2017/5/12.<br>
 */
@FunctionalInterface
public interface ActionListener<T> {
    void onAction(T t);
}
