package com.guxian.common.openfegin.facecheck;

import com.guxian.common.entity.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;


@FeignClient(value = "face")
public interface FaceCheckController {
    @PostMapping("/face/compare")
    ResponseData compareFace(File file);

    @PostMapping("/face/upload")
    ResponseData uploadFace(@RequestParam("file") File file);

    @PostMapping(value = "/face/file/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String uploadFile(@RequestPart(value = "file") MultipartFile file,
                            @RequestParam("filename") String filename);
}
