package com.guxian.facecheck.controller;

import com.guxian.common.entity.ResponseData;
import com.guxian.facecheck.service.OSSForFaceService;
import com.guxian.facecheck.service.OssService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;

@RestController
@RequestMapping("/face")
public class FaceCheckController {
    private final OssService ossService;
    private final OSSForFaceService faceOss;
    public FaceCheckController(OssService ossService, OSSForFaceService faceOss) {
        this.ossService = ossService;
        this.faceOss = faceOss;
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
