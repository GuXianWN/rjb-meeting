package com.guxian.facecheck.service.opencv;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.guxian.common.exception.BizCodeEnum;
import com.guxian.common.exception.ServiceException;
import com.guxian.facecheck.service.FaceCompareService;
import com.guxian.facecheck.service.opencv.OpencvCacheService.OpencvUtils;
import com.guxian.facecheck.service.opencv.OpencvCacheService.ReturnData;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 比对2张图的人脸相似度 （越接近1越相似）
 */
@Log4j2
@Service
public class OpencvFaceCompareService implements FaceCompareService {

    private final OpencvUtils utils;
    private final CascadeClassifier faceDetector;

    public OpencvFaceCompareService(CascadeClassifier cascadeClassifier) {
        this.utils = new OpencvUtils(cascadeClassifier);
        this.faceDetector = cascadeClassifier;
    }

    @SneakyThrows
    @Override
    public double checkFaceSimilarRate(File faceFileA, File faceFileB) {
        var begin = Instant.now();
        AtomicReference<Mat> faceFileMatA = new AtomicReference<>();
        AtomicReference<Mat> faceFileMatB = new AtomicReference<>();

        faceFileMatA.set(convMat(faceFileA.getAbsolutePath()));

        faceFileMatB.set(convMat(faceFileB.getAbsolutePath()));


        Mat hist1 = new Mat();
        Mat hist2 = new Mat();

        //颜色范围
//        MatOfFloat ranges = utils.getRange();

        //直方图大小， 越大匹配越精确 (越慢)
//        MatOfInt histSize = new MatOfInt(100000);

        AtomicReference<ReturnData> data1 = new AtomicReference<>();
        AtomicReference<ReturnData> data2 = new AtomicReference<>();
        var d = new Thread(() -> {
            data1.set(utils.calcHist(faceFileA.getAbsolutePath(), List.of(faceFileMatA.get()), hist1));
        });
        d.start();
        var d1 = new Thread(() -> {
            data2.set(utils.calcHist(faceFileB.getAbsolutePath(), List.of(faceFileMatB.get()), hist2));
        });
        d1.start();
        d.join();
        d1.join();

//        Imgproc.calcHist(List.of(faceFileMatA), new MatOfInt(0), new Mat(), hist1, histSize, ranges);
//        Imgproc.calcHist(List.of(faceFileMatB), new MatOfInt(0), new Mat(), hist2, histSize, ranges);

        // CORREL 相关系数
        return Imgproc.compareHist(data1.get().getHist(), data2.get().getHist(), Imgproc.CV_COMP_CORREL);
    }

    public Mat convMat(String img) {
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

}
