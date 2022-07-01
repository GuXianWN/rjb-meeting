package com.guxian.facecheck.service;

import org.springframework.stereotype.Service;

import java.io.File;

public interface FaceCompareService {
    double checkFaceSimilarRate(File faceFileA);
}
