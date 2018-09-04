package com.dz.address.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description check string
 * Created by deng on 2018/9/4.
 */
public class StringUtil {
    /**
     * 是否含有特殊字符验证
     *
     * @param str string
     * @return 验证通过返回true
     */
    public static boolean isContainSpecial(String str) {
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find();
    }
}
