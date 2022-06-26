package com.guxian.facecheck.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public interface OSSForFaceService {
    /**
     * @param file   脸图片
     * @param userId 给谁上传的 ，为空则给当前用户
     * @return 暂时为空
     */
    String uploadFace(File file, Long userId);

    String uploadFace(File file);

    File downloadFace(Long userId);

    File downloadFace();
}
