package com.guxian;

import org.junit.jupiter.api.Test;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.net.URL;

class FaceCheckBootStrapTest {

    @Test
    void opencvTest() {
        URL url = ClassLoader.getSystemResource("lib/opencv/opencv_java455.dll");
        System.load(url.getPath());
        Mat image = Imgcodecs.imread("D:/File/picture/(95).png", 1);// 读取图片，第二个参数是图片读取方式0：灰度 1,彩色

        HighGui.imshow("图片", image);// 显示读取的图片

        // 转换为灰度
        Mat image2 = new Mat();
        Imgproc.cvtColor(image, image2, Imgproc.COLOR_BGR2GRAY);// 将读取的图片转为灰色

        HighGui.imshow("灰度", image2);// 展示灰色的图片

        HighGui.waitKey();// 阻塞进程

        HighGui.destroyAllWindows(); // 销毁窗口
    }
}
