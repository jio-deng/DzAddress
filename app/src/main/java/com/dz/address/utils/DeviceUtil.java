package com.dz.address.utils;

import android.content.Context;

/**
 * @Description device util
 * Created by deng on 2018/9/3.
 */
public class DeviceUtil {
    public static int dpTopx(Context context, int dpValue) {
        final float scale = context.getApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int dpTopx(Context context, float dpValue) {
        final float scale = context.getApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
