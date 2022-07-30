package com.guxian.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guxian.auth.entity.FaceCount;
import com.guxian.auth.entity.vo.CountVo;
import com.guxian.auth.entity.vo.FaceCountVo;
import com.guxian.auth.mapper.FaceCountMapper;
import com.guxian.auth.service.FaceCountService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class FaceCountServiceImpl extends ServiceImpl<FaceCountMapper, FaceCount> implements FaceCountService {
    @Override
    public FaceCountVo countTime() {
        LocalDateTime now = LocalDateTime.now();
        List<CountVo> list = new ArrayList<>();
        for (int i = 1; i < 24; i++) {
            LocalDateTime r = now.minusHours(i);
            LocalDateTime l = now.minusHours(i-1);
            Long count = baseMapper.selectCount(new LambdaQueryWrapper<FaceCount>()
                    .le(FaceCount::getTime, l)
                    .ge(FaceCount::getTime,r));
            list.add(new CountVo(l,r,count));
        }
        FaceCountVo vo = new FaceCountVo()
                .setList(list);
        vo.setSuccess(baseMapper.selectCount(new LambdaQueryWrapper<FaceCount>()
                .ge(FaceCount::getTime, now.minusHours(24))
                .le(FaceCount::getTime,now)
                .ge(FaceCount::getRale,0.7)));
        vo.setError(baseMapper.selectCount(new LambdaQueryWrapper<FaceCount>()
                .ge(FaceCount::getTime, now.minusHours(24))
                .le(FaceCount::getTime,now)
                .le(FaceCount::getRale,0.7)));
        return vo;
    }
}
