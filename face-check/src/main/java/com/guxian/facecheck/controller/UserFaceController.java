package com.guxian.facecheck.controller;

import com.guxian.common.entity.PageData;
import com.guxian.common.entity.ResponseData;
import com.guxian.facecheck.entity.UserFace;
import com.guxian.facecheck.repo.UserFaceRepo;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NegativeOrZero;
import javax.validation.constraints.PositiveOrZero;
import java.util.Optional;

@Log4j2
@RequestMapping("/face")
@RestController
public class UserFaceController {
    private final UserFaceRepo faceRepo;

    public UserFaceController(UserFaceRepo faceRepo) {
        this.faceRepo = faceRepo;
    }

    @GetMapping("/list/{page}/{size}")
    ResponseData getFaces(@PathVariable(name = "page") Integer page, @PathVariable(name = "size") Integer size) {
        page = (page <= 0 ? 0 : page - 1);
        var userFace = faceRepo.findAll(PageRequest.of(page, size));
        var pageData = new PageData(Long.valueOf(page), Long.valueOf(size), Long.valueOf(userFace.getTotalPages()), userFace.getContent());
        log.info("pageData :{}", pageData);
        return ResponseData.success().data(pageData);
    }

    @GetMapping("/{uid}")
    public ResponseData getFaces(@PathVariable Long uid) {
        UserFace userFace = faceRepo.findByUserId(uid).orElse(new UserFace().setUserId(uid));
        return ResponseData.success().data(userFace);
    }
}
