package com.twisty.lootlib;

import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * Project : Loot<br>
 * Created by twisty on 2017/5/16.<br>
 */

public class FileUtil {
    public static File createTmpFile() throws IOException {
        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM + "/Camera");
        File file = new File(path, "/LOOT_" + System.currentTimeMillis() + ".jpg");
        if (!path.exists()) path.mkdirs();
        if (!file.exists()) file.createNewFile();
        return file;
    }
}
