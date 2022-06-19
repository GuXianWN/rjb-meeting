package com.guxian.facecheck;

import nu.pattern.OpenCV;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.net.URL;
import java.util.ArrayList;

import static java.lang.StrictMath.log10;
import static org.opencv.core.CvType.CV_32F;

class FaceCheckBootStrapTest {


    static String resourcePath = System.getProperty("user.dir") + "/src/main/resources";


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


    Mat getFace(String url) {
        Mat image = Imgcodecs.imread(url, 1);// 读取图片，第二个参数是图片读取方式0：灰度 1,彩色
        // 对图片检测
        MatOfRect faceDetections = new MatOfRect();
        //获取当前项目路径
        CascadeClassifier faceDetector = new CascadeClassifier(resourcePath + "/haarcascade_frontalface_alt.xml");
        faceDetector.detectMultiScale(image, faceDetections);
        Rect[] rects = faceDetections.toArray();
        return image.submat(rects[0]);
    }


    @Test
    void findFaceTest() {

        var image1=getFace(resourcePath + "/photos/01.png");
        var image2=getFace(resourcePath + "/photos/02.png");
        System.out.println(getPSNR(image1, image2));
    }

    double getPSNR(Mat I1, Mat I2) {
        Mat s1 = new Mat();

        //手动改了尺寸，不然absdiff会报错
        I1 = I1.submat(20, 350, 20, 350);
        I2 = I2.submat(20, 350, 20, 350);

        printImage(I1);
        printImage(I2);

        Core.absdiff(I1, I2, s1);       // |I1 - I2|
        s1.convertTo(s1, CvType.CV_32F);  // cannot make a square on 8 bits
        s1 = s1.mul(s1);           // |I1 - I2|^2

        // sum elements per channel
        Scalar s = Core.sumElems(s1);

        double sse = s.val[0] + s.val[1] + s.val[2]; // sum channels

        if (sse <= 1e-10) // for small values return zero
            return 0;
        else {
            double mse = sse / (double) (I1.channels() * I1.total());
            double psnr = 10.0 * log10((255 * 255) / mse);
            return psnr;
        }
    }

    public void printImage(Mat mat) {
        // 展示图片
        HighGui.imshow("face", mat);
        HighGui.waitKey();
        HighGui.destroyAllWindows();
    }
}


//        // 对检测到的人脸进行矩形框
//        for (Rect rect : faceDetections.toArray()) {
//            Imgproc.rectangle(image, new org.opencv.core.Point(rect.x, rect.y), new org.opencv.core.Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
//            // 截取人脸
//            Mat face = image.submat(rect);
//            Imgcodecs.imwrite(resourcePath + "/photos/face.png", face);
//            faces.add(face);
//        }