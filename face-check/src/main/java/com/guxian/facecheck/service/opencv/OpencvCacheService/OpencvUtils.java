package com.guxian.facecheck.service.opencv.OpencvCacheService;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.guxian.common.entity.UserSession;
import com.guxian.common.exception.BizCodeEnum;
import com.guxian.common.exception.ServiceException;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
public class OpencvUtils {

    private final CascadeClassifier faceDetector;

    public OpencvUtils(CascadeClassifier faceDetector) {
        this.faceDetector = faceDetector;
    }


    /**
     * 灰度化人脸
     *
     * @param img
     * @return
     */
    public Mat convMat(String img) {
        log.info("进入convMat 方法");
        if (StringUtils.isBlank(img)) {
            return null;
        }
        Mat image0 = Imgcodecs.imread(img);
        Mat image1 = new Mat();
        //Mat image2 = new Mat();
        // 灰度化
        Imgproc.cvtColor(image0, image1, Imgproc.COLOR_BGR2GRAY);
        //直方均匀
        //Imgproc.equalizeHist(image1, image2);

        // 探测人脸
        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(image1, faceDetections);

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
            log.info("未识别出该图像中的人脸，img={}", img);
            throw new ServiceException(BizCodeEnum.NO_FACE_WAS_DETECTED);
        }
        return face;
    }

    //是不是必须清空缓存
    public ReturnData calcHist(String filePath, List<Mat> mat, Mat hist1) {
        log.info("calcHist.....");
        Imgproc.calcHist(mat, new MatOfInt(0), new Mat(), hist1, getHitSizeMat(), getRange());
        return new ReturnData().setMat(mat.get(0)).setHist(hist1);
    }

    public MatOfFloat getRange() {
        return new MatOfFloat(0f, 256f);
    }

    public MatOfInt getHitSizeMat() {
        return new MatOfInt(100000);
    }
}
