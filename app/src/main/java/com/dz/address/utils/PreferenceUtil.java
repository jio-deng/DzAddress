package com.dz.address.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * @Description sp helper
 * Created by deng on 2018/9/4.
 */
public class PreferenceUtil {
    public static final String ADDRESS_TAG_SELF_DEFINE = "address_tag_self_define";

    private SharedPreferences sp;

    private static PreferenceUtil instance;

    public static PreferenceUtil getInstance() {
        if (instance == null)
            instance = new PreferenceUtil();
        return instance;
    }

    public void init(Context context) {
        sp = context.getSharedPreferences("address_list", Context.MODE_PRIVATE);
    }

    public void writeToPreference(String key, String value) {
        if (TextUtils.isEmpty(value))
            return;
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String readString(String key) {
        return sp.getString(key, "");
    }
}
