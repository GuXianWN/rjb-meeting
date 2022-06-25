package com.guxian.facecheck.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.Callback;
import com.aliyun.oss.model.GetObjectRequest;
import com.guxian.common.exception.BizCodeEnum;
import com.guxian.common.exception.ServiceException;
import com.guxian.common.utils.CurrentUserSession;
import com.guxian.facecheck.repo.UserFaceRepo;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;

import java.io.File;


@Service
@Log4j2
@Data
public class AliOssUploadService implements UploadService {

    private final UserFaceRepo userFaceRepo;

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

    @Value("${oss.download-prefix}")
    private String downloadPictureUrl;


    private OSS ossClient;


    private static final String FACE_PIC_PREFIX = "FACE_";
    private static final String FACE_PIC_SUFFIX = ".png";


    public AliOssUploadService(UserFaceRepo userFaceRepo) {
        this.userFaceRepo = userFaceRepo;
        if(downloadPictureUrl==null) throw new ServiceException(BizCodeEnum.OSS_INIT_EXCEPTION);
        this.ossClient = new OSSClientBuilder().build(this.endpoint, this.accessKeyId, this.accessKeySecret);
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
        var objectName = objectNamePrefix + filename;
        PutObjectRequest putObjectRequest = new PutObjectRequest(this.bucketName, objectName, file);

        ossClient.putObject(putObjectRequest);
        ossClient.shutdown();

        return downloadPictureUrl + filename;
    }

    public void deleteObject(String filename) {
        ossClient.deleteObject(bucketName, filename);
    }

    public void downloadObject(String filename) {
        ossClient.getObject(new GetObjectRequest(bucketName, filename), new File(uploadPath + filename));
    }
}
