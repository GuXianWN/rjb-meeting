package com.guxian.meeting.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guxian.common.enums.CheckWay;
import com.guxian.common.entity.ResponseData;
import com.guxian.common.exception.BizCodeEnum;
import com.guxian.common.exception.ServiceException;
import com.guxian.common.redis.RedisUtils;
import com.guxian.common.utils.CurrentUserSession;
import com.guxian.meeting.clients.FaceCheckClient;
import com.guxian.meeting.entity.CheckIn;
import com.guxian.meeting.entity.Meeting;
import com.guxian.meeting.entity.MeetingCheck;
import com.guxian.meeting.entity.vo.CheckDataVo;
import com.guxian.meeting.entity.vo.ReCheckVo;
import com.guxian.meeting.service.CheckInService;
import com.guxian.meeting.mapper.CheckInMapper;
import com.guxian.meeting.service.MeetingCheckService;
import com.guxian.meeting.service.UserMeetingService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author GuXian
 * @description 针对表【check_in】的数据库操作Service实现
 * @createDate 2022-05-31 21:08:48
 */

@Service
@Log4j2
public class DefaultCheckInService extends ServiceImpl<CheckInMapper, CheckIn> implements CheckInService {
    @Autowired
    private UserMeetingService userMeetingService;
    @Autowired
    private FaceCheckClient faceCheckClient;

    @Autowired
    private MeetingServiceImpl meetingService;
    @Autowired
    private MeetingCheckService meetingCheckService;


    /**
     * @param checkDataVo
     * @return boolean 是否签到成功
     * @author GuXian
     */


    @Override
    public boolean checkIn(CheckDataVo checkDataVo) {
        if (checkDataVo.getCheckWay() == CheckWay.FACE && checkInUseFace(checkDataVo)) {
            userMeetingService.checkIn(CurrentUserSession.getUserSession().getUserId(),
                    checkDataVo.getCheckWay());
        }

        if (checkDataVo.getCheckWay() == CheckWay.CODE && checkInUseFace(checkDataVo)) {
            userMeetingService.checkIn(CurrentUserSession.getUserSession().getUserId(),
                    checkDataVo.getCheckWay());
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
        var responseData = faceCheckClient.compareFace(face);
        return ResponseData.returnIs(responseData);
    }

    @Override
    public List<CheckIn> getCheckInList(Long checkId) {
        return baseMapper.selectList(new QueryWrapper<CheckIn>().eq("cid", checkId));
    }

    @Override
    public void reCheck(ReCheckVo reCheckVo, Long uid) {
        Meeting meeting = meetingService.getMeetingById(reCheckVo.getMid());
        if (!meeting.getCreateUid().equals(uid)) {
            throw new ServiceException(BizCodeEnum.NO_ACCESS);
        }
        meetingCheckService.getCheckById(reCheckVo.getCheckId());
        baseMapper.insert(new CheckIn(null, new Date(), reCheckVo.getUid(), reCheckVo.getCheckId()));
    }
}




