package com.guxian.auth.controller;

import com.guxian.auth.entity.Count;
import com.guxian.auth.service.CountService;
import com.guxian.common.entity.ResponseData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/count")
public class CountController {
    @Resource
    private CountService countService;

    @GetMapping
    public ResponseData count() {
        return ResponseData.success().data(countService.countTime());
    }

    @PostMapping
    public void add() {
        countService.save(new Count().setTime(LocalDateTime.now()));
    }
}
