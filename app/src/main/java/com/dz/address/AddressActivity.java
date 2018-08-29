package com.dz.address;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.dz.address.fragment.AddressChangeFragment;
import com.dz.address.fragment.AddressListFragment;

import java.util.Stack;

/**
 * @Description 收货地址
 * Created by deng on 2018/8/29.
 */
public class AddressActivity extends Activity {
    private static final String TAG = "AddressActivity";

    public static final int RESULT_SET_OK = 1 << 2;
    public static final int RESULT_EMPTY = 1 << 2 + 1;

    private boolean hasAddress;
    private Stack<Fragment> mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initIntent();
        init();
    }

    private void initIntent() {
        Intent intent = getIntent();
        hasAddress = intent.getBooleanExtra("hasAddress", true);
    }

    private void init() {
        mFragments = new Stack<>();
        ((TextView) findViewById(R.id.tv_title)).setText("收货地址");
        showListFragment();
    }

    /**
     * 显示收货地址列表界面
     */
    private void showListFragment() {
        Fragment fragment = new AddressListFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("hasAddress", hasAddress);
        fragment.setArguments(bundle);
        showFragment(fragment);
    }

    /**
     * 显示收货地址编辑界面
     */
    private void showDetailFragment() {
        Fragment fragment = new AddressChangeFragment();
        showFragment(fragment);
    }

    /**
     * 1.新fragment入栈
     * 2.show new fragment
     * @param fragment new fragment
     */
    private void showFragment(Fragment fragment) {
        Fragment preFragment = null;
        if (mFragments.size() != 0) {
            preFragment = mFragments.peek();
        }
        mFragments.push(fragment);

        if (preFragment == null) {
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .show(fragment)
                    .commitAllowingStateLoss();
        } else {
            getFragmentManager()
                    .beginTransaction()
                    .remove(preFragment)
                    .add(R.id.fragment_container, fragment)
                    .show(fragment)
                    .commitAllowingStateLoss();
        }

    }

    @Override
    public void onBackPressed() {
        if (mFragments != null && mFragments.size() > 1) {
            Fragment fragment1 = mFragments.pop();
            Fragment fragment2 = mFragments.peek();
            getFragmentManager()
                    .beginTransaction()
                    .remove(fragment1)
                    .add(R.id.fragment_container, fragment2)
                    .show(fragment2)
                    .commitAllowingStateLoss();
        } else {
            setResult(hasAddress ? RESULT_SET_OK : RESULT_EMPTY);
            finish();
        }
    }
}
