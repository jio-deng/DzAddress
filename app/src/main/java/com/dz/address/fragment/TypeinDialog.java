package com.dz.address.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.dz.address.R;
import com.dz.address.utils.DeviceUtil;
import com.dz.address.utils.KeyboardUtil;
import com.dz.address.utils.StringUtil;
import com.dz.address.view.CustomEditText;

/**
 * @Description type in sth in this dialog
 * Created by deng on 2018/9/4.
 */
public class TypeinDialog extends DialogFragment implements View.OnClickListener {
    private CustomEditText mEtTags;

    private OnTagDefined onTagDefined;

    public interface OnTagDefined {
        void onTagDefined(String tag);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        onTagDefined = (OnTagDefined) activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.Theme_CustomDialog);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_type_in, null, false);
        initViews(view);
        dialog.setContentView(view);

        dialog.setCanceledOnTouchOutside(true);

        //设置宽度为屏宽、靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.width = DeviceUtil.getDeviceScreenWidth(getActivity());
        window.setAttributes(wlp);

        return dialog;
    }

    private void initViews(View view) {
        view.findViewById(R.id.comment_cancel).setOnClickListener(this);
        view.findViewById(R.id.comment_commit).setOnClickListener(this);
        mEtTags = view.findViewById(R.id.comment_edittext);
        mEtTags.setOnKeyPreImeLister(new CustomEditText.onKeyPreImeLinster() {
            @Override
            public void myonkeyPreIme(int keyCode, KeyEvent event) {
                KeyboardUtil.closeKeybord(mEtTags, getActivity());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.comment_cancel:
                KeyboardUtil.closeKeybord(mEtTags, getActivity());
                getDialog().dismiss();
                break;
            case R.id.comment_commit:
                checkTypeInTag();
                break;
        }
    }

    /**
     * 检验输入是否合理
     */
    private void checkTypeInTag() {
        String tag = mEtTags.getText().toString();
        if (TextUtils.isEmpty(tag)) {
            Toast.makeText(getActivity(), "输入不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (tag.length() > 5) {
            Toast.makeText(getActivity(), "长度不能超过5！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtil.isContainSpecial(tag)) {
            Toast.makeText(getActivity(), "不能含有特殊字符！", Toast.LENGTH_SHORT).show();
            return;
        }
        //TODO:upload?
        onTagDefined.onTagDefined(tag);
        KeyboardUtil.closeKeybord(mEtTags, getActivity());
        getDialog().dismiss();
    }
}
