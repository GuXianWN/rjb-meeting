package com.guxian.facecheck.controller;

import com.guxian.common.entity.ResponseData;
import com.guxian.facecheck.service.AliOssUploadService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;

@RestController
@RequestMapping("/face-check")
public class FaceCheckController {
    private final AliOssUploadService aliOssUploadService;

    public FaceCheckController(AliOssUploadService aliOssUploadService) {
        this.aliOssUploadService = aliOssUploadService;
    }

    @PostMapping("/")
    public ResponseData check(String url) {
        var check = aliOssUploadService.checkFace(url);
        return ResponseData.is(check);
    }

    //文件上传 ,参数为文件
    @PostMapping("/upload")
    public ResponseData upload(@RequestParam("file") File file) {
        var check = aliOssUploadService.upload(file);
        return ResponseData.is(StringUtils.hasText(check));
    }

    @GetMapping("/test/{url}")
    public ResponseData test(@PathVariable("url") String url) {
        return ResponseData.success(url);
    }
}
