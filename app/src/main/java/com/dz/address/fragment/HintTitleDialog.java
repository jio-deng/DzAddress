package com.dz.address.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dz.address.R;
import com.dz.address.utils.DeviceUtil;

/**
 * @Description dialog with title or not
 * Created by deng on 2018/9/3.
 */
public class HintTitleDialog extends DialogFragment implements View.OnClickListener{
    private String title;
    private String content;
    private String btn1;
    private String btn2;
    private int textSize;
    private int textColor1;
    private int textColor2;
    private int dialogWidth;
    private OnBtnClickListener btn1Listener;
    private OnBtnClickListener btn2Listener;
    private int backGroundId = 0;
    private boolean isBlack;
    private boolean titleBold;
    private int contentColor;
    private int btnPadding;
    private int contentPaddingTop;

    public HintTitleDialog() {
    }

    @SuppressLint("ValidFragment")
    public HintTitleDialog(HintTitleDialogBuilder builder) {
        this.title = builder.title;
        this.content = builder.content;
        this.btn1 = builder.btn1;
        this.btn2 = builder.btn2;
        this.textSize = builder.textSize;
        this.textColor1 = builder.textColor1;
        this.textColor2 = builder.textColor2;
        this.dialogWidth = builder.dialogWidth;
        this.btn1Listener = builder.btn1Listener;
        this.btn2Listener = builder.btn2Listener;
        this.backGroundId = builder.backgroundId;
        this.isBlack = builder.isBlack;
        this.titleBold = builder.titleBold;
        this.contentColor = builder.contentColor;
        this.btnPadding = builder.btnPadding;
        this.contentPaddingTop = builder.contentPaddingTop;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.common_dialog);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_title, null);
        if (backGroundId != 0) {
            view.findViewById(R.id.rl_hint_background).setBackgroundResource(backGroundId);
        }else if (isBlack) {
            view.findViewById(R.id.rl_hint_background).setBackgroundResource(R.drawable.check_login_dialog_bg);
        }
        LinearLayout mLlDialog = view.findViewById(R.id.dialog_ll);
        TextView mTvTitle = view.findViewById(R.id.hint_dialog_title);
        TextView mTvContent = view.findViewById(R.id.hint_dialog_content);
        TextView mTvBtn1 = view.findViewById(R.id.hint_dialog_btn1);
        TextView mTvBtn2 = view.findViewById(R.id.hint_dialog_btn2);
        mTvBtn1.setOnClickListener(this);
        mTvBtn2.setOnClickListener(this);
        if (dialogWidth != 0) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(DeviceUtil.dpTopx(getActivity(), dialogWidth), LinearLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            mLlDialog.setLayoutParams(params);
        }
        if (!TextUtils.isEmpty(title)) {
            mTvTitle.setText(title);
        }else {
            mTvTitle.setVisibility(View.GONE);
        }
        if (titleBold){
            mTvTitle.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        }
        mTvContent.setText(content);
        if (TextUtils.isEmpty(btn1)) {
            mTvBtn1.setVisibility(View.GONE);
        } else {
            mTvBtn1.setText(btn1);
        }
        if (TextUtils.isEmpty(btn2)) {
            mTvBtn2.setVisibility(View.GONE);
        } else {
            mTvBtn2.setText(btn2);
        }
        if (textSize != 0) {
            mTvBtn1.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
            mTvBtn2.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        }
        if (textColor1 != 0 && textColor2 != 0) {
            mTvBtn1.setTextColor(getResources().getColor(textColor1));
            mTvBtn2.setTextColor(getResources().getColor(textColor2));
        }
        if (contentColor != 0){
            mTvContent.setTextColor(getResources().getColor(contentColor));
        }
        if (btnPadding != 0){
            mTvBtn1.setPadding(0,getResources().getDimensionPixelOffset(btnPadding),0,getResources().getDimensionPixelOffset(btnPadding));
        }
        if (contentPaddingTop != 0){
            mTvContent.setPadding(0,getResources().getDimensionPixelOffset(contentPaddingTop),0,getResources().getDimensionPixelOffset(R.dimen.value_20dp));
        }
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        //layoutParams.height = getActivity().getResources().getDimensionPixelOffset(R.dimen.value_168dp);
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.gravity = Gravity.TOP;
        layoutParams.x = 0;
        layoutParams.y = 0;

        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);

        return dialog;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.hint_dialog_btn1:
                if (btn1Listener != null)
                    btn1Listener.onClick(this);
                break;
            case R.id.hint_dialog_btn2:
                if (btn2Listener != null)
                    btn2Listener.onClick(this);
                break;
        }
    }

    public static class HintTitleDialogBuilder {
        private Context mContext;
        private String title;
        private String content;
        private String btn1;
        private String btn2;
        private int textSize = 0;
        private int textColor1 = 0;
        private int textColor2 = 0;
        private int dialogWidth = 0;
        private OnBtnClickListener btn1Listener;
        private OnBtnClickListener btn2Listener;
        private int backgroundId = 0;
        private boolean isBlack = false;
        private boolean titleBold = false;
        private int contentColor = 0;
        private int btnPadding = 0;
        private int contentPaddingTop = 0;


        public HintTitleDialogBuilder(Context context) {
            mContext = context;
        }

        public HintTitleDialogBuilder title(String title) {
            this.title = title;
            return this;
        }
        public HintTitleDialogBuilder title(int title) {
            this.title = mContext.getString(title);
            return this;
        }

        public HintTitleDialogBuilder titleBold() {
            this.titleBold = true;
            return this;
        }
        public HintTitleDialogBuilder content(String content) {
            this.content = content;
            return this;
        }
        public HintTitleDialogBuilder content(int content) {
            this.content = mContext.getString(content);
            return this;
        }

        public HintTitleDialogBuilder button1(String name, OnBtnClickListener btn1Listener) {
            this.btn1 = name;
            this.btn1Listener = btn1Listener;
            return this;
        }
        public HintTitleDialogBuilder button1(int name, OnBtnClickListener btn1Listener) {
            this.btn1 = mContext.getString(name);
            this.btn1Listener = btn1Listener;
            return this;
        }

        public HintTitleDialogBuilder button2(String name, OnBtnClickListener btn2Listener) {
            this.btn2 = name;
            this.btn2Listener = btn2Listener;
            return this;
        }
        public HintTitleDialogBuilder button2(int name, OnBtnClickListener btn2Listener) {
            this.btn2 = mContext.getString(name);
            this.btn2Listener = btn2Listener;
            return this;
        }

        //背景是否为黑色遮挡
        public HintTitleDialogBuilder isBlack(boolean isBlack) {
            this.isBlack = isBlack;
            return this;
        }

        public HintTitleDialogBuilder background(int backgroundId) {
            this.backgroundId = backgroundId;
            return this;
        }

        public HintTitleDialogBuilder textSize(int textSize) {
            this.textSize = textSize;
            return this;
        }

        public HintTitleDialogBuilder textColor(int textColor1, int textColor2) {
            this.textColor1 = textColor1;
            this.textColor2 = textColor2;
            return this;
        }

        public HintTitleDialogBuilder contentColor(int contentColor) {
            this.contentColor = contentColor;
            return this;
        }


        public HintTitleDialogBuilder dialogWidth(int dialogWidth) {
            this.dialogWidth = dialogWidth;
            return this;
        }

        public HintTitleDialogBuilder btnPadding(int btnPadding) {
            this.btnPadding = btnPadding;
            return this;
        }

        public HintTitleDialogBuilder contentPaddingTop(int contentPaddingTop) {
            this.contentPaddingTop = contentPaddingTop;
            return this;
        }

        public HintTitleDialog build() {
            return new HintTitleDialog(this);
        }
    }

    public interface OnBtnClickListener {
        void onClick(HintTitleDialog dialog);
    }

}
