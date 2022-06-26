package com.guxian.facecheck.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URL;

@Service
public interface FaceCheckService {
    double checkFaceSimilarRate(File faceFile);

    boolean hasFace(File file);

    boolean hasFace(URL url);

}
