package com.guxian.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.io.InputStream;
import java.util.Random;
import java.util.UUID;

@Slf4j
public class SomeUtils {
    private SomeUtils() {
    }

    private static final Random random = new Random();

    private static String FACE_FILENAME_PREFIX = "FACE_";

    private static String FACE_FILENAME_SUFFIX = ".jpg";

    private static String FILENAME_PREFIX = "PORTRAIT_";

    private static String FILENAME_SUFFIX = ".jpg";

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


    public static InputStream getResourceAsStream(String filename) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
    }

    public static String getFileNameFromOssUrl(String url){
        String[] split = url.split("/");
        return split[split.length - 1];
    }

    public static String getResource(String filename) {
        var resource = SomeUtils.class.getClassLoader().getResource(filename);
        assert resource != null;
        var path = resource.getPath();
        if (path.charAt(0) == '/') {
            path = path.substring(1);
        }
        if (path.startsWith("file:/")) {
            path = path.substring("file:/".length());
        }
        return path;
    }


    public static String buildFaceFileName(Long userId) {
        return SomeUtils.FACE_FILENAME_PREFIX + userId + SomeUtils.FACE_FILENAME_SUFFIX;
    }

    public static String buildFaceFileUUName() {
        return SomeUtils.FACE_FILENAME_PREFIX + UUID.randomUUID() + SomeUtils.FACE_FILENAME_SUFFIX;
    }

    public static String buildPortrait(Long userId) {
        return SomeUtils.FILENAME_PREFIX + userId + FILENAME_SUFFIX;
    }

    public static String getPath() {
        StringBuilder path = new StringBuilder(SomeUtils.getResource("application.yaml"));
        String[] split = path.toString().split("/");
        path = new StringBuilder();
        for (int i = 0; i < split.length - 4; i++) {
            path.append(split[i]).append("/");
        }
        return path.toString();
    }
}
