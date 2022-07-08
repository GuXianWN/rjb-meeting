package com.guxian.facecheck.service;

import org.springframework.web.multipart.MultipartFile;

public interface UserFaceService {

    double compareFace(MultipartFile file);
}
