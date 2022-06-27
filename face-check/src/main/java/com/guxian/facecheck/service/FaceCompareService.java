package com.guxian.facecheck.service;

import org.springframework.stereotype.Service;

import java.io.File;

@Service
public interface FaceCompareService {
    double checkFaceSimilarRate(File faceFileA);
}
