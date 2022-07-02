package com.guxian.facecheck.service.provider;

import com.aliyun.credentials.Client;
import com.aliyun.teaopenapi.models.Config;
import com.guxian.common.utils.CurrentUserSession;
import com.guxian.facecheck.config.AliServiceObject;
import com.guxian.facecheck.entity.UserFace;
import com.guxian.facecheck.repo.UserFaceRepo;
import org.apache.commons.io.input.BrokenInputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class AliFaceCompareServiceTest {

    @Autowired
    private AliServiceObject aliServiceObject;
    @Mock
    private UserFaceRepo mockUserFaceRepo;

    @Mock
    CurrentUserSession currentUserSession;

    private AliFaceCompareService aliFaceCompareServiceUnderTest;

    @BeforeEach
    void setUp() {
        aliFaceCompareServiceUnderTest = new AliFaceCompareService(aliServiceObject, mockUserFaceRepo);
    }

    @Test
    void testCheckFaceSimilarRate() throws Exception {
        // Setup
        final InputStream faceFileA = new FileInputStream(new File("C:/Users/32253/Desktop/02.png"));

        // Configure UserFaceRepo.findByUserId(...).
        final Optional<UserFace> userFace = Optional.of(
                new UserFace(0, 2L, "https://cocos-kepu.oss-cn-beijing.aliyuncs.com/ruanjianbei_2022/FACE_2.png", new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime()));
        when(mockUserFaceRepo.findByUserId(2L)).thenReturn(userFace);
        // Run the test
        final double result = aliFaceCompareServiceUnderTest.checkFaceSimilarRate(faceFileA);
        assertThat(result).isNotNaN();
        System.out.println(result);
        // Verify the results
    }

    @Test
    void testCheckFaceSimilarRate_EmptyFaceFileA() throws Exception {
        // Setup
        final InputStream faceFileA = InputStream.nullInputStream();

        // Configure AliServiceObject.getClient(...).
        final Config config = new Config();
        config.setAccessKeyId("accessKeyId");
        config.setAccessKeySecret("accessKeySecret");
        config.setSecurityToken("securityToken");
        config.setProtocol("protocol");
        config.setMethod("method");
        config.setRegionId("regionId");
        config.setReadTimeout(0);
        config.setConnectTimeout(0);
        config.setHttpProxy("httpProxy");
        config.setHttpsProxy("httpsProxy");
        config.setCredential(new Client(new com.aliyun.credentials.models.Config()));
        config.setEndpoint("endpoint");
        config.setNoProxy("noProxy");
        config.setMaxIdleConns(0);
        config.setNetwork("network");
        final com.aliyun.facebody20191230.Client client = new com.aliyun.facebody20191230.Client(config);
        when(aliServiceObject.getClient()).thenReturn(client);

        // Configure UserFaceRepo.findByUserId(...).
        final Optional<UserFace> userFace = Optional.of(
                new UserFace(0, 0L, "faceUrl", new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime()));
        when(mockUserFaceRepo.findByUserId(0L)).thenReturn(userFace);

        // Run the test
        final double result = aliFaceCompareServiceUnderTest.checkFaceSimilarRate(faceFileA);

        // Verify the results
        assertThat(result).isEqualTo(0.0, within(0.0001));
    }

    @Test
    void testCheckFaceSimilarRate_BrokenFaceFileA() throws Exception {
        // Setup
        final InputStream faceFileA = new BrokenInputStream();

        // Configure AliServiceObject.getClient(...).
        final Config config = new Config();
        config.setAccessKeyId("accessKeyId");
        config.setAccessKeySecret("accessKeySecret");
        config.setSecurityToken("securityToken");
        config.setProtocol("protocol");
        config.setMethod("method");
        config.setRegionId("regionId");
        config.setReadTimeout(0);
        config.setConnectTimeout(0);
        config.setHttpProxy("httpProxy");
        config.setHttpsProxy("httpsProxy");
        config.setCredential(new Client(new com.aliyun.credentials.models.Config()));
        config.setEndpoint("endpoint");
        config.setNoProxy("noProxy");
        config.setMaxIdleConns(0);
        config.setNetwork("network");
        final com.aliyun.facebody20191230.Client client = new com.aliyun.facebody20191230.Client(config);
        when(aliServiceObject.getClient()).thenReturn(client);

        // Configure UserFaceRepo.findByUserId(...).
        final Optional<UserFace> userFace = Optional.of(
                new UserFace(0, 0L, "faceUrl", new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime()));
        when(mockUserFaceRepo.findByUserId(0L)).thenReturn(userFace);

        // Run the test
        final double result = aliFaceCompareServiceUnderTest.checkFaceSimilarRate(faceFileA);

        // Verify the results
        assertThat(result).isEqualTo(0.0, within(0.0001));
    }

    @Test
    void testCheckFaceSimilarRate_UserFaceRepoReturnsAbsent() throws Exception {
        // Setup
        final InputStream faceFileA = new ByteArrayInputStream("content".getBytes());

        // Configure AliServiceObject.getClient(...).
        final Config config = new Config();
        config.setAccessKeyId("accessKeyId");
        config.setAccessKeySecret("accessKeySecret");
        config.setSecurityToken("securityToken");
        config.setProtocol("protocol");
        config.setMethod("method");
        config.setRegionId("regionId");
        config.setReadTimeout(0);
        config.setConnectTimeout(0);
        config.setHttpProxy("httpProxy");
        config.setHttpsProxy("httpsProxy");
        config.setCredential(new Client(new com.aliyun.credentials.models.Config()));
        config.setEndpoint("endpoint");
        config.setNoProxy("noProxy");
        config.setMaxIdleConns(0);
        config.setNetwork("network");
        final com.aliyun.facebody20191230.Client client = new com.aliyun.facebody20191230.Client(config);
        when(aliServiceObject.getClient()).thenReturn(client);

        when(mockUserFaceRepo.findByUserId(0L)).thenReturn(Optional.empty());

        // Run the test
        final double result = aliFaceCompareServiceUnderTest.checkFaceSimilarRate(faceFileA);

        // Verify the results
        assertThat(result).isEqualTo(0.0, within(0.0001));
    }

}
