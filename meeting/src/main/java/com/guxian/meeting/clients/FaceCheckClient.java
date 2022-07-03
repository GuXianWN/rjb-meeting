package com.guxian.meeting.clients;

import com.guxian.common.entity.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@FeignClient(value = "face-check")
public interface FaceCheckClient {
    @PostMapping("/face/compare")
    public ResponseData compareFace(@RequestParam(name = "file") MultipartFile file);
}
