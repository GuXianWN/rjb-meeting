package com.guxian.facecheck.service.provider;

import com.aliyun.oss.OSS;
import com.guxian.common.exception.BizCodeEnum;
import com.guxian.common.exception.ServiceException;
import com.guxian.common.utils.CurrentUserSession;
import com.guxian.facecheck.entity.UserFace;
import com.guxian.facecheck.repo.UserFaceRepo;
import com.guxian.facecheck.service.CheckFaceExistService;
import com.guxian.facecheck.service.OSSForFaceService;
import com.guxian.facecheck.service.OssService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
public class AliFaceOssService implements OSSForFaceService {

    private String tmpDir = System.getProperty("user.dir").replace('\\', '/') + "/userFace";
    private final OssService ossService;

    @Value("${oss.face-filename-prefix}")
    private String faceFilenamePrefix = "FACE_";

    @Value("${oss.face-filename-suffix}")
    private String faceFilenameSuffix = ".png";

    private final UserFaceRepo userFaceRepo;
    @Resource
    private AliOssService aliOssService;

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
    public String demo(MultipartFile file) {
        String fileName = UUID.randomUUID().toString() + file.getOriginalFilename();
        String path = tmpDir + "/" + fileName;
        try {
            // 复制文件
            File targetFile = new File(tmpDir, fileName);
            FileUtils.writeByteArrayToFile(targetFile, file.getBytes());
            log.info("{}", path);
        } catch (IOException e) {
            //TODO 加异常类
            log.error("保存文件到服务器（本地）失败", e);
        }
        if (!checkFaceExistService.hasFace(path)) {
            throw new ServiceException(BizCodeEnum.NO_FACE_WAS_DETECTED);
        }

        String url = aliOssService.uploadMultipart(file);
        // 从数据库查询是否有该用户的face 信息 ， 如果有 则返回 对应userId ，否则返回 null 的 id （视为没有对应用户的信息）
        Long userId = CurrentUserSession.getUserSession().getUserId();
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
