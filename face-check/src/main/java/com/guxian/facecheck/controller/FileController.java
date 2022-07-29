package com.guxian.facecheck.controller;

import com.guxian.common.entity.ResponseData;
import com.guxian.common.utils.CurrentUserSession;
import com.guxian.common.utils.SomeUtils;
import com.guxian.facecheck.service.OssService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RequestMapping("/face")
@RestController
public class FileController {
    private final OssService oss;

    public FileController(OssService oss) {
        this.oss = oss;
    }

    @SneakyThrows
    @PostMapping("/file/upload")
    public String uploadFile(@RequestPart(value = "file") MultipartFile file
            , @RequestParam("filename") String filename) {
        return oss.uploadObject(file.getInputStream(),
                filename);
    }

}
