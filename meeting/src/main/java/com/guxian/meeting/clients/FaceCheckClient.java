package com.guxian.meeting.clients;

import com.guxian.common.entity.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
/**
 * 当你看到他的时候，他在
 * {@link
 *  com.guxian.common.openfegin.facecheck.FaceCheckController
 * }
 */
//@FeignClient(value = "face")
//public interface FaceCheckClient {
//    @PostMapping("/face/compare")
//    ResponseData compareFace(@RequestPart(name = "file") MultipartFile file);
//}
