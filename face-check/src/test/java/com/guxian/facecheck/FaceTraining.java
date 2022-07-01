package com.guxian.facecheck;

import com.sun.activation.viewers.ImageViewer;
import org.junit.jupiter.api.Test;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FaceTraining {
    @Test
    void test() {
        Mat image = Imgcodecs.imread("userFace\\3dd5ecfe-066b-4dec-b172-da055bad4966file.jpg");
    }
}
