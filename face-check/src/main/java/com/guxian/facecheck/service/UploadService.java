package com.guxian.facecheck.service;

import java.io.File;

public interface UploadService {
    /**
     * 上传图片
     *
     * @param file 图片文件
     * @param name 文件名
     * @return 图片URL
     */

    String upload(File file, String name);

    /**
     *
     * @param file 脸图片
     * @param userId 给谁上传的 ，为空则给当前用户
     * @return 暂时为空
     */
    String uploadFace(File file, Long userId);

    String uploadFace(File file);
}
