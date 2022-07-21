package com.guxian.facecheck.service.provider;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.DownloadFileRequest;
import com.aliyun.oss.model.GetObjectRequest;
import com.guxian.common.utils.FileCacheUtils;
import com.guxian.facecheck.config.AliServiceObject;
import com.guxian.facecheck.service.OssService;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Order;
import org.springframework.stereotype.Service;
import com.aliyun.oss.model.PutObjectRequest;

import java.io.File;
import java.io.InputStream;
import java.net.URL;


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
        return aliServiceObject.getDownloadPathPrefix() + filename;
    }

    @Override
    public void deleteObject(String filename) {
        aliServiceObject.getOss().deleteObject(aliServiceObject.getBucketName(), filename);
    }

    @SneakyThrows
    @Override
    public InputStream downloadObject(String filename) {

        var url = new URL(aliServiceObject.getDownloadPathPrefix() + filename);
        return url.openStream();
    }

    @Override
    public Boolean hasObject(String filename) {
        return aliServiceObject.getOss().doesObjectExist(aliServiceObject.getBucketName(), filename);
    }
}
