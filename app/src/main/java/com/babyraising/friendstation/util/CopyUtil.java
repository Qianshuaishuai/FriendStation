package com.babyraising.friendstation.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

public class CopyUtil {
    public static void copy(Context context, String s) {
        // 获取系统剪贴板
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建一个剪贴数据集，包含一个普通文本数据条目（需要复制的数据）
        ClipData clipData = ClipData.newPlainText(null, s);
        // 把数据集设置（复制）到剪贴板
        clipboard.setPrimaryClip(clipData);
    }

}