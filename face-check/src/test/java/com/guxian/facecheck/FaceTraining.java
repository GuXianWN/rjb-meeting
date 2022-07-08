package com.guxian.facecheck;

import com.guxian.common.exception.BizCodeEnum;
import com.guxian.common.exception.ServiceException;
import com.guxian.common.utils.SomeUtils;
import com.sun.activation.viewers.ImageViewer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.List;

public class FaceTraining {
    CascadeClassifier cascadeClassifier;

    @BeforeEach
    void setUp() {
        nu.pattern.OpenCV.loadShared();
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        String xml = SomeUtils.getPath() + "static/haarcascade_frontalface_alt.xml";
        File xmlFile = new File(xml);
        this.cascadeClassifier = new CascadeClassifier(xml.toString());

    }
    @Test
    void test() {
        Mat mat1 = convMat("C:\\Users\\32253\\Desktop\\1.png");
        Mat hist1 = new Mat();

        //颜色范围
        MatOfFloat ranges = new MatOfFloat(0f, 256f);
        //直方图大小， 越大匹配越精确 (越慢)
        MatOfInt histSize = new MatOfInt(100000);

        Imgproc.calcHist(List.of(mat1), new MatOfInt(0), new Mat(), hist1, histSize, ranges);
        Imgproc.calcHist(List.of(mat2), new MatOfInt(0), new Mat(), hist2, histSize, ranges);

        // CORREL 相关系数
        double res = Imgproc.compareHist(hist1, hist2, Imgproc.CV_COMP_CORREL);
    }

    private Mat convMat(String s) {
        Mat image0 = Imgcodecs.imread(img);
        Mat image1 = new Mat();
        //Mat image2 = new Mat();
        // 灰度化
        Imgproc.cvtColor(image0, image1, Imgproc.COLOR_BGR2GRAY);
        //直方均匀
        //Imgproc.equalizeHist(image1, image2);

        // 探测人脸
        MatOfRect faceDetections = new MatOfRect();
        CascadeClassifier.detectMultiScale(image1, faceDetections);

        //探测人眼
//        MatOfRect eyeDetections = new MatOfRect();
//        eyeDetector.detectMultiScale(image1, eyeDetections);

        // rect中人脸图片的范围
        Mat face = null;
        for (Rect rect : faceDetections.toArray()) {

            //给图片上画框框 参数1是图片 参数2是矩形 参数3是颜色 参数四是画出来的线条大小
            //Imgproc.rectangle(image0,rect,new Scalar(0,0,255),2);
            //输出图片
            //Imgcodecs.imwrite(img+"_.jpg",image0);

            face = new Mat(image1, rect);
        }
        if (null == face) {
            throw new ServiceException(BizCodeEnum.NO_FACE_WAS_DETECTED);
        }
        return face;
    }
}
