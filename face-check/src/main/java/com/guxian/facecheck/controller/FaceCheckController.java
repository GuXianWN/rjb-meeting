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
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

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

    //文件上传 ,参数为文件
    @SneakyThrows
    @PostMapping("/upload")
    public ResponseData uploadFace(@RequestParam("file") MultipartFile files) {
        if (files.isEmpty() || files.getSize() != 1) {
            throw new ServiceException(BizCodeEnum.NUMBER_OF_UPLOADED_FILE_NOT_ONE);
        }

        // mkdir
        String fileName = files.getOriginalFilename();  // 文件名
        File dest = new File(tmpDir + '/' + fileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }

        byte[] bytes = files.getBytes();
        Path path = Paths.get(dest.getPath());
        Files.write(path, bytes);
        var check = faceOss.uploadFace(dest);
        return ResponseData.is(StringUtils.hasText(check), BizCodeEnum.UPLOAD_ERROR);
    }

    @PostMapping("/demo")
    public ResponseData demo(@RequestParam MultipartFile file) {
        //校验格式
        if (!StringUtils.endsWithIgnoreCase(file.getOriginalFilename(), ".jpg")){
            return ResponseData.error("文件格式错误");
        }
        //康康是不是头
        String url = faceOss.demo(file);
        return ResponseData.success().data("url",url);
    }
}
