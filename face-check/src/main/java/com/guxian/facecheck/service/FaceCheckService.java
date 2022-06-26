package com.guxian.facecheck.service;

import org.springframework.stereotype.Service;

import java.io.File;

@Service
public interface FaceCheckService {
    double checkFaceSimilarRate(File faceFileA);



}
