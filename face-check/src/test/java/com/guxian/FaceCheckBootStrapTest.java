package com.guxian;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;

class FaceCheckBootStrapTest {

    public native void a();


    @BeforeEach
    void setUp() {
        URL url = ClassLoader.getSystemResource("lib/opencv/opencv_java455.dll");
        System.load(url.getPath());

    }

    @Test
    void opencvTest() {
        Mat image = Imgcodecs.imread("D:/File/picture/(95).png", 1);// 读取图片，第二个参数是图片读取方式0：灰度 1,彩色

        HighGui.imshow("图片", image);// 显示读取的图片

        // 转换为灰度
        Mat image2 = new Mat();
        Imgproc.cvtColor(image, image2, Imgproc.COLOR_BGR2GRAY);// 将读取的图片转为灰色

        HighGui.imshow("灰度", image2);// 展示灰色的图片

        HighGui.waitKey();// 阻塞进程

        HighGui.destroyAllWindows(); // 销毁窗口
    }

    //        Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2GRAY);// 将读取的图片转为灰色
//        Imgproc.equalizeHist(image, image);// 对图片进行直方图均衡化
//        Imgproc.resize(image, image, new org.opencv.core.Size(image.cols() / 2, image.rows() / 2));// 将图片缩放到1/2大小
//        Imgproc.GaussianBlur(image, image, new org.opencv.core.Size(5, 5), 0);// 对图片进行高斯模糊
//        Imgproc.Canny(image, image, 10, 100);// 对图片进行边缘检测
    @Test
    void findFaceTest() {
        String resourcePath = System.getProperty("user.dir") + "/src/main/resources";
        Mat image = Imgcodecs.imread(resourcePath + "/photos/877.png", 1);// 读取图片，第二个参数是图片读取方式0：灰度 1,彩色
        // 对图片检测
        MatOfRect faceDetections = new MatOfRect();
        //获取当前项目路径
        CascadeClassifier faceDetector = new CascadeClassifier(resourcePath + "/haarcascade_frontalface_alt.xml");
        faceDetector.detectMultiScale(image, faceDetections);
        // 对检测到的人脸进行矩形框
        for (Rect rect : faceDetections.toArray()) {
            Imgproc.rectangle(image, new org.opencv.core.Point(rect.x, rect.y), new org.opencv.core.Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
        }

//
//        Imgproc.HoughCircles(image, image, Imgproc.HOUGH_GRADIENT, 1, image.rows() / 8, 100, 20, 1, 100);// 对图片进行霍夫变换检测圆


        // 展示图片
        HighGui.imshow("face", image);
        HighGui.waitKey();
        HighGui.destroyAllWindows();

    }
}
