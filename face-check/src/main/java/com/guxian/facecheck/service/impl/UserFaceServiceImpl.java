package com.guxian.facecheck.service.impl;

import com.guxian.common.exception.BizCodeEnum;
import com.guxian.common.exception.ServiceException;
import com.guxian.common.utils.CurrentUserSession;
import com.guxian.common.utils.FileCacheUtils;
import com.guxian.common.utils.SomeUtils;
import com.guxian.facecheck.repo.UserFaceRepo;
import com.guxian.facecheck.service.CheckFaceExistService;
import com.guxian.facecheck.service.FaceCompareService;
import com.guxian.facecheck.service.UserFaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

@Service
@Slf4j
public class UserFaceServiceImpl implements UserFaceService {
    @Resource
    private UserFaceRepo userFaceRepo;
    @Resource
    private FaceCompareService faceCompareService;
    @Resource
    private CheckFaceExistService checkFaceExistService;

    @Value("${oss.face-filename-suffix}")
    private String faceFilenameSuffix = ".jpg";

    @Override
    public double compareFace(MultipartFile file) {
        log.info("人脸比对开始");
        //检查上传的是不是人
        var fileCacheUtils = new FileCacheUtils();
        File paramFaceImg = fileCacheUtils.saveFile(file, SomeUtils.buildFaceFileUUName());
        if (!checkFaceExistService.hasFace(paramFaceImg)){
            log.info("face delete {}",paramFaceImg.getName());
            paramFaceImg.delete();
            throw new ServiceException(BizCodeEnum.NO_FACE_WAS_DETECTED);
        }
        //是人的话 检查本地有无用户的人脸
        var user = userFaceRepo.findByUserId(CurrentUserSession.getUserSession().getUserId());
        var faceUrl = user.orElseThrow(() -> new ServiceException(BizCodeEnum.USER_FACE_NOT_EXIST))
                .getFaceUrl();
        if (!StringUtils.hasText(faceUrl)) {
            throw new ServiceException(BizCodeEnum.USER_FACE_NOT_EXIST);
        }
        String ossFaceName = SomeUtils.getFileNameFromOssUrl(faceUrl);
        File remoteUserFaceImg = fileCacheUtils.getFaceFile(ossFaceName);
        if (!remoteUserFaceImg.canRead()){
            log.info("本地无用户人脸");
            try {
                URL url = new URL(faceUrl);
                fileCacheUtils.saveFileFromRemote(url,ossFaceName);
            } catch (Exception e) {
                throw new ServiceException(BizCodeEnum.USER_FACE_NOT_EXIST);
            }
        }

        var rate = faceCompareService
                .checkFaceSimilarRate(remoteUserFaceImg, paramFaceImg);
        log.warn("current rate is {}=======", rate);

        paramFaceImg.delete();
        log.info("face delete {}",paramFaceImg.getName());
        return rate;
    }
}
