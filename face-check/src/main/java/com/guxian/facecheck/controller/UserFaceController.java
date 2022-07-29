package com.guxian.facecheck.controller;

import com.guxian.common.entity.PageData;
import com.guxian.common.entity.ResponseData;
import com.guxian.common.exception.BizCodeEnum;
import com.guxian.common.exception.ServiceException;
import com.guxian.facecheck.entity.UserFace;
import com.guxian.facecheck.service.OSSForFaceService;
import com.guxian.facecheck.service.UserFaceService;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Log4j2
@RequestMapping("/face")
@RestController
public class UserFaceController {
    private final UserFaceService userFaceService;

    @Value("${oss.face-filename-suffix}")
    private String faceFilenameSuffix = ".jpg";

    private final OSSForFaceService faceOss;

    public UserFaceController(UserFaceService userFaceService, OSSForFaceService faceOss) {
        this.userFaceService = userFaceService;
        this.faceOss = faceOss;
    }

    @GetMapping("/list/{page}/{size}")
    ResponseData getFaces(@PathVariable(name = "page") Integer page, @PathVariable(name = "size") Integer size) {
        page = (page <= 0 ? 0 : page - 1);
        var userFace = userFaceService.findAll(PageRequest.of(page, size));
        var pageData = new PageData(Long.valueOf(page), Long.valueOf(size), (long) userFace.getTotalPages(), userFace.getContent());
        log.info("pageData :{}", pageData);
        return ResponseData.success().data(pageData);
    }

    @GetMapping("/{uid}")
    public ResponseData getFaces(@PathVariable Long uid) {
        UserFace userFace = userFaceService.findByUserId(uid).orElse(new UserFace().setUserId(uid));
        return ResponseData.success().data(userFace);
    }

    @PostMapping("/upload")
    @SneakyThrows
    public ResponseData upload(@RequestPart(value = "file") MultipartFile file) {
        if (file.isEmpty()) {
            throw new ServiceException(BizCodeEnum.NUMBER_OF_UPLOADED_FILE_NOT_ONE);
        }
        //校验格式
        if (!StringUtils.endsWithIgnoreCase(file.getOriginalFilename(), faceFilenameSuffix)) {
            return ResponseData.error("文件格式错误");
        }
        //上传
        String url = faceOss.uploadFace(file.getInputStream());
        return ResponseData.success().data("url", url);
    }



    @PostMapping("/upload/{uid}")
    @SneakyThrows
    public ResponseData uploadById(@RequestPart(value = "file") MultipartFile file, @PathVariable Long uid) {
        if (file.isEmpty()) {
            throw new ServiceException(BizCodeEnum.NUMBER_OF_UPLOADED_FILE_NOT_ONE);
        }
        //校验格式
        if (!StringUtils.endsWithIgnoreCase(file.getOriginalFilename(), faceFilenameSuffix)) {
            return ResponseData.error("文件格式错误");
        }
        //上传
        String url = faceOss.uploadFace(file.getInputStream(),uid);
        return ResponseData.success().data("url", url);
    }

    @PostMapping("/lock/{uid}")
    public ResponseData lockUserFace(@PathVariable Long uid) {
        return ResponseData.is(userFaceService.setUserFaceLockStatus(uid, true));
    }

    @PostMapping("/unlock/{uid}")
    public ResponseData unlockUserFace(@PathVariable Long uid) {
        return ResponseData.is(userFaceService.setUserFaceLockStatus(uid, false));
    }
}
