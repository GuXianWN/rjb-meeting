package com.guxian.facecheck.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URL;

@Service
public interface CheckFaceService {
    double checkFaceSimilarRate(File faceFile);

    boolean hasFace(File file);

    boolean hasFace(URL url);

}
