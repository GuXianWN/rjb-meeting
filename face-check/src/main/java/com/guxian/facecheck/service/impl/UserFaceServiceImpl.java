package com.guxian.facecheck.service.impl;

import com.guxian.common.exception.BizCodeEnum;
import com.guxian.common.exception.ServiceException;
import com.guxian.common.utils.CurrentUserSession;
import com.guxian.common.utils.FileCacheUtils;
import com.guxian.common.utils.SomeUtils;
import com.guxian.facecheck.entity.UserFace;
import com.guxian.facecheck.repo.UserFaceRepo;
import com.guxian.facecheck.service.CheckFaceExistService;
import com.guxian.facecheck.service.FaceCompareService;
import com.guxian.facecheck.service.OSSForFaceService;
import com.guxian.facecheck.service.UserFaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Optional;

@Service
@Slf4j
public class UserFaceServiceImpl implements UserFaceService {
    private final UserFaceRepo userFaceRepo;

    private final FaceCompareService faceCompareService;

    private final CheckFaceExistService checkFaceExistService;

    private final OSSForFaceService ossForFaceService;

    @Value("${oss.face-filename-suffix}")
    private String faceFilenameSuffix = ".jpg";

    public UserFaceServiceImpl(UserFaceRepo userFaceRepo, FaceCompareService faceCompareService, CheckFaceExistService checkFaceExistService, OSSForFaceService ossForFaceService) {
        this.userFaceRepo = userFaceRepo;
        this.faceCompareService = faceCompareService;
        this.checkFaceExistService = checkFaceExistService;
        this.ossForFaceService = ossForFaceService;
    }

    @Override
    public double compareFace(MultipartFile file) {
        log.info("人脸比对开始");

        //检查上传的是不是人
        var fileCacheUtils = new FileCacheUtils();

        //存储临时文件（用户上传的人脸）
        var paramFaceImg = fileCacheUtils.saveFaceFile(file, SomeUtils.buildFaceFileUUName());

        //用于对比的远程人脸（或者从本地缓存获取）
        File userFaceImg;

        if (!checkFaceExistService.hasFace(paramFaceImg)) {
            log.info("face delete {}", paramFaceImg.getName());
            paramFaceImg.delete();
            throw new ServiceException(BizCodeEnum.NO_FACE_WAS_DETECTED);
        }
        //是人的话 检查本地有无用户的人脸

        var fileName = SomeUtils.buildFaceFileName(CurrentUserSession.getUserSession().getUserId());

        userFaceImg = fileCacheUtils.getFaceFile(fileName);

        //本地文件无法读取成功，尝试远程拉取

        if (!userFaceImg.canRead()) {
            log.info("本地无用户人脸");
            var user = userFaceRepo.findByUserId(CurrentUserSession.getUserSession().getUserId());
            var remoteFaceUrl = user.orElseThrow(() -> new ServiceException(BizCodeEnum.USER_FACE_NOT_EXIST))
                    .getFaceUrl();
            //无远程URL，该用户未上传FACE！
            if (!StringUtils.hasText(remoteFaceUrl)) {
                throw new ServiceException(BizCodeEnum.USER_FACE_NOT_EXIST);
            }

            //下载
            userFaceImg = ossForFaceService.downloadFace(CurrentUserSession.getUserSession().getUserId());
        }

        var rate = faceCompareService
                .checkFaceSimilarRate(userFaceImg, paramFaceImg);
        log.warn("current rate is {}=======", rate);

        paramFaceImg.delete();

        log.info("face delete {}", paramFaceImg.getName());
        return rate;
    }

    @Override
    public Page<UserFace> findAll(PageRequest of) {
        return userFaceRepo.findAll(of);
    }

    @Override
    public Optional<UserFace> findByUserId(Long uid) {
        return userFaceRepo.findByUserId(uid);
    }

    @Override
    public boolean setUserFaceLockStatus(Long uid, boolean isLocked) {
        var userFace = userFaceRepo.findByUserId(uid).orElseThrow(() -> new ServiceException(BizCodeEnum.USER_FACE_NOT_EXIST));
        userFaceRepo.save(userFace.setLocked(isLocked));
        return true;
    }

    @Override
    public boolean getUserFaceLockStatus(Long uid) {
        return userFaceRepo.findByUserId(uid)
                .orElseThrow(() -> new ServiceException(BizCodeEnum.USER_NOT_EXIST))
                .getLocked();
    }
}
