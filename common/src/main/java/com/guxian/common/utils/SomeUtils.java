package com.guxian.common.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;

import javax.annotation.meta.When;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class SomeUtils {
    private SomeUtils() {
    }

    private static final Random random = new Random();

    private static String FACE_FILENAME_PREFIX = "FACE_";

    private static String FACE_FILENAME_SUFFIX = ".png";

    @Value("${oss.face-filename-prefix}")
    public void setFaceFilenamePrefix(String faceFilenamePrefix) {
        SomeUtils.FACE_FILENAME_PREFIX = faceFilenamePrefix;
    }

    @Value("${oss.face-filename-suffix}")
    public void setFaceFilenameSuffix(String faceFilenameSuffix) {
        SomeUtils.FACE_FILENAME_SUFFIX = faceFilenameSuffix;
    }

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


    public static InputStream getResourceAsStream(String filename){
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
    }

    public static String getResource(String filename) {
        var resource = SomeUtils.class.getClassLoader().getResource(filename);
        Assert.notNull(resource, "\n\n\n\n警告！face-check的核心文件未装载成功！！！！！！！\n\n\n\n");
        Assert.hasText(resource.getPath(), "\n\n\n\n警告！face-check的核心文件未装载成功！！！！！！！\n\n\n\n");
        var path = resource.getPath();
        if (path.charAt(0) == '/') {
            path = path.substring(1);
        }
        if(path.startsWith("file:/")){
            path = path.substring("file:/".length());
        }
        return path;
    }


    public static String buildFileName(Long userId) {
        return SomeUtils.FACE_FILENAME_PREFIX + userId + SomeUtils.FACE_FILENAME_SUFFIX;
    }
}
