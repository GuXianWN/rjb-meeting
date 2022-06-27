package com.guxian.meeting.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guxian.common.CheckWay;
import com.guxian.common.entity.ResponseData;
import com.guxian.common.exception.BizCodeEnum;
import com.guxian.common.exception.ServiceException;
import com.guxian.common.openfegin.facecheck.FaceCheckController;
import com.guxian.common.redis.RedisUtils;
import com.guxian.meeting.entity.CheckIn;
import com.guxian.meeting.entity.MeetingCheck;
import com.guxian.meeting.entity.vo.CheckDataVo;
import com.guxian.meeting.service.CheckInService;
import com.guxian.meeting.mapper.CheckInMapper;
import com.guxian.meeting.service.UserMeetingService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author GuXian
 * @description 针对表【check_in】的数据库操作Service实现
 * @createDate 2022-05-31 21:08:48
 */

@Service
@Log4j2
public class DefaultCheckInService extends ServiceImpl<CheckInMapper, CheckIn>
        implements CheckInService {

    private final UserMeetingService userMeetingService;

    private final FaceCheckController faceCheckController;

    public DefaultCheckInService(UserMeetingService userMeetingService, FaceCheckController faceCheckController) {
        this.userMeetingService = userMeetingService;
        this.faceCheckController = faceCheckController;
    }


    /**
     * @param checkDataVo
     * @return boolean 是否签到成功
     * @author GuXian
     */


    @Override
    public boolean checkIn(CheckDataVo checkDataVo) {

        if (checkDataVo.getCheckWay() == CheckWay.FACE) {
            return checkInUseFace(checkDataVo);
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

    public boolean checkInUseFace(CheckDataVo checkDataVo) {
        var face = checkDataVo.getFace();
        var responseData = faceCheckController.compareFace(face);
        return true;
    }

    @Override
    public List<CheckIn> getCheckInList(Long checkId) {
        return baseMapper.selectList(new QueryWrapper<CheckIn>().eq("cid", checkId));
    }
}




