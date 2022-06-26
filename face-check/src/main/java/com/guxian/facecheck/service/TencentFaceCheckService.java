package com.guxian.facecheck.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;

import java.io.File;
import java.net.URL;

public class TencentFaceCheckService implements FaceCheckService {
    private OSS ossClient;

    @Override
    public double checkFaceSimilarRate(File faceFile) {

        return 0;
    }

    @Override
    public boolean hasFace(File file) {
        return false;
    }

    @Override
    public boolean hasFace(URL url) {
        return false;
    }
}
