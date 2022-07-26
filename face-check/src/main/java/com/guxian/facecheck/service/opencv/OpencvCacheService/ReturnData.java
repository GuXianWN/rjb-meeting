package com.guxian.facecheck.service.opencv.OpencvCacheService;

import lombok.Data;
import lombok.experimental.Accessors;
import org.opencv.core.Mat;

@Data
@Accessors(chain = true)
public class ReturnData {
        Mat mat;
        Mat hist;
    }
