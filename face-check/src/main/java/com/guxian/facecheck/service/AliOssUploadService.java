package com.guxian.facecheck.service;

import com.aliyun.oss.model.GetObjectRequest;
import com.guxian.common.exception.BizCodeEnum;
import com.guxian.common.exception.ServiceException;
import com.guxian.common.utils.CurrentUserSession;
import com.guxian.facecheck.config.AliService;
import com.guxian.facecheck.repo.UserFaceRepo;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.aliyun.oss.model.PutObjectRequest;

import java.io.File;


@Service
@Log4j2
@Data
public class AliOssUploadService implements UploadService {

    private final UserFaceRepo userFaceRepo;
    private final AliService aliService;


    private static final String FACE_PIC_PREFIX = "FACE_";
    private static final String FACE_PIC_SUFFIX = ".png";


    public AliOssUploadService(UserFaceRepo userFaceRepo, AliService aliService) {
        this.userFaceRepo = userFaceRepo;
        this.aliService = aliService;
    }



    public boolean checkFace(String url) {
        var userId = CurrentUserSession.getUserSession().getUserId();
        var userFaceUrl = userFaceRepo.findByUserId(userId).orElseThrow(() -> new ServiceException(BizCodeEnum.USER_FACE_NOT_EXIST)).getFaceUrl();
        return false;
    }


    @Override
    public String uploadFace(File file) {
        return uploadFace(file, CurrentUserSession.getUserSession().getUserId());
    }

    @Override
    public String upload(File file, String name) {
        return null;
    }

    @Override
    public String uploadFace(File file, Long userId) {

        var filename = FACE_PIC_PREFIX + userId + FACE_PIC_SUFFIX;
        var objectName = aliService.getObjectNamePrefix() + filename;
        PutObjectRequest putObjectRequest = new PutObjectRequest(aliService.getBucketName(), objectName, file);

        aliService.getOssClient().putObject(putObjectRequest);
        aliService.getOssClient().shutdown();

        return aliService.getDownloadPathPrefix() + filename;
    }

    public void deleteObject(String filename) {
        aliService.getOssClient().deleteObject(aliService.getBucketName(), filename);
    }

    public void downloadObject(String filename) {
        aliService.getOssClient().getObject(new GetObjectRequest(aliService.getBucketName(), filename), new File(aliService.getDownloadPathPrefix()+ filename));
    }
}
