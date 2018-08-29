package com.dz.address.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dz.address.R;
import com.dz.address.adapter.AddressListAdapter;
import com.dz.address.adapter.CommonItemDecoration;
import com.dz.address.bean.AddressBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @Description 收货地址列表界面
 * Created by deng on 2018/8/29.
 */
public class AddressListFragment extends Fragment implements View.OnClickListener, AddressListAdapter.IAddressAdapter {
    private static final String TAG = "AddressListFragment";

    private RecyclerView mRvList;
    private AddressListAdapter adapter;
    private ArrayList<AddressBean> addressList;
    private boolean hasAddress;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null && (hasAddress = bundle.getBoolean("hasAddress", true))) {
            //TODO:get data
            initFakeData();
        } else {
            //TODO:show no-data layout
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_address_list, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        view.findViewById(R.id.tv_address_new).setOnClickListener(this);
        mRvList = view.findViewById(R.id.recycler_address_list);
        if (adapter == null) {
            adapter = new AddressListAdapter(this);
        }
        adapter.setAddressList(addressList);
        mRvList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvList.addItemDecoration(new CommonItemDecoration(getActivity()));
        mRvList.setAdapter(adapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_address_new:
                //TODO:
                break;
        }
    }

    /**
     * 跳转修改界面
     * @param addressList data
     * @param position position
     */
    @Override
    public void gotoEdit(ArrayList<AddressBean> addressList, int position) {

    }

    /**
     * 显示删除dialog
     * @param addressList data
     * @param position position
     */
    @Override
    public void gotoDelete(ArrayList<AddressBean> addressList, int position) {

    }

    /**
     * fake data
     */
    private void initFakeData() {
        JSONArray array = new JSONArray();
        for (int i = 0; i < 10; i ++) {
            JSONObject object = new JSONObject();
            try {
                object.put("id", i + 1);
                object.put("name", "邓子明");
                object.put("phone", "18840832890");
                object.put("address", "这句话一共有十个字哦这句话一共有十个字哦这句话一共有十个字哦这句话一共有十个字哦这句话一共有十个字哦");
                object.put("isDefault", i == 0);
                object.put("tags", "公司");
                array.put(object);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "initFakeData: sth is wrong when initializing fake data, e=" + e);
            }
        }

        addressList = new ArrayList<>();
        try {
            for (int i = 0; i < array.length(); i ++) {
                JSONObject obj = array.getJSONObject(i);
                addressList.add(new AddressBean(obj));
            }
            Log.d(TAG, "onCreate: addressList=" + addressList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
