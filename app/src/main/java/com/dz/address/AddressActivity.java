package com.dz.address;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.dz.address.bean.AddressBean;
import com.dz.address.fragment.AddressChangeFragment;
import com.dz.address.fragment.AddressListFragment;
import com.dz.address.fragment.TypeinDialog;

import java.util.ArrayList;
import java.util.Stack;

/**
 * @Description 收货地址
 * Created by deng on 2018/8/29.
 */
public class AddressActivity extends Activity implements AddressListFragment.IAddressEdit,
        AddressChangeFragment.OnChangeCompleted, TypeinDialog.OnTagDefined{
    private static final String TAG = "AddressActivity";

    public static final int RESULT_SET_OK = 1 << 2;
    public static final int RESULT_EMPTY = 1 << 2 + 1;
    public static final String TYPE_NEW_ADDRESS = "type_new_address";
    public static final String TYPE_EDIT_ADDRESS = "type_edit_address";

    private boolean hasAddress;
    private Stack<Fragment> mFragments;
    private AddressListFragment addressListFragment;
    private AddressChangeFragment addressChangeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

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
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        showListFragment();
    }

    /**
     * 显示收货地址列表界面
     */
    private void showListFragment() {
        addressListFragment = new AddressListFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("hasAddress", hasAddress);
        addressListFragment.setArguments(bundle);
        showFragment(addressListFragment);
    }

    /**
     * 显示收货地址编辑界面
     */
    private void showDetailFragment(String type, AddressBean bean) {
        addressChangeFragment = new AddressChangeFragment();

        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        if (bean != null) {
            bundle.putParcelable("address", bean);
        }
        addressChangeFragment.setArguments(bundle);

        showFragment(addressChangeFragment);
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
                    .hide(preFragment)
                    .add(R.id.fragment_container, fragment)
                    .show(fragment)
                    .commitAllowingStateLoss();
        }

    }

    @Override
    public void onBackPressed() {
        if (mFragments != null && mFragments.size() > 1) {
            Fragment fragment = mFragments.peek();
            if (fragment instanceof AddressChangeFragment) {
                ((AddressChangeFragment) fragment).onBackPressed();
            } else {
                //won't come into this, in case sth awful happens...
                showPreFragment();
            }
        } else {
            setResult(hasAddress ? RESULT_SET_OK : RESULT_EMPTY);
            finish();
        }
    }

    /**
     * onBackPressed send to AddressChangeFragment, callback
     * @param isChanged is data edited
     * @param addressBean data
     */
    @Override
    public void onChangeCompleted(boolean isChanged, AddressBean addressBean) {
        //TODO:upload
        if (isChanged) {
            addressListFragment.onChangeCompleted(addressBean);
        }
        showPreFragment();
    }

    private void showPreFragment() {
        Fragment fragment1 = mFragments.pop();
        Fragment fragment2 = mFragments.peek();
        getFragmentManager()
                .beginTransaction()
                .remove(fragment1)
                .show(fragment2)
                .commitAllowingStateLoss();
        addressChangeFragment = null;
    }

    /**
     * 跳转编辑界面
     * @param type new or edit
     * @param addressList editable data
     */
    @Override
    public void onAddressEdit(String type, ArrayList<AddressBean> addressList, int position) {
        if (TextUtils.equals(type, TYPE_NEW_ADDRESS)) {
            showDetailFragment(type, null);
        } else {
            showDetailFragment(type, addressList.get(position));
        }
    }

    /**
     * 设置自定义标签的回调
     * @param tag new tag
     */
    @Override
    public void onTagDefined(String tag) {
        if (addressChangeFragment != null) {
            addressChangeFragment.setNewTag(tag);
        } else {
            Log.e(TAG, "onTagDefined: addressChangeFragment is null, in where r u doing this shit ???");
        }
    }
}
