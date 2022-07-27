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
        // 从数据库查询是否有该用户的face 信息 ， 如果有 则返回 对应userId ，否则返回 null 的 id （视为没有对应用户的信息）
        //insert or modify
        var userFace = userFaceRepo.findByUserId(userId).orElse(new UserFace().setId(null));

        //如果被锁住，则禁止修改
        if (Boolean.TRUE.equals(userFace.getLocked())) {
            throw new ServiceException(BizCodeEnum.USER_FACE_LOCKED);
        }

        String fileName = SomeUtils.buildFaceFileName(userId);
        var tmpFile = FileCacheUtils.saveFaceFile(inputStream, fileName);
        //检测是否存在人脸，如果不存在，进行删除tmp文件
        if (!checkFaceExistService.hasFace(tmpFile)) {
            log.info("{} delete", fileName);
            tmpFile.deleteOnExit();
            throw new ServiceException(BizCodeEnum.NO_FACE_WAS_DETECTED);
        }


        //上传文件
        String url = ossService.uploadObject(new FileInputStream(tmpFile), fileName);

        userFaceRepo.save(new UserFace()
                .setUserId(userId)
                .setFaceUrl(url)
                .setId(userFace.getId())
                .setCreateTime(Date.from(Instant.now())));
        return url;
    }

    @Override
    public File downloadFace(Long userId) {
        var inputStream = ossService.downloadObject(SomeUtils.buildFaceFileName(userId));
        FileCacheUtils fileCacheUtils = new FileCacheUtils();
        var file = fileCacheUtils.saveFileFromRemote(inputStream, SomeUtils.buildFaceFileName(userId));
        if (file == null || !file.canRead()) {
            throw new ServiceException(BizCodeEnum.USER_FACE_NOT_EXIST);
        }
        return file;
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
