package com.guxian.meeting.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guxian.common.CheckWay;
import com.guxian.common.entity.RedisPrefix;
import com.guxian.common.exception.BizCodeEnum;
import com.guxian.common.exception.ServiceException;
import com.guxian.common.redis.RedisUtils;
import com.guxian.meeting.entity.CheckIn;
import com.guxian.meeting.entity.MeetingCheck;
import com.guxian.meeting.entity.vo.CheckDataVo;
import com.guxian.meeting.service.CheckInService;
import com.guxian.meeting.mapper.CheckInMapper;
import com.guxian.meeting.service.UserMeetingService;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

/**
 * @author GuXian
 * @description 针对表【check_in】的数据库操作Service实现
 * @createDate 2022-05-31 21:08:48
 */

@Service
@Log4j2
@Setter(onMethod_ = @Autowired)
public class DefaultCheckInService extends ServiceImpl<CheckInMapper, CheckIn>
        implements CheckInService {

    private UserMeetingService userMeetingService;

    /**
     * @param checkDataVo
     * @return boolean 是否签到成功
     * @author GuXian
     */


    @Override
    public boolean checkIn(CheckDataVo checkDataVo) {

        if (checkDataVo.getCheckWay() == CheckWay.FACE) {

        }

        if (checkDataVo.getCheckWay() == CheckWay.CODE) {
            return checkInUseCode(checkDataVo);
        }

        return false;
    }


    public boolean checkInUseCode(CheckDataVo checkDataVo) {
        Object getCodeByRedis = RedisUtils.ops.get(MeetingCheck.buildKey(checkDataVo.getMeetingId()));
        if (getCodeByRedis == null) {
            throw new ServiceException(BizCodeEnum.CHECK_IN_CODE_NOT_EXIST);
        }

        var code = getCodeByRedis.toString();

        if (!code.equals(checkDataVo.getCode())) {
            throw new ServiceException(BizCodeEnum.CHECK_IN_CODE_ERROR);
        }

        userMeetingService.checkIn(checkDataVo.getMeetingId(), CheckWay.CODE);
        return true;
    }
}




