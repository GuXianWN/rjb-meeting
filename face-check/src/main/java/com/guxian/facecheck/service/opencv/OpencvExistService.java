package com.guxian.facecheck.service.opencv;

import com.guxian.facecheck.service.CheckFaceExistService;
import lombok.extern.log4j.Log4j2;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.objdetect.CascadeClassifier;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URL;

@Service
@Log4j2
public class OpencvExistService implements CheckFaceExistService {
    private static final String RESOURCE_PATH = System.getProperty("user.dir").replace('\\','/') + "/src/main/resources";

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
    // todo 将文件下载到本地!!! file:\C:\Users\32253\Desktop\0 无法解析！
    public boolean hasFace(String url) {
        Mat image = Imgcodecs.imread(url, 1);// 读取图片，第二个参数是图片读取方式0：灰度 1,彩色
        // 对图片检测
        MatOfRect faceDetections = new MatOfRect();
        //获取当前项目路径
        log.info("当前训练集路径: {}  ",RESOURCE_PATH + "/haarcascade_frontalface_alt.xml");
        CascadeClassifier faceDetector = new CascadeClassifier(RESOURCE_PATH + "/haarcascade_frontalface_alt.xml");
        faceDetector.detectMultiScale(image, faceDetections);
        Rect[] rects = faceDetections.toArray();
        return rects.length != 0;
    }
}
