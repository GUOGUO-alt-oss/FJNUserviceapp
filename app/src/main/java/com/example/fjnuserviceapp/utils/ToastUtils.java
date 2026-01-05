package com.example.fjnuserviceapp.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * 吐司工具类，避免重复弹出
 */
public class ToastUtils {
    private static Toast mToast;

    public static void showShort(Context context, String msg) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        mToast.show();
    }

    public static void showLong(Context context, String msg) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        mToast.show();
    }
}