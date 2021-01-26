package com.babyraising.friendstation.util;

import android.os.Build;

public class TypeUtil {
    public static boolean isHuawei() {
        String manufacturer = Build.MANUFACTURER;
        //这个字符串可以自己定义,例如判断华为就填写huawei,魅族就填写meizu
        if ("huawei".equalsIgnoreCase(manufacturer)) {
            return true;
        }
        return false;
    }
}
