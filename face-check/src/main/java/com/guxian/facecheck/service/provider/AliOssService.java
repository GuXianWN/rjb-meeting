package com.guxian.facecheck.service.provider;

import com.aliyun.oss.model.GetObjectRequest;
import com.guxian.common.utils.CurrentUserSession;
import com.guxian.facecheck.config.AliServiceObject;
import com.guxian.facecheck.repo.UserFaceRepo;
import com.guxian.facecheck.service.OSSForFaceService;
import com.guxian.facecheck.service.OssService;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Order;
import org.springframework.stereotype.Service;
import com.aliyun.oss.model.PutObjectRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;


@Service
@Log4j2
@Data
@Order(1)
public class AliOssService implements OssService {

    private final AliServiceObject aliServiceObject;


    public AliOssService(AliServiceObject aliServiceObject) {
        this.aliServiceObject = aliServiceObject;
    }


    @SneakyThrows
    @Override
    public String uploadObject(InputStream inputStream, String filename) {
        var objectName = aliServiceObject.getObjectNamePrefix() + filename;
        PutObjectRequest putObjectRequest = new PutObjectRequest(aliServiceObject.getBucketName(), objectName, inputStream);
        aliServiceObject.getOss().putObject(putObjectRequest);
        aliServiceObject.getOss().shutdown();
        return aliServiceObject.getDownloadPathPrefix() + filename;
    }

    @Override
    public void deleteObject(String filename) {
        aliServiceObject.getOss().deleteObject(aliServiceObject.getBucketName(), filename);
    }

    @Override
    public File downloadObject(String filename) {
        var file = new File(aliServiceObject.getDownloadPathPrefix() + filename);
        aliServiceObject.getOss().getObject(new GetObjectRequest(aliServiceObject.getBucketName(), filename), file);
        return file;
    }
}
