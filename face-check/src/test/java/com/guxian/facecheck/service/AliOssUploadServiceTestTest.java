package com.guxian.facecheck.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AliOssUploadServiceTestTest {

    private AliOssUploadServiceTest aliOssUploadServiceTestUnderTest;

    @BeforeEach
    void setUp() {
        aliOssUploadServiceTestUnderTest = new AliOssUploadServiceTest();
    }

    @Test
    void testSetUp() {
        // Setup
        // Run the test
        aliOssUploadServiceTestUnderTest.setUp();

        // Verify the results
    }

    @Test
    void testTestCheckFace() {
        // Setup
        // Run the test
        aliOssUploadServiceTestUnderTest.testCheckFace();

        // Verify the results
    }

    @Test
    void testTestCheckFace_UserFaceRepoReturnsAbsent() {
        // Setup
        // Run the test
        aliOssUploadServiceTestUnderTest.testCheckFace_UserFaceRepoReturnsAbsent();

        // Verify the results
    }

    @Test
    void testTestUpload() {
        // Setup
        // Run the test
        aliOssUploadServiceTestUnderTest.testUpload();

        // Verify the results
    }
}
