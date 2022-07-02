package com.guxian.facecheck.controller;

import com.guxian.common.entity.ResponseData;
import com.guxian.common.exception.BizCodeEnum;
import com.guxian.common.exception.ServiceException;
import com.guxian.facecheck.service.FaceCompareService;
import com.guxian.facecheck.service.OSSForFaceService;
import com.guxian.facecheck.service.OssService;
import com.guxian.facecheck.service.provider.AliOssService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;

@RestController
@RequestMapping("/face")
@Slf4j
public class FaceCheckController {
    private final OssService ossService;
    private final OSSForFaceService faceOss;
    private final FaceCompareService faceCompareService;

    @Resource
    private AliOssService aliOssService;

    @Value("${face-compare.minimum-confidence}")
    private double minimumConfidence = 0.9;

    @Value("${oss.face-filename-suffix}")
    private String faceFilenameSuffix = ".png";

    private String tmpDir = System.getProperty("user.dir").replace('\\', '/') + "/userFace";

    public FaceCheckController(OssService ossService,
                               OSSForFaceService faceOss,
                               FaceCompareService faceCompareService) {
        this.ossService = ossService;
        this.faceOss = faceOss;
        this.faceCompareService = faceCompareService;
    }

    @PostMapping("/compare")
    public ResponseData compareFace(File file) {
        return ResponseData.is(faceCompareService.checkFaceSimilarRate(file) >= minimumConfidence
                , BizCodeEnum.FACE_CONTRAST_INCONSISTENT);
    }


    @PostMapping("/upload")
    @SneakyThrows
    public ResponseData upload(@RequestParam MultipartFile file) {
        if (file.isEmpty()) {
            throw new ServiceException(BizCodeEnum.NUMBER_OF_UPLOADED_FILE_NOT_ONE);
        }
        //校验格式
        if (!StringUtils.endsWithIgnoreCase(file.getOriginalFilename(), faceFilenameSuffix)) {
            return ResponseData.error("文件格式错误");
        }
        String url = faceOss.uploadFace(file.getInputStream());
        return ResponseData.success().data("url", url);
    }
}
