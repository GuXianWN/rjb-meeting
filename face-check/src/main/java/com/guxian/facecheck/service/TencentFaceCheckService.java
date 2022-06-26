package com.guxian.facecheck.service;

import java.io.File;
import java.net.URL;

public class TencentFaceCheckService implements FaceCheckService {
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
