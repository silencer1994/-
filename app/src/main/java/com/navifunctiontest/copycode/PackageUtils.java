package com.navifunctiontest.copycode;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;

import java.io.File;

/**
 * android 包工具类
 * Created by lhy on 2016/4/14.
 */
public class PackageUtils {
    /**
     * 检查sd卡是否存在
     *
     * @return True if external storage is removable (like an SD card), false
     *         otherwise.
     */
    public static boolean isExternalStorageRemovable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) { // 9
            return Environment.isExternalStorageRemovable();
        }
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }
    /**
     * 获取/Android/data/ 包名/cache/"
     *
     */
    public static File tils(Context context) {
        final String cacheDir = "/Android/data/" + context.getPackageName()
                + "/cache/";
        return new File(Environment.getExternalStorageDirectory().getPath()
                + cacheDir);
    }
    /**
     * 获取手机内存卡中的data目录
     * @return
     */
    public static String getCacheDirectory() {
        String path = Environment.getExternalStorageDirectory()
                .getAbsolutePath();

        return path + "/Android/data";
    }
    /**
     * 获取mnt/Android/data/com.……
     * @param context
     * @return
     */
    public static String getApplicationCacheDirectory(Context context) {
        return getCacheDirectory() + "/" + getApplicationPakageName(context);
    }
    /**
     * 获取手机内存中的data/data/com.……
     * @param context
     * @return
     */
    public static String getApplicationDataDirectory(Context context) {
        String path = Environment.getDataDirectory().getAbsolutePath();

        return path + "/data/" + getApplicationPakageName(context);
    }
    /**
     * 获取包名
     * @param context
     * @return
     */
    public static String getApplicationPakageName(Context context) {
        try {
            PackageInfo pkg = context.getPackageManager().getPackageInfo(
                    context.getApplicationContext().getPackageName(), 0);
            String pkgName = pkg.packageName;

            return pkgName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
