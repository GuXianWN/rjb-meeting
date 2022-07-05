package com.guxian.facecheck.service;

import com.guxian.facecheck.config.AliServiceBuilder;
import com.guxian.facecheck.config.OssProperties;
import com.guxian.facecheck.repo.UserFaceRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AliOssServiceTest {

    @MockBean
    private UserFaceRepo mockUserFaceRepo;


    @Autowired
    private OssProperties ossProperties;


    @BeforeEach
    void setUp() {

    }




    @Test
    void testUpload() {
//        AliServiceBuilder aliServiceBuilder = new AliServiceBuilder(ossProperties, oss);
//        AliOssService aliOssUploadServiceUnderTest=new AliOssService(mockUserFaceRepo, aliServiceBuilder.aliServiceBean());

        // Setup

        final File file = new File("D:/game/galgame/新建文件夹/归路/www/img/pictures/1night.png");

        // Run the test

//        final String result = aliOssUploadServiceUnderTest.uploadFace(file, 2L);

        // Verify the results
//        System.out.println(result);
    }
}
