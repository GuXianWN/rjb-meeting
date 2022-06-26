package com.guxian.facecheck.service;

import com.aliyun.oss.OSSClientBuilder;
import com.guxian.facecheck.config.AliServiceBuilder;
import com.guxian.facecheck.config.OssProperties;
import com.guxian.facecheck.entity.UserFace;
import com.guxian.facecheck.repo.UserFaceRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AliOssUploadServiceTest {

    @MockBean
    private UserFaceRepo mockUserFaceRepo;


    @Autowired
    private OssProperties ossProperties;


    @BeforeEach
    void setUp() {

    }

    @Test
    void testCheckFace() {
        // Setup
        // Configure UserFaceRepo.findByUserId(...).
        final Optional<UserFace> userFace = Optional.of(
                new UserFace(0, 0L, "faceUrl", new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime()));
        when(mockUserFaceRepo.findByUserId(0L)).thenReturn(userFace);

        // Run the test
//        final boolean result = aliOssUploadServiceUnderTest.checkFace("url");

        // Verify the results
//        assertThat(result).isFalse();
    }

    @Test
    void testCheckFace_UserFaceRepoReturnsAbsent() {
        // Setup
        when(mockUserFaceRepo.findByUserId(0L)).thenReturn(Optional.empty());

        // Run the test
//        assertThatThrownBy(() -> aliOssUploadServiceUnderTest.checkFace("url")).isInstanceOf(ServiceException.class);
    }


    @Test
    void testUpload() {
        AliServiceBuilder aliServiceBuilder = new AliServiceBuilder(ossProperties);
        AliOssUploadService aliOssUploadServiceUnderTest=new AliOssUploadService(mockUserFaceRepo, aliServiceBuilder.aliServiceBean());

        // Setup

        final File file = new File("D:/game/galgame/新建文件夹/归路/www/img/pictures/1night.png");

        // Run the test

        final String result = aliOssUploadServiceUnderTest.uploadFace(file, 2L);

        // Verify the results
        System.out.println(result);
    }
}