package com.guxian.common.openfegin.facecheck;

import com.guxian.common.entity.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.File;


//@FeignClient(value = "face_check")
public interface FaceCheckController {
//    @PostMapping("/face/compare")
    ResponseData compareFace(File file);
}
