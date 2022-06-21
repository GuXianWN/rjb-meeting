package com.guxian.facecheck.service;

import com.guxian.common.exception.BizCodeEnum;
import com.guxian.common.exception.ServiceException;
import com.guxian.common.utils.CurrentUserSession;
import com.guxian.facecheck.repo.UserFaceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectRequest;

import java.io.File;

@Service
public class AliOssUploadService implements UploadService {

    private final UserFaceRepo userFace;
    @Value("${file.upload.path}")
    private String uploadPath;

    public AliOssUploadService(@Autowired UserFaceRepo userFace) {
        this.userFace = userFace;
    }

    public boolean checkFace(String url) {
        var userId = CurrentUserSession.getUserSession().getUserId();
        var userFaceUrl = userFace.findByUserId(userId).orElseThrow(() ->
                new ServiceException(BizCodeEnum.USER_FACE_NOT_EXIST)).getFaceUrl();

        return userFaceUrl != null;
    }


    public boolean upload(String url) {
        return true;
    }

    @Override
    public String upload(File file) {

        String endpoint = "https://oss-cn-beijing.aliyuncs.com";

        String accessKeyId = "LTAI5tR75dDocmNCnoASQNCk";
        String accessKeySecret = "1laLGmM2O8QWKl930b0kbvqcVIgGhU";

        String objectName = "ruanjianbei_2022/test01";

        String bucketName = "cocos-kepu";


        // 填写本地文件的完整路径，例如D:\\localpath\\examplefile.txt。
        // 如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件。
        String filePath = "C:/Users/32253/Desktop/This is test  for OSS.txt";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, new File(filePath));

            // 上传文件。
            ossClient.putObject(putObjectRequest);
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }

        }
        return "";
    }
}
