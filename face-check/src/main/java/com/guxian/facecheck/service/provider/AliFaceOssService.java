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
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import static org.aspectj.weaver.tools.cache.SimpleCacheFactory.path;

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
        String fileName = SomeUtils.buildFileName(userId);

        var fileCacheUtils = new FileCacheUtils("/face");
        var file = fileCacheUtils.saveFile(inputStream, fileName);
        if (!checkFaceExistService.hasFace(file)) {
            throw new ServiceException(BizCodeEnum.NO_FACE_WAS_DETECTED);
        }

        String url = ossService.uploadObject(new FileInputStream(file), fileName);
        // 从数据库查询是否有该用户的face 信息 ， 如果有 则返回 对应userId ，否则返回 null 的 id （视为没有对应用户的信息）
        var userFace = userFaceRepo.findByUserId(userId).orElse(new UserFace().setId(null));
        userFaceRepo.save(new UserFace()
                .setUserId(userId)
                .setFaceUrl(url)
                .setId(userFace.getId())
                .setCreateTime(Date.from(Instant.now())));

        return url;
    }


    @Override
    public File downloadFace(Long userId) {
        return ossService.downloadObject(SomeUtils.buildFileName(userId));
    }

    @Override
    public File downloadFace() {
        return downloadFace(CurrentUserSession.getUserSession().getUserId());
    }
}
