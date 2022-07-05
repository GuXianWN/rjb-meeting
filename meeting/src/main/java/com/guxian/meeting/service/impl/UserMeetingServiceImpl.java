package com.guxian.meeting.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guxian.common.CheckWay;
import com.guxian.common.MeetingJoinState;
import com.guxian.common.UserCheckInStatus;
import com.guxian.common.entity.PageData;
import com.guxian.common.exception.BizCodeEnum;
import com.guxian.common.exception.ServiceException;
import com.guxian.common.utils.CurrentUserSession;
import com.guxian.common.utils.JwtUtils;
import com.guxian.meeting.entity.Meeting;
import com.guxian.meeting.entity.MeetingCheck;
import com.guxian.meeting.entity.UserMeeting;
import com.guxian.meeting.service.MeetingService;
import com.guxian.meeting.service.UserMeetingService;
import com.guxian.meeting.mapper.UserMeetingMapper;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 *
 */
@Service
public class UserMeetingServiceImpl extends ServiceImpl<UserMeetingMapper, UserMeeting>
        implements UserMeetingService {

    @Resource
    private MeetingService meetingService;
    @Autowired
    private JwtUtils jwtUtils;
    @Resource
    private UserMeetingMapper userMeetingMapper;

    @Override
    public Optional<MeetingCheck> addCheckType(MeetingCheck meetingCheck, String code) {
        return Optional.empty();
    }

    @Override
    public void checkIn(Long meetingId, CheckWay way) {
        List<UserMeeting> userMeetings = baseMapper.queryByMidAndUid(meetingId, CurrentUserSession.getUserSession().getUserId());
        if (!userMeetings.isEmpty()) {
            throw new ServiceException(BizCodeEnum.CHECK_IN_ALREADY);
        }

        this.save(new UserMeeting()
                .setType(UserCheckInStatus.JOIN)
                .setMid(meetingId)
                .setJoinTime(Date.from(Instant.now()))
                .setUid(CurrentUserSession.getUserSession().getUserId()));
    }

    @Override
    public int removeUserByMeeting(Long mid, Long uid) {
        return baseMapper.delete(new LambdaQueryWrapper<>(new UserMeeting()
                .setMid(mid)
                .setUid(uid)));
    }

    @Override
    public List<UserMeeting> getUserByMeeting(Long mid) {
        return baseMapper.selectList(new LambdaQueryWrapper<>(new UserMeeting()
                .setMid(mid)));
    }


    @Override
    public UserMeeting joinMeeting(Long mid, Long uid) {
        UserMeeting userMeeting1 = baseMapper.selectOne(new LambdaQueryWrapper<UserMeeting>()
                .eq(UserMeeting::getUid, uid)
                .eq(UserMeeting::getMid, mid));
        if (userMeeting1 != null) {
            throw new ServiceException(BizCodeEnum.JOINED);
        }

        UserMeeting userMeeting = new UserMeeting();
        userMeeting.setMid(mid)
                .setUid(uid)
                .setJoinState(MeetingJoinState.JoinWaitingToBeAccepted.getExplain());
        baseMapper.insert(userMeeting);
        return userMeeting;
    }

    @Override
    public PageData getMeetingJoinList(Long uid, Long page, Long size) {
        Page<UserMeeting> page1 = new Page<>();
        Page<UserMeeting> iPage = baseMapper.selectPage(page1, new QueryWrapper<UserMeeting>()
                .eq("uid", uid));
        return new PageData(page, size, iPage.getTotal(), iPage.getRecords());
    }

    @Override
    public void invite(List<Long> uids, Long mid) {
        Meeting meeting = meetingService.getMeetingById(mid);
        if (!meeting.getCreateUid().equals(CurrentUserSession.getUserSession().getUserId())) {
            throw new ServiceException(BizCodeEnum.NO_ACCESS);
        }

        //TODO 可以换批量插入插件
        uids.forEach(v -> {
            UserMeeting one = baseMapper.selectOne(new LambdaQueryWrapper<UserMeeting>()
                    .eq(UserMeeting::getMid, mid)
                    .eq(UserMeeting::getUid, v));

            if (one == null) {
                baseMapper.insert(new UserMeeting()
                        .setUid(v)
                        .setMid(mid)
                        .setJoinState(MeetingJoinState.InviteWaitingToBeAccepted.getExplain()));
            }
        });
    }

    /**
     * 用户接受会议主持的邀请
     */
    @Override
    public boolean acceptInvite(Long umid) {
        UserMeeting userMeeting = selectById(umid);
        if (!userMeeting.getUid().equals(CurrentUserSession.getUserSession().getUserId())) {
            throw new ServiceException(BizCodeEnum.NO_ACCESS);
        }
        int update = baseMapper.updateById(userMeeting
                .setJoinState(MeetingJoinState.Accepted.getExplain())
                .setJoinTime(new Date()));
        return update != 0;
    }

    /**
     * 会议主持接受用户的加入
     */
    @Override
    public boolean acceptJoin(Long umid) {
        UserMeeting userMeeting = selectById(umid);
        Meeting meeting = meetingService.getMeetingById(userMeeting.getMid());
        if (!meeting.getCreateUid().equals(CurrentUserSession.getUserSession().getUserId())) {
            throw new ServiceException(BizCodeEnum.NO_ACCESS);
        }
        int update = baseMapper.updateById(userMeeting
                .setType("ok")
                .setJoinState(MeetingJoinState.Accepted.getExplain())
                .setJoinTime(new Date()));
        return update != 0;
    }

    @Override
    public UserMeeting selectById(Long umid) {
        return Optional.ofNullable(baseMapper.selectById(umid))
                .orElseThrow(() -> new ServiceException(BizCodeEnum.NOTJOINED_OR_NOTINVITE));
    }

    @Override
    public PageData list(Long page, Long size) {
        Page<UserMeeting> page1 = new Page<>(page, size);
        Page<UserMeeting> page2 = baseMapper.selectPage(page1, new LambdaQueryWrapper<UserMeeting>()
                .eq(UserMeeting::getUid, CurrentUserSession.getUserSession().getUserId()));
        return new PageData(page, size, page2.getTotal(), page2.getRecords());
    }
}




