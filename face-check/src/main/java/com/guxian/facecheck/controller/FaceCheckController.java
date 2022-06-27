package com.guxian.facecheck.controller;

import com.guxian.common.entity.ResponseData;
import com.guxian.common.exception.BizCodeEnum;
import com.guxian.facecheck.service.FaceCompareService;
import com.guxian.facecheck.service.OSSForFaceService;
import com.guxian.facecheck.service.OssService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;

@RestController
@RequestMapping("/face")
public class FaceCheckController {
    private final OssService ossService;
    private final OSSForFaceService faceOss;
    private  FaceCompareService faceCompareService;

    @Value("${face-compare.minimum-confidence}")
    private double minimumConfidence = 0.9;

    public FaceCheckController(OssService ossService, OSSForFaceService faceOss) {
        this.ossService = ossService;
        this.faceOss = faceOss;
//        this.faceCompareService = faceCompareService;
    }

    @PostMapping("/compare")
    public ResponseData compareFace(File file) {
        return ResponseData.is(faceCompareService.checkFaceSimilarRate(file) >= minimumConfidence
        , BizCodeEnum.FACE_CONTRAST_INCONSISTENT);
    }

    @PostMapping("/")
    public ResponseData check(String url) {
        return ResponseData.success();
        //        var check = aliOssUploadService.checkFace(url);
//        return ResponseData.is(check);
    }

    //文件上传 ,参数为文件
    @PostMapping("/upload")
    public ResponseData uploadFace(@RequestParam("file") File file) {
        var check = faceOss.uploadFace(file);
        return ResponseData.is(StringUtils.hasText(check));
    }


    @GetMapping("/test/{url}")
    public ResponseData test(@PathVariable("url") String url) {
        return ResponseData.success(url);
    }

}
