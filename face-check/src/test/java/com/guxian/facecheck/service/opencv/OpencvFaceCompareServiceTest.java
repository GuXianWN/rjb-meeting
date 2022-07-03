package com.guxian.facecheck.service.opencv;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opencv.core.Mat;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class OpencvFaceCompareServiceTest {

    private OpencvFaceCompareService opencvFaceCompareServiceUnderTest;

    @BeforeEach
    void setUp() {
//        opencvFaceCompareServiceUnderTest = new OpencvFaceCompareService();
    }

    @Test
    void testCheckFaceSimilarRate() {
        // Setup
        final File faceFileA = new File("C:/Users/32253/Desktop/1.jpg");
        final File faceFileB = new File("C:/Users/32253/Desktop/2.jpg");

        // Run the test
        final double result = opencvFaceCompareServiceUnderTest.checkFaceSimilarRate(faceFileA, faceFileB);

        // Verify the results
        System.out.println(result);

    }

    @Test
    void testConv_Mat() {
        // Setup
        // Run the test
        final Mat result = opencvFaceCompareServiceUnderTest.convMat("img");

        // Verify the results
    }
}
