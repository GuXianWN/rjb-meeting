package com.guxian.common.utils;

import java.util.List;
import java.util.Random;

public class SomeUtils {
    private SomeUtils() {
    }

    private static final Random random = new Random();

    public static String addStringToArrayBetween(String str, String... array) {
        StringBuilder sb = new StringBuilder();
        for (String s : array) {
            sb.append(s).append(str);
        }
        return sb.substring(0, sb.length() - 1);
    }

    public static String randomString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    public static String randomCode() {
        //四位随机数
        Random ne=new Random();//实例化一个random的对象ne

        int x = ne.nextInt(9999 - 1000 + 1) + 1000;

        return String.valueOf(x);
    }
}
