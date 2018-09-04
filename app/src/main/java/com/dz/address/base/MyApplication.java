package com.dz.address.base;

import android.app.Application;

import com.dz.address.utils.PreferenceUtil;

/**
 * @Description application
 * Created by deng on 2018/9/4.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PreferenceUtil.getInstance().init(this);
    }
}
