package com.guxian.facecheck.service;

import java.io.File;

public interface UploadService {
    /**
     * 上传图片
     * @param file 图片文件
     * @return 图片URL
     * */
    String upload(File file);
}
