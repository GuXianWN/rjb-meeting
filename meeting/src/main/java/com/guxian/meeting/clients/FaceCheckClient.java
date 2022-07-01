package com.guxian.meeting.clients;

import com.guxian.common.entity.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.File;

@FeignClient(value = "face-check")
public interface FaceCheckClient {
    @PostMapping("/face/compare")
    ResponseData compareFace(File file);
}
