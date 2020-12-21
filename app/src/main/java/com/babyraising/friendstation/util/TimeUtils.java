package com.babyraising.friendstation.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
    public static String getShowTime(long second){
        String nowChatTime = "";
        long currentHours = TimeUtils.getHours(second);
        long currentMins = TimeUtils.getMins(second);
        long currentSeconds = TimeUtils.getSeconds(second);
        if (currentHours != 0) {
            if (currentHours < 10) {
                nowChatTime = nowChatTime + "0" + currentHours + ":";
            } else {
                nowChatTime = nowChatTime + +currentHours + ":";
            }
        }

        if (currentMins != 0) {
            if (currentMins < 10) {
                nowChatTime = nowChatTime + "0" + currentMins + ":";
            } else {
                nowChatTime = nowChatTime + +currentMins + ":";
            }
        } else {
            nowChatTime = nowChatTime + "00:";
        }

        if (currentSeconds != 0) {
            if (currentSeconds < 10) {
                nowChatTime = nowChatTime + "0" + currentSeconds;
            } else {
                nowChatTime = nowChatTime + currentSeconds;
            }
        }

        return nowChatTime;
    }

    public static long getHours(long second) {//计算秒有多少小时
        long h = 00;
        if (second > 3600) {
            h = second / 3600;
        }
        return h;
    }

    public static long getMins(long second) {//计算秒有多少分
        long d = 00;
        long temp = second % 3600;
        if (second > 3600) {
            if (temp != 0) {
                if (temp > 60) {
                    d = temp / 60;
                }
            }
        } else {
            d = second / 60;
        }
        return d;
    }

    public static long getSeconds(long second) {//计算秒有多少秒
        long s = 0;
        long temp = second % 3600;
        if (second > 3600) {
            if (temp != 0) {
                if (temp > 60) {
                    if (temp % 60 != 0) {
                        s = temp % 60;
                    }
                } else {
                    s = temp;
                }
            }
        } else {
            if (second % 60 != 0) {
                s = second % 60;
            }
        }
        return s;
    }

    public static String timeStamp2Date(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");//要转换的时间格式
        Date date;
        try {
            date = sdf.parse(sdf.format(time));
            return sdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}