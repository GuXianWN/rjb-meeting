package com.guxian.facecheck.service;

import com.guxian.facecheck.entity.UserFace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface UserFaceService {

    double compareFace(MultipartFile file);

    Page<UserFace> findAll(PageRequest of);

    Optional<UserFace> findByUserId(Long uid);

    boolean setUserFaceLockStatus(Long uid, boolean isLock);
}
