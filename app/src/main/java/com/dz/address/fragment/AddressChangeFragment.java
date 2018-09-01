package com.dz.address.fragment;

import android.app.Fragment;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.dz.address.R;
import com.dz.address.bean.AddressBean;

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

/**
 * @Description 新增／编辑收货地址界面
 * Created by deng on 2018/8/29.
 */
public class AddressChangeFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "AddressChangeFragment";

    private static final String FILE_NAME = "province.json";

    private String type;
    private AddressBean editAddressBean;
    private List<String> provinceList = new ArrayList<>();
    private List<List<String>> cityList = new ArrayList<>();
    private List<List<List<String>>> thirdList = new ArrayList<>();

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
        view.findViewById(R.id.ll_choose_location).setOnClickListener(this);

        if (editAddressBean != null) {
            //TODO:set default data
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_choose_location:
                showPickerView();
                break;
        }
    }

    /**
     * 弹出选择器
     */
    private void showPickerView() {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(getActivity(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = provinceList.get(options1) +
                        cityList.get(options1).get(options2) +
                        thirdList.get(options1).get(options2).get(options3);

            }
        })
                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
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




//        try {
//            JSONArray array = new JSONArray(json);
//            if (array.length() != 0) {
//                for (int index = 0; index < array.length(); index ++) {
//                    //第一层联动
//                    JSONObject object = array.getJSONObject(index);
//                    provinceList.add(object.optString("name"));
//                    JSONArray secondArray = object.optJSONArray("child");
//                    if (secondArray.length() != 0) {
//                        List<String> secondTemp = new ArrayList<>();
//                        List<List<String>> thirdTemp1 = new ArrayList<>();
//                        for (int second = 0; second < secondArray.length(); second ++) {
//                            //第二层联动
//                            JSONObject secondObject = secondArray.optJSONObject(second);
//                            secondTemp.add(secondObject.optString("name"));
//                            JSONObject thirdObject = secondObject.optJSONObject("child");
//                            List<String> thirdTemp2 = new ArrayList<>();
//                            Iterator<String> iterator = thirdObject.keys();
//                            while (iterator.hasNext()) {
//                                thirdTemp2.add(thirdObject.optString(iterator.next()));
//                            }
//                            thirdTemp1.add(thirdTemp2);
//                        }
//                        cityList.add(secondTemp);
//                        thirdList.add(thirdTemp1);
//                    }
//                }
//            } else {
//                Log.e(TAG, "parseJson: wrong when parsing city data : array.length() is 0!");
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//            Log.e(TAG, "parseJson: wrong when parsing city data : e=" + e);
//        }
    }
}
