package com.guxian.facecheck.service.provider;

import com.aliyun.oss.model.PutObjectRequest;
import com.guxian.common.utils.CurrentUserSession;
import com.guxian.facecheck.entity.UserFace;
import com.guxian.facecheck.repo.UserFaceRepo;
import com.guxian.facecheck.service.OSSForFaceService;
import com.guxian.facecheck.service.OssService;

import java.io.File;
import java.time.Instant;
import java.util.Date;

public class AliFaceOssService implements OSSForFaceService {

    private final OssService ossService;
    private static final String FACE_PIC_PREFIX = "FACE_";
    private static final String FACE_PIC_SUFFIX = ".png";
    private final UserFaceRepo userFaceRepo;


    public AliFaceOssService(OssService ossService, UserFaceRepo userFaceRepo) {
        this.ossService = ossService;
        this.userFaceRepo = userFaceRepo;
    }


    @Override
    public String uploadFace(File file) {
        return uploadFace(file, CurrentUserSession.getUserSession().getUserId());
    }

    @Override
    public String uploadFace(File file, Long userId) {
        var url = ossService.uploadObject(file, buildFileName(userId));

        // 从数据库查询是否有该用户的face 信息 ， 如果有 则返回 对应userId ，否则返回 null 的 id （视为没有对应用户的信息）
        var userFace = userFaceRepo.findByUserId(userId).orElse(new UserFace().setId(null));
        return userFaceRepo.save(new UserFace()
                        .setUserId(userId)
                        .setFaceUrl(url)
                        .setId(userFace.getId())
                        .setCreateTime(Date.from(Instant.now())))
                .getFaceUrl();
    }

    @Override
    public File downloadFace(Long userId) {
        return ossService.downloadObject(buildFileName(userId));
    }

    @Override
    public File downloadFace() {
        return downloadFace(CurrentUserSession.getUserSession().getUserId());
    }

    private String buildFileName(Long userId) {
        return FACE_PIC_PREFIX + userId + FACE_PIC_SUFFIX;
    }
}
