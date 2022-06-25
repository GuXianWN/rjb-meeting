package com.guxian.common.utils;

import java.util.List;
import java.util.Objects;
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


    @SafeVarargs
    public static <T> T getNotNullValue(T... objects) {
        for (T object : objects) {
            if (object != null) {
                return object;
            }
        }
        return null;
    }


    public static String getResource(String path) {
        return Objects.requireNonNull(SomeUtils.class.getClassLoader().getResource(path)).getPath();
    }

}
