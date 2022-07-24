package com.guxian.facecheck.controller;

import com.guxian.common.entity.ResponseData;
import com.guxian.common.exception.BizCodeEnum;
import com.guxian.common.exception.ServiceException;
import com.guxian.facecheck.service.FaceCompareService;
import com.guxian.facecheck.service.OSSForFaceService;
import com.guxian.facecheck.service.UserFaceService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/face")
@Slf4j
public class FaceCheckController {
    private final OSSForFaceService faceOss;
    private final FaceCompareService faceCompareService;
    private final UserFaceService userFaceService;

    @Value("${face-compare.minimum-confidence}")
    private double minimumConfidence = 0.7;

    @Value("${oss.face-filename-suffix}")
    private String faceFilenameSuffix = ".jpg";

    public FaceCheckController(
            OSSForFaceService faceOss,
            FaceCompareService faceCompareService,
            UserFaceService userFaceService) {
        this.faceOss = faceOss;
        this.faceCompareService = faceCompareService;
        this.userFaceService = userFaceService;
    }

    @PostMapping("/compare")
    public ResponseData compareFace(@RequestPart(name = "file") MultipartFile file) {
        if (file.isEmpty()) {
            throw new ServiceException(BizCodeEnum.NUMBER_OF_UPLOADED_FILE_NOT_ONE);
        }

        if (!StringUtils.endsWithIgnoreCase(file.getOriginalFilename(), faceFilenameSuffix)) {
            return ResponseData.error("文件格式错误");
        }

        double rate = userFaceService.compareFace(file);

        return ResponseData.is(rate >= minimumConfidence
                , BizCodeEnum.FACE_CONTRAST_INCONSISTENT).data(rate);
    }

    @PostMapping("/upload")
    @SneakyThrows
    public ResponseData upload(@RequestPart(value = "file") MultipartFile file) {
        if (file.isEmpty()) {
            throw new ServiceException(BizCodeEnum.NUMBER_OF_UPLOADED_FILE_NOT_ONE);
        }
        //校验格式
        if (!StringUtils.endsWithIgnoreCase(file.getOriginalFilename(), faceFilenameSuffix)) {
            return ResponseData.error("文件格式错误");
        }

        //上传
        String url = faceOss.uploadFace(file.getInputStream());
        return ResponseData.success().data("url", url);
    }

    @GetMapping("/is/{id}")
    public ResponseData hasFace(@PathVariable Long id) {
        return ResponseData.success().data(faceOss.hasFace(id));
    }
}
