package com.guxian.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guxian.auth.entity.Count;

public interface CountService extends IService<Count> {
    Object countTime();
}
