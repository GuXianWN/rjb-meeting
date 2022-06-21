package com.guxian.facecheck.service;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.PutObjectRequest;
import com.guxian.common.exception.ServiceException;
import com.guxian.facecheck.entity.UserFace;
import com.guxian.facecheck.repo.UserFaceRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AliOssUploadServiceTest {

    private AliOssUploadService aliOssUploadServiceUnderTest;

    @BeforeEach
    void setUp(){
        this.aliOssUploadServiceUnderTest = spy(mock(AliOssUploadService.class));
    }


    @Test
    void testUpload() {
        String upload = aliOssUploadServiceUnderTest.upload(new File(""));
    }

    @Test
    void  download() {

        String endpoint = "https://oss-cn-beijing.aliyuncs.com";

        String accessKeyId = "LTAI5tR75dDocmNCnoASQNCk";
        String accessKeySecret = "1laLGmM2O8QWKl930b0kbvqcVIgGhU";

        String objectName = "ruanjianbei_2022/test01";

        String bucketName = "cocos-kepu";
        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        // 填写Bucket名称，例如examplebucket。
        // 填写不包含Bucket名称在内的Object完整路径，例如testfolder/exampleobject.txt。
        String pathName = "D:/1.txt";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            // 下载Object到本地文件，并保存到指定的本地路径中。如果指定的本地文件存在会覆盖，不存在则新建。
            // 如果未指定本地路径，则下载后的文件默认保存到示例程序所属项目对应本地路径中。
            ossClient.getObject(new GetObjectRequest(bucketName, objectName), new File(pathName));
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
    }
}
