package com.guxian.facecheck.controller;

import com.guxian.common.entity.ResponseData;
import com.guxian.facecheck.service.FaceCheckService;
import lombok.Setter;
import org.bouncycastle.cert.ocsp.RespData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/face-check")
@Setter(onMethod_ = @Autowired)
public class FaceCheckController {
    private FaceCheckService faceCheckService;

    @PostMapping("/")
    public ResponseData check(String url) {
        var check=faceCheckService.checkFace(url);
        if(check){
            return ResponseData.success("识别程度高");
        }else {
            return ResponseData.success("识别程度低");
        }
    }


}
