package com.guxian.facecheck.service;

import com.guxian.common.utils.CurrentUserSession;
import com.guxian.facecheck.repo.UserFaceRepo;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;

import java.io.File;


@Service
public class AliOssUploadService implements UploadService {

    private final UserFaceRepo userFace;
    @Value("${file.upload.path}")
    private String uploadPath;

    @Value("${oss.endpoint}")
    private String endpoint;
    @Value("${oss.access-key-id}")
    private String accessKeyId;
    @Value("${oss.access-key-secret}")
    private String accessKeySecret;
    @Value("${oss.bucket}")
    private String bucketName;
    @Value("${oss.object-name-prefix}")
    private String objectNamePrefix;
    @Autowired
    UserFaceRepo userFaceRepo;
    public AliOssUploadService() {
        this.userFace = null;
    }

    public boolean checkFace(String url) {
        var userId = CurrentUserSession.getUserSession().getUserId();
//        var userFaceUrl = userFace.findByUserId(userId).orElseThrow(() -> new ServiceException(BizCodeEnum.USER_FACE_NOT_EXIST)).getFaceUrl();

//        return userFaceUrl != null;
        return  false;
    }


    public boolean upload(String url) {
        return true;
    }

    @SneakyThrows
    @Override
    public String upload(File file) {
        var objectName = objectNamePrefix + file.getName();
        var ossClient = new OSSClientBuilder().build(this.endpoint, this.accessKeyId, this.accessKeySecret);
        PutObjectRequest putObjectRequest = new PutObjectRequest(this.bucketName, objectName, file);
        ossClient.putObject(putObjectRequest);
        ossClient.shutdown();
        return "";
    }
}
