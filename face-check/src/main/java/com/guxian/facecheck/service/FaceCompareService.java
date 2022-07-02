package com.guxian.facecheck.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;

public interface FaceCompareService {
    double checkFaceSimilarRate(File faceFileA,File faceFileB);
}
