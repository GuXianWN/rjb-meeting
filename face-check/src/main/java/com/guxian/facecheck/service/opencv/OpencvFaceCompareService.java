package com.guxian.facecheck.service.opencv;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.guxian.common.exception.BizCodeEnum;
import com.guxian.common.exception.ServiceException;
import com.guxian.common.utils.SomeUtils;
import com.guxian.facecheck.service.FaceCompareService;
import lombok.extern.log4j.Log4j2;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URL;
import java.util.List;

/**
 * 比对2张图的人脸相似度 （越接近1越相似）
 */
@Log4j2
@Service
public class OpencvFaceCompareService implements FaceCompareService {
    private final CascadeClassifier faceDetector;

    public OpencvFaceCompareService(CascadeClassifier cascadeClassifier) {
        this.faceDetector = cascadeClassifier;
    }

    @Override
    public double checkFaceSimilarRate(File faceFileA, File faceFileB) {
        Mat mat_1 = conv_Mat(faceFileA.getAbsolutePath());
        Mat mat_2 = conv_Mat(faceFileB.getAbsolutePath());
        Mat hist_1 = new Mat();
        Mat hist_2 = new Mat();

        //颜色范围
        MatOfFloat ranges = new MatOfFloat(0f, 256f);
        //直方图大小， 越大匹配越精确 (越慢)
        MatOfInt histSize = new MatOfInt(100000);

        Imgproc.calcHist(List.of(mat_1), new MatOfInt(0), new Mat(), hist_1, histSize, ranges);
        Imgproc.calcHist(List.of(mat_2), new MatOfInt(0), new Mat(), hist_2, histSize, ranges);

        // CORREL 相关系数
        double res = Imgproc.compareHist(hist_1, hist_2, Imgproc.CV_COMP_CORREL);
        log.info("当前比较的结果系数为： {}", res);
        return res;
    }

    /**
     * 灰度化人脸
     *
     * @param img
     * @return
     */
    public Mat conv_Mat(String img) {
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
            log.info("conv_Mat未识别出该图像中的人脸，img={}", img);
            throw new ServiceException(BizCodeEnum.NO_FACE_WAS_DETECTED);
        }
        return face;
    }
}
