package com.dz.address.base;

import android.app.Application;

import com.dz.address.utils.PreferenceUtil;
import com.squareup.leakcanary.LeakCanary;

/**
 * @Description application
 * Created by deng on 2018/9/4.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        PreferenceUtil.getInstance().init(this);
    }
}
