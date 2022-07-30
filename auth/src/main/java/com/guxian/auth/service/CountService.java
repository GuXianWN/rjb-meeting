package com.guxian.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guxian.auth.entity.Count;
import com.guxian.auth.entity.vo.CountVo;

import java.util.List;

public interface CountService extends IService<Count> {
    List<CountVo> countTime();
}
