package com.guxian.facecheck.controller;

import com.guxian.common.entity.ResponseData;
import com.guxian.common.exception.BizCodeEnum;
import com.guxian.common.exception.ServiceException;
import com.guxian.common.utils.CurrentUserSession;
import com.guxian.common.utils.FileCacheUtils;
import com.guxian.common.utils.SomeUtils;
import com.guxian.facecheck.repo.UserFaceRepo;
import com.guxian.facecheck.service.FaceCompareService;
import com.guxian.facecheck.service.OSSForFaceService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.Normalizer;
import java.util.UUID;

@RestController
@RequestMapping("/face")
@Slf4j
public class FaceCheckController {
    private final OSSForFaceService faceOss;
    private final FaceCompareService faceCompareService;
    private final UserFaceRepo userFaceRepo;

    @Value("${face-compare.minimum-confidence}")
    private double minimumConfidence = 0.7;

    @Value("${oss.face-filename-suffix}")
    private String faceFilenameSuffix = ".png";

    public FaceCheckController(
            OSSForFaceService faceOss,
            FaceCompareService faceCompareService,
            UserFaceRepo userFaceRepo) {
        this.faceOss = faceOss;
        this.faceCompareService = faceCompareService;
        this.userFaceRepo = userFaceRepo;
    }

    @PostMapping("/compare")
    @SneakyThrows
    //todo 文件夹中保存的文件命名为ULR 相关参数，减少从服务器的下载。
    public ResponseData compareFace(@RequestParam(name = "file") MultipartFile file) {
        if (file.isEmpty()) {
            throw new ServiceException(BizCodeEnum.NUMBER_OF_UPLOADED_FILE_NOT_ONE);
        }

        var user = userFaceRepo.findByUserId(CurrentUserSession.getUserSession().getUserId());

        var faceUrl = user.orElseThrow(() -> new ServiceException(BizCodeEnum.USER_FACE_NOT_EXIST))
                .getFaceUrl();

        if (!StringUtils.hasText(faceUrl)) {
            throw new ServiceException(BizCodeEnum.USER_FACE_NOT_EXIST);
        }


        var fileCacheUtils = new FileCacheUtils("/face");
        var remoteUserFaceImg = fileCacheUtils.saveFileFromRemote(new URL(faceUrl)
//                , SomeUtils.buildFileName(CurrentUserSession.getUserSession().getUserId()));
                , UUID.randomUUID() + ".png");
        log.info("remoteUserFaceImg ok{}", remoteUserFaceImg.getAbsolutePath());
        var paramFaceImg = fileCacheUtils.saveFile(file,
//                SomeUtils.buildFileName(CurrentUserSession.getUserSession().getUserId()));
                UUID.randomUUID() + ".png");
        log.info("paramFaceImg ok{}", paramFaceImg.getAbsolutePath());
        var rate = faceCompareService
                .checkFaceSimilarRate(remoteUserFaceImg, paramFaceImg);
        log.warn("current rate is {}=======", rate);
        return ResponseData.is(rate >= minimumConfidence
                , BizCodeEnum.FACE_CONTRAST_INCONSISTENT);
    }

    @PostMapping("/upload")
    @SneakyThrows
    public ResponseData upload(@RequestParam(name = "file") MultipartFile file) {
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
