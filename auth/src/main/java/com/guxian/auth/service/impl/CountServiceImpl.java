package com.guxian.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guxian.auth.entity.Count;
import com.guxian.auth.entity.vo.CountVo;
import com.guxian.auth.mapper.CountMapper;
import com.guxian.auth.service.CountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CountServiceImpl extends ServiceImpl<CountMapper, Count> implements CountService {
    @Override
    public Object countTime() {
        LocalDateTime now = LocalDateTime.now();
        List<CountVo> list = new ArrayList<>();
        for (int i = 1; i < 24; i++) {
            LocalDateTime r = LocalDateTime.now().minusHours(i);
            LocalDateTime l = LocalDateTime.now().minusHours(i-1);
            Long count = baseMapper.selectCount(new LambdaQueryWrapper<Count>()
                    .le(Count::getTime, l)
                    .ge(Count::getTime,r));
            list.add(new CountVo(l,r,count));
        }
        log.info("{}",now);
        return list;
    }
}
