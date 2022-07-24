package com.guxian.facecheck.service;


import java.io.InputStream;

public interface OssService {
    /**
     * 上传图片
     *
     * @param file 图片文件
     * @param name 文件名
     * @return 图片URL
     */

    String uploadObject(InputStream file, String name);

    void deleteObject(String filename);


    InputStream downloadObject(String filename);

    Boolean hasObject(String filename);

}
