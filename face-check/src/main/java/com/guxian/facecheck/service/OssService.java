package com.guxian.facecheck.service;

import org.springframework.stereotype.Service;

import java.io.File;

@Service
public interface OssService {
    /**
     * 上传图片
     *
     * @param file 图片文件
     * @param name 文件名
     * @return 图片URL
     */

    String uploadObject(File file, String name);




    void deleteObject(String filename);


    File downloadObject(String filename);
}
