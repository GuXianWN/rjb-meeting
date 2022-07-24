package com.guxian.meeting.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guxian.common.enums.CheckWay;
import com.guxian.common.exception.BizCodeEnum;
import com.guxian.common.exception.ServiceException;
import com.guxian.common.redis.RedisUtils;
import com.guxian.common.utils.SomeUtils;
import com.guxian.meeting.entity.CheckIn;
import com.guxian.meeting.entity.CheckInfor;
import com.guxian.meeting.entity.Meeting;
import com.guxian.meeting.entity.MeetingCheck;
import com.guxian.meeting.service.CheckInService;
import com.guxian.meeting.service.MeetingCheckService;
import com.guxian.meeting.mapper.MeetingCheckMapper;
import com.guxian.meeting.service.MeetingService;
import com.guxian.meeting.service.UserMeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 *
 */
@Service
public class MeetingCheckServiceImpl extends ServiceImpl<MeetingCheckMapper, MeetingCheck>
        implements MeetingCheckService {

    @Autowired
    private MeetingService meetingService;
    @Autowired
    private CheckInService checkInService;
    @Autowired
    private UserMeetingService userMeetingService;


    public Optional<MeetingCheck> createMeetingCheckUseCode(MeetingCheck meetingCheck,@NotNull(message = "code 不能为空") String data) {
        RedisUtils.ops.set("meeting_check:check_code:" + meetingCheck.getId(), data ,
                meetingCheck.getDuration(), TimeUnit.SECONDS);
        return Optional.of(meetingCheck);
    }

    public Optional<MeetingCheck> createMeetingCheckUseFace(MeetingCheck toMeetingCheck, String data) {
        RedisUtils.ops.set("meeting_check:" + toMeetingCheck.getCheckWay() + ":" + toMeetingCheck.getId(),
                JSON.toJSONString(toMeetingCheck),
                toMeetingCheck.getDuration(), TimeUnit.SECONDS);
        return Optional.of(toMeetingCheck);
    }

    @Override
    public Optional<MeetingCheck> createMeetingCheck(MeetingCheck meetingCheck, String data, Long uid) {
        Meeting meeting = meetingService.getMeetingById(meetingCheck.getMeetingId());
        if (!meeting.getCreateUid().equals(uid)) {
            throw new ServiceException(BizCodeEnum.NO_ACCESS);
        }

        meetingCheck.setBeginTime(Date.from(Instant.now()));

        //已经存在的会议签到，则删除原有的签到，重新创建新的签到
//        if (baseMapper.selectById(meetingCheck.getMeetingId()) != null) {
//            baseMapper.deleteById(meetingCheck.getMeetingId());
//        }

        baseMapper.insert(meetingCheck);


        /**
         * 创建签到 ,不同的签到方式，创建的签到不同
         */
        if (meetingCheck.getCheckWay() == CheckWay.CODE.getValue()) {
            return createMeetingCheckUseCode(meetingCheck, data);
        } else {
            return createMeetingCheckUseFace(meetingCheck, data);
        }
    }

    @Override
    public List<CheckInfor> getCheckInList(Long id) {
        //会议加入者数量
        int meetingJoin = userMeetingService.getUserByMeeting(id).size();

        List<MeetingCheck> meetingCheckList = baseMapper.selectList(new QueryWrapper<MeetingCheck>()
                .eq("meeting_id", id));
        List<CheckInfor> list = new ArrayList<>();
        meetingCheckList.forEach(v -> {
            list.add(new CheckInfor(v));
        });

        list.forEach(v -> {
            //获取签到列表
            List<CheckIn> checkInList = checkInService.getCheckInList(v.getMeetingCheck().getId());
            v.setCheckInList(checkInList)
                    .setCheckNum(checkInList.size())
                    .setJoinNum(meetingJoin);
        });
        return list;
    }

    @Override
    public MeetingCheck getCheckById(Long checkId) {
        return Optional.ofNullable(baseMapper.selectById(checkId))
                .orElseThrow(() -> new ServiceException(BizCodeEnum.CHECK_DOES_NOT_EXIST));
    }

    @Override
    public Map<Integer,Long> getMeetingTypeList() {

        var length = CheckWay.values().length;
        var map = new HashMap<Integer,Long>();
        for (int i = 0; i < length; i++) {
            var tmp = baseMapper.selectCount(new QueryWrapper<>(new MeetingCheck())
                    .eq("check_way", (i)));
            map.put(i, tmp);
        }

        return map;
    }
}




