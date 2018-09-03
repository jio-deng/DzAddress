package com.dz.address.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dz.address.R;
import com.dz.address.adapter.AddressListAdapter;
import com.dz.address.bean.AddressBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.dz.address.AddressActivity.TYPE_EDIT_ADDRESS;
import static com.dz.address.AddressActivity.TYPE_NEW_ADDRESS;

/**
 * @Description 收货地址列表界面
 * Created by deng on 2018/8/29.
 */
public class AddressListFragment extends Fragment implements View.OnClickListener, AddressListAdapter.IAddressAdapter {
    private static final String TAG = "AddressListFragment";

    private RecyclerView mRvList;
    private AddressListAdapter adapter;
    private ArrayList<AddressBean> addressList = new ArrayList<>();
    private IAddressEdit callback;
    private boolean hasAddress;

    public interface IAddressEdit {
        void onAddressEdit(String type, ArrayList<AddressBean> addressList, int position);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (IAddressEdit) activity;
    }

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
//        mRvList.addItemDecoration(new CommonItemDecoration(getActivity()));
        mRvList.setAdapter(adapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_address_new:
                if (addressList.size() >= 10) {
                    Toast.makeText(getActivity(), "最多有10个收货地址，无法继续添加", Toast.LENGTH_SHORT).show();
                } else {
                    callback.onAddressEdit(TYPE_NEW_ADDRESS, null, -1);
                }
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
        callback.onAddressEdit(TYPE_EDIT_ADDRESS, addressList, position);
    }

    /**
     * 显示删除dialog
     * @param addressList data
     * @param position position
     */
    @Override
    public void gotoDelete(final ArrayList<AddressBean> addressList, final int position) {
        new HintTitleDialog.HintTitleDialogBuilder(getActivity())
                .title("确认删除")
                .content("确定删除这条收货信息吗？")
                .button1("取消", new HintTitleDialog.OnBtnClickListener() {
                    @Override
                    public void onClick(HintTitleDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .button2("确定", new HintTitleDialog.OnBtnClickListener() {
                    @Override
                    public void onClick(HintTitleDialog dialog) {
                        addressList.remove(position);
                        adapter.notifyDataSetChanged();
                        //TODO:need to upload
                        dialog.dismiss();
                    }
                }).build().show(getActivity().getFragmentManager(), "address_delete_dialog");
    }

    /**
     * 新增/编辑后的数据
     * @param bean data
     */
    public void onChangeCompleted(AddressBean bean) {
        if (addressList.size() == 0) {
            addressList.add(bean);
        } else {
            boolean isEdit = false;
            boolean isDefault = bean.isDefault;
            if (isDefault) {
                for (int index = 0; index < addressList.size(); index ++) {
                    addressList.get(index).isDefault = false;
                    if (!isEdit && addressList.get(index).id == bean.id) {
                        addressList.set(index, bean);
                        addressList.get(index).isDefault = true;//TODO:wtf???
                        isEdit = true;
                    }
                }
            } else {
                for (int index = 0; index < addressList.size(); index ++) {
                    if (addressList.get(index).id == bean.id) {
                        addressList.set(index, bean);
                        isEdit = true;
                        break;
                    }
                }
            }

            if (!isEdit) {
                addressList.add(bean);
            }
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * fake data
     */
    private void initFakeData() {
        JSONArray array = new JSONArray();
        for (int i = 0; i < 3; i ++) {
            JSONObject object = new JSONObject();
            try {
                object.put("id", i + 1);
                object.put("name", "邓子明");
                object.put("phone", "18840832890");
                object.put("firstAddress", "山东省济南市高新区");
                object.put("secondAddress", "这句话一共有十个字哦这句话一共有十个字哦这句话一共有十个字哦这句话一共有十个字哦这句话一共有十个字哦");
                object.put("isDefault", i == 0);
                object.put("tags", "公司");
                array.put(object);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "initFakeData: sth is wrong when initializing fake data, e=" + e);
            }
        }

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
