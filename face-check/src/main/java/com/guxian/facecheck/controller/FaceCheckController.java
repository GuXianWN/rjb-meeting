//package com.guxian.facecheck.controller;
//
//import com.guxian.common.entity.ResponseData;
//import com.guxian.facecheck.service.AliOssUploadService;
//import lombok.Setter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.util.StringUtils;
//import org.springframework.web.bind.annotation.*;
//
//import java.io.File;
//
//@RestController
//@RequestMapping("/face-check")
//@Setter(onMethod_ = @Autowired)
//public class FaceCheckController {
//
//
//    private AliOssUploadService aliOssUploadService;
//
//    @PostMapping("/")
//    public ResponseData check(String url) {
//        var check = aliOssUploadService.checkFace(url);
//        return ResponseData.is(check);
//    }
//
//
//    //文件上传 ,参数为文件
//    @PostMapping("/upload")
//    public ResponseData upload(@RequestParam("file") File file) {
//        var check = aliOssUploadService.upload(file);
//        return ResponseData.is(StringUtils.hasText(check));
//    }
//
////    test
//    @GetMapping("/test")
//    public ResponseData test(String url) {
//        return ResponseData.success(url);
//    }
//}
