package com.babyraising.friendstation.util;

import java.util.Random;

public class RandomUtil {
    public static String getRandom() {
        Random rand = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 1; i <= 32; i++) {
            int randNum = rand.nextInt(9) + 1;
            String num = randNum + "";
            sb = sb.append(num);
        }
        String random = String.valueOf(sb);

        return random;
    }
}
