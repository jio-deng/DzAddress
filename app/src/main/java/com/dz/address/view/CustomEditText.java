package com.dz.address.view;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.KeyEvent;

/**
 * @Description 自定义edittext , 识别手机返回键 , 隐藏软键盘的同时finish页面
 * Created by deng on 2018/9/4.
 */
public class CustomEditText extends AppCompatEditText{

    public CustomEditText(Context context) {
        super(context);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            // Do your thing here
            if (mListener != null) {
                mListener.myonkeyPreIme(keyCode, event);
            }
        }
        return super.onKeyPreIme(keyCode, event);


    }
    private onKeyPreImeLinster mListener;

    public interface onKeyPreImeLinster {
        void  myonkeyPreIme(int keyCode, KeyEvent event);
    }

    public void setOnKeyPreImeLister( onKeyPreImeLinster lister){
        this.mListener = lister;
    }
}
