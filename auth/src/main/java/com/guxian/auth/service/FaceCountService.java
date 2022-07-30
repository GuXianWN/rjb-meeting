package com.guxian.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guxian.auth.entity.FaceCount;
import com.guxian.auth.entity.vo.FaceCountVo;

public interface FaceCountService extends IService<FaceCount> {
    FaceCountVo countTime();
}
