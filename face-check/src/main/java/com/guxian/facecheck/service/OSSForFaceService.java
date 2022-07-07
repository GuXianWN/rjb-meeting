package com.guxian.facecheck.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;

public interface OSSForFaceService {
    /**
     * @param inputStream   脸图片
     * @param userId 给谁上传的 ，为空则给当前用户
     * @return 暂时为空
     */
    String uploadFace(InputStream inputStream, Long userId);

    String uploadFace(InputStream inputStream);

    File downloadFace(Long userId);

    File downloadFace();

    Boolean hasFace(Long userId);

}
