package com.guxian.facecheck.service.opencv;

import com.guxian.facecheck.service.CheckFaceExistService;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.net.URL;

public class OpencvExistService implements CheckFaceExistService {
    private static final String RESOURCE_PATH = System.getProperty("user.dir") + "/src/main/resources";

    public OpencvExistService() {
        URL url = ClassLoader.getSystemResource("lib/opencv/opencv_java455.dll");
        System.load(url.getPath());
    }

    @Override
    public boolean hasFace(File file) {
        return hasFace(file.getAbsolutePath());
    }
    /**
     * @param url must is <b style="color:red">local</b> file !!!!!
     * @return false or true
     */
    public boolean hasFace(String url) {
        Mat image = Imgcodecs.imread(url, 1);// 读取图片，第二个参数是图片读取方式0：灰度 1,彩色
        // 对图片检测
        MatOfRect faceDetections = new MatOfRect();
        //获取当前项目路径
        CascadeClassifier faceDetector = new CascadeClassifier(RESOURCE_PATH + "/haarcascade_frontalface_alt.xml");
        faceDetector.detectMultiScale(image, faceDetections);
        Rect[] rects = faceDetections.toArray();
        return rects.length != 0;
    }
}
