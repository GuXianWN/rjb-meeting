package com.guxian.facecheck.service.provider;

import com.guxian.common.exception.BizCodeEnum;
import com.guxian.common.exception.ServiceException;
import com.guxian.common.utils.CurrentUserSession;
import com.guxian.facecheck.entity.UserFace;
import com.guxian.facecheck.repo.UserFaceRepo;
import com.guxian.facecheck.service.CheckFaceExistService;
import com.guxian.facecheck.service.OSSForFaceService;
import com.guxian.facecheck.service.OssService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.Instant;
import java.util.Date;

@Service
public class AliFaceOssService implements OSSForFaceService {

    private final OssService ossService;

    @Value("${oss.face-filename-prefix}")
    private String faceFilenamePrefix = "FACE_";

    @Value("${oss.face-filename-suffix}")
    private String faceFilenameSuffix = ".png";

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
    public String uploadFace(File file) {

        return uploadFace(file, CurrentUserSession.getUserSession().getUserId());
    }

    @Override
    public String uploadFace(File file, Long userId) {
        //检测面部是否存在
        if (!checkFaceExistService.hasFace(file)) {
            throw new ServiceException(BizCodeEnum.NO_FACE_WAS_DETECTED);
        }

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
        return faceFilenamePrefix + userId + faceFilenameSuffix;
    }
}
