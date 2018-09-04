package com.dz.address.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.dz.address.R;
import com.dz.address.bean.AddressBean;
import com.dz.address.utils.KeyboardUtil;
import com.dz.address.utils.PreferenceUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.dz.address.AddressActivity.TYPE_EDIT_ADDRESS;
import static com.dz.address.utils.PreferenceUtil.ADDRESS_TAG_SELF_DEFINE;

/**
 * @Description 新增／编辑收货地址界面
 * Created by deng on 2018/8/29.
 */
public class AddressChangeFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "AddressChangeFragment";

    private static final String FILE_NAME = "province.json";

    private EditText mEtPersonName;
    private EditText mEtPhone;
    private TextView mTvAddress;
    private EditText mEtDetailAddress;
    private RadioButton mRbIsDefault;
    private TextView mTvChoose;
    /** tag start **/
    private LinearLayout mLlTag4;
    private TextView mTvTag1;
    private TextView mTvTag2;
    private TextView mTvTag3;
    private TextView mTvTag4;
    private TextView mTvTagEdit;
    private boolean isDefined = false;
    /** tag end **/
    private HintTitleDialog dialog;

    private String type;
    private boolean isChanged = false;
    private AddressBean editAddressBean;
    private OnChangeCompleted onChangeCompleted;

    private List<String> provinceList = new ArrayList<>();
    private List<List<String>> cityList = new ArrayList<>();
    private List<List<List<String>>> thirdList = new ArrayList<>();

    public interface OnChangeCompleted {
        //TODO:upload?
        void onChangeCompleted(boolean isChanged, AddressBean addressBean);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        onChangeCompleted = (OnChangeCompleted) activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        type = bundle.getString("type");
        if (TextUtils.equals(type, TYPE_EDIT_ADDRESS)) {
            editAddressBean = bundle.getParcelable("address");
        }
        parseJson(getJson(FILE_NAME));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_address_change, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        mEtPersonName = view.findViewById(R.id.et_address_change_name);
        mEtPhone = view.findViewById(R.id.et_address_change_phone);
        mTvAddress = view.findViewById(R.id.tv_location);
        mEtDetailAddress = view.findViewById(R.id.et_address_change_address);
        mRbIsDefault = view.findViewById(R.id.radio_is_default);
        mRbIsDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = !editAddressBean.isDefault;
                mRbIsDefault.setChecked(flag);
                editAddressBean.isDefault = flag;
                isChanged = true;
            }
        });
        mTvTag1 = view.findViewById(R.id.tv_address_tag_1);
        mTvTag2 = view.findViewById(R.id.tv_address_tag_2);
        mTvTag3 = view.findViewById(R.id.tv_address_tag_3);
        mTvTag4 = view.findViewById(R.id.tv_address_tag_4);
        mTvTagEdit = view.findViewById(R.id.tv_address_tag_edit);
        mLlTag4 = view.findViewById(R.id.ll_address_tag_4);

        mTvChoose = view.findViewById(R.id.tv_choose_loca);
        mEtPersonName.addTextChangedListener(textWatcher);
        mEtPhone.addTextChangedListener(textWatcher);
        mEtDetailAddress.addTextChangedListener(textWatcher);
        mTvTag1.setOnClickListener(this);
        mTvTag2.setOnClickListener(this);
        mTvTag3.setOnClickListener(this);
        mTvTag4.setOnClickListener(this);
        mTvTagEdit.setOnClickListener(this);
        mTvChoose.setOnClickListener(this);
        view.findViewById(R.id.tv_save).setOnClickListener(this);

        if (editAddressBean != null) {
            //TODO:set default data
            mEtPersonName.setText(editAddressBean.name);
            mEtPhone.setText(editAddressBean.phone);
            mTvAddress.setText(editAddressBean.firstAddress);
            mEtDetailAddress.setText(editAddressBean.secondAddress);
            mRbIsDefault.setChecked(editAddressBean.isDefault);
            String selfDefine = PreferenceUtil.getInstance().readString(ADDRESS_TAG_SELF_DEFINE);
            if (!TextUtils.isEmpty(selfDefine)) {
                mTvTag4.setText(selfDefine);
                isDefined = true;
            }
            setTagsShow(editAddressBean.tags);

        } else {
            editAddressBean = new AddressBean();
        }
    }

    /**
     * 加载传进的tag
     * @param tag editAddressBean.tag
     */
    private void setTagsShow(String tag) {
        if (TextUtils.isEmpty(tag))
            return;

        String selfDefine = PreferenceUtil.getInstance().readString(ADDRESS_TAG_SELF_DEFINE);
        if (TextUtils.equals(selfDefine, tag)) {
            setTagShow(R.id.tv_address_tag_4);
            mTvTagEdit.setVisibility(View.VISIBLE);
            mLlTag4.setBackgroundResource(R.drawable.bg_circle_orange_and_black);
            return;
        }

        switch (tag) {
            case "公司":
                setTagShow(R.id.tv_address_tag_1);
                break;
            case "家":
                setTagShow(R.id.tv_address_tag_2);
                break;
            case "学校":
                setTagShow(R.id.tv_address_tag_3);
                break;
        }
    }

    /**
     * tag点击显示
     * @param id 控件id
     */
    private void setTagShow(int id) {
        switch (id) {
            case R.id.tv_address_tag_1:
                mTvTag1.setBackgroundResource(R.drawable.bg_circle_orange);
                mTvTag2.setBackgroundResource(R.drawable.bg_black_circle);
                mTvTag3.setBackgroundResource(R.drawable.bg_black_circle);
                mLlTag4.setBackgroundResource(R.drawable.bg_black_circle);
                mTvTagEdit.setVisibility(View.GONE);
                break;
            case R.id.tv_address_tag_2:
                mTvTag1.setBackgroundResource(R.drawable.bg_black_circle);
                mTvTag2.setBackgroundResource(R.drawable.bg_circle_orange);
                mTvTag3.setBackgroundResource(R.drawable.bg_black_circle);
                mLlTag4.setBackgroundResource(R.drawable.bg_black_circle);
                mTvTagEdit.setVisibility(View.GONE);
                break;
            case R.id.tv_address_tag_3:
                mTvTag1.setBackgroundResource(R.drawable.bg_black_circle);
                mTvTag2.setBackgroundResource(R.drawable.bg_black_circle);
                mTvTag3.setBackgroundResource(R.drawable.bg_circle_orange);
                mLlTag4.setBackgroundResource(R.drawable.bg_black_circle);
                mTvTagEdit.setVisibility(View.GONE);
                break;
            case R.id.tv_address_tag_4:
                if (isDefined) {
                    mTvTag1.setBackgroundResource(R.drawable.bg_black_circle);
                    mTvTag2.setBackgroundResource(R.drawable.bg_black_circle);
                    mTvTag3.setBackgroundResource(R.drawable.bg_black_circle);
                    mLlTag4.setBackgroundResource(R.drawable.bg_circle_orange_and_black);
                    mTvTagEdit.setVisibility(View.VISIBLE);
                } else {
                    showTag4EditDialog();
                }
                break;
        }
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            isChanged = true;
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_choose_loca:
                showPickerView();
                break;
            case R.id.tv_save:
                if (isChanged) {
                    //TODO:检查数据完整性
                    editAddressBean.name = mEtPersonName.getText().toString();
                    editAddressBean.phone = mEtPhone.getText().toString();
                    editAddressBean.firstAddress = mTvAddress.getText().toString();
                    editAddressBean.secondAddress = mEtDetailAddress.getText().toString();
                    editAddressBean.isDefault = mRbIsDefault.isChecked();
                    isChanged = false;
                    onChangeCompleted.onChangeCompleted(true, editAddressBean);
                } else {
                    onChangeCompleted.onChangeCompleted(false, null);
                }
                break;
            case R.id.tv_address_tag_1:
            case R.id.tv_address_tag_2:
            case R.id.tv_address_tag_3:
            case R.id.tv_address_tag_4:
                setTagShow(v.getId());
                break;
            case R.id.tv_address_tag_edit:
                showTag4EditDialog();
                break;
        }
    }

    private void showTag4EditDialog() {
        KeyboardUtil.closeKeybord(mTvTag4, getActivity());
        //TODO edit dialog show
    }

    /**
     * on back pressed
     * case 1 : show dialog
     * case 2 : hide dialog
     */
    public void onBackPressed() {
        if (isChanged) {
            if (dialog == null || !dialog.isVisible()) {
                dialog = new HintTitleDialog.HintTitleDialogBuilder(getActivity())
                        .content(R.string.address_change_dialog_content)
                        .button1("取消", new HintTitleDialog.OnBtnClickListener() {
                            @Override
                            public void onClick(HintTitleDialog dialog) {
                                dialog.dismiss();
                            }
                        })
                        .button2("确定", new HintTitleDialog.OnBtnClickListener() {
                            @Override
                            public void onClick(HintTitleDialog dialog) {
                                dialog.dismiss();
                                onChangeCompleted.onChangeCompleted(false, null);
                            }
                        }).build();
                dialog.show(getActivity().getFragmentManager(), "address_delete_dialog");
            } else {
                dialog.dismiss();
            }
        } else {
            //未进行修改
            onChangeCompleted.onChangeCompleted(false, null);
        }
    }

    /**
     * 弹出选择器
     */
    private void showPickerView() {
        KeyboardUtil.closeKeybord(mTvChoose, getActivity());
        OptionsPickerView pvOptions = new OptionsPickerBuilder(getActivity(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String firstAddress = provinceList.get(options1) +
                        cityList.get(options1).get(options2) +
                        thirdList.get(options1).get(options2).get(options3);
                mTvAddress.setText(firstAddress);
                editAddressBean.firstAddress = firstAddress;
                isChanged = true;
            }
        })
                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .setDecorView((ViewGroup) getActivity().getWindow().getDecorView().findViewById(android.R.id.content))
                .build();
        pvOptions.setTitleText("请选择所在城市");
        pvOptions.setPicker(provinceList, cityList, thirdList);//三级选择器
        pvOptions.show();
    }

    /**
     * 获取province.json中的城市数据
     * @param fileName province.json
     * @return json
     */
    private String getJson(String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        AssetManager assetManager = getActivity().getAssets();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    /**
     * 解析城市信息
     * @param json json
     */
    private void parseJson(String json) {

        try {
            JSONObject firstObject = new JSONObject(json);
            Iterator<String> firstKeys = firstObject.keys();
            while (firstKeys.hasNext()) {
                String firstKey = firstKeys.next();
                provinceList.add(firstKey);
                JSONObject secondObject = firstObject.optJSONObject(firstKey);

                List<String> secondTemp = new ArrayList<>();
                List<List<String>> thirdTemp = new ArrayList<>();
                Iterator<String> secondKeys = secondObject.keys();
                while (secondKeys.hasNext()) {
                    String secondKey = secondKeys.next();
                    secondTemp.add(secondKey);

                    JSONArray thirdArray = secondObject.optJSONArray(secondKey);
                    List<String> thirdData = new ArrayList<>();
                    for (int index = 0; index < thirdArray.length(); index ++) {
                        thirdData.add((String) thirdArray.get(index));
                    }
                    thirdTemp.add(thirdData);
                }

                cityList.add(secondTemp);
                thirdList.add(thirdTemp);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "parseJson: wrong when parsing city data : e=" + e);
        }
    }
}
