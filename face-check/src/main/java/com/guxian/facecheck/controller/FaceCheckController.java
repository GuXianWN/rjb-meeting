package com.guxian.facecheck.controller;

import com.guxian.common.entity.ResponseData;
import com.guxian.common.exception.BizCodeEnum;
import com.guxian.common.exception.ServiceException;
import com.guxian.facecheck.service.FaceCompareService;
import com.guxian.facecheck.service.OSSForFaceService;
import com.guxian.facecheck.service.OssService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/face")
public class FaceCheckController {
    private final OssService ossService;
    private final OSSForFaceService faceOss;
    private final FaceCompareService faceCompareService;

    @Value("${face-compare.minimum-confidence}")
    private double minimumConfidence = 0.9;

    private String tmpDir = System.getProperty("user.dir").replace('\\', '/') + "/src/main/resources/tmp";

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
}
