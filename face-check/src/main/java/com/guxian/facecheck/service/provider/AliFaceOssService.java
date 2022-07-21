package com.guxian.facecheck.service.provider;

import com.guxian.common.exception.BizCodeEnum;
import com.guxian.common.exception.ServiceException;
import com.guxian.common.utils.CurrentUserSession;
import com.guxian.common.utils.FileCacheUtils;
import com.guxian.common.utils.SomeUtils;
import com.guxian.facecheck.entity.UserFace;
import com.guxian.facecheck.repo.UserFaceRepo;
import com.guxian.facecheck.service.CheckFaceExistService;
import com.guxian.facecheck.service.OSSForFaceService;
import com.guxian.facecheck.service.OssService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.Instant;
import java.util.Date;

@Service
@Slf4j
public class AliFaceOssService implements OSSForFaceService {

    private final OssService ossService;

    private final UserFaceRepo userFaceRepo;

    private final CheckFaceExistService checkFaceExistService;

    public AliFaceOssService(OssService ossService
            , UserFaceRepo userFaceRepo
            , CheckFaceExistService checkFaceExistService) {
        this.ossService = ossService;
        this.userFaceRepo = userFaceRepo;
        this.checkFaceExistService = checkFaceExistService;
    }


    @Override
    public String uploadFace(InputStream inputStream) {
        return uploadFace(inputStream, CurrentUserSession.getUserSession().getUserId());
    }

    @SneakyThrows
    @Override
    public String uploadFace(InputStream inputStream, Long userId) {
        String fileName = SomeUtils.buildFaceFileUUName();
        var fileCacheUtils = new FileCacheUtils();
        var file = fileCacheUtils.saveFile(inputStream, fileName);
        if (!checkFaceExistService.hasFace(file)) {
            log.info("{} delete",fileName);
            file.delete();
            throw new ServiceException(BizCodeEnum.NO_FACE_WAS_DETECTED);
        }

        String url = ossService.uploadObject(new FileInputStream(file), fileName);
        // 从数据库查询是否有该用户的face 信息 ， 如果有 则返回 对应userId ，否则返回 null 的 id （视为没有对应用户的信息）
        var userFace = userFaceRepo.findByUserId(userId).orElse(new UserFace().setId(null));
        //如果有把本地的删掉
        if (userFace.getFaceUrl()!=null){
            String ossUrl = SomeUtils.getFileNameFromOssUrl(userFace.getFaceUrl());
            File faceFile = fileCacheUtils.getFaceFile(ossUrl);
            faceFile.delete();
            log.info("delete face {}",ossUrl);
        }
        userFaceRepo.save(new UserFace()
                .setUserId(userId)
                .setFaceUrl(url)
                .setId(userFace.getId())
                .setCreateTime(Date.from(Instant.now())));
        return url;
    }


    @Override
    public File downloadFace(Long userId) {
        return ossService.downloadObject(SomeUtils.buildFaceFileName(userId));
    }

    @Override
    public File downloadFace() {
        return downloadFace(CurrentUserSession.getUserSession().getUserId());
    }

    @Override
    public Boolean hasFace(Long userId) {
        return ossService.hasObject(SomeUtils.buildFaceFileName(userId));
    }
}
