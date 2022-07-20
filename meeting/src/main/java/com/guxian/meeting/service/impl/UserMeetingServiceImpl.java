package com.guxian.meeting.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guxian.common.enums.CheckWay;
import com.guxian.common.enums.MeetingJoinState;
import com.guxian.common.enums.MeetingJoinType;
import com.guxian.common.enums.UserCheckInStatus;
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
    public PageData getMeetingJoinList(Long uid, Long page, Long size) {
        Page<UserMeeting> page1 = new Page<>();
        Page<UserMeeting> iPage = baseMapper.selectPage(page1, new QueryWrapper<UserMeeting>()
                .eq("uid", uid));
        return new PageData(page, size, iPage.getTotal(), iPage.getRecords());
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

    /**
     * 加入会议 (需要白名单)
     */
    @Override
    public UserMeeting joinMeeting(Long mid, Long uid) {
        Meeting meeting = meetingService.getMeetingById(mid);

        UserMeeting userMeeting = baseMapper.selectOne(new LambdaQueryWrapper<UserMeeting>()
                .eq(UserMeeting::getUid, uid)
                .eq(UserMeeting::getMid, mid));

        //会议加入方式为def直接放进去
        if (meeting.getJoinType().equals(MeetingJoinType.DEFAULT)) {
            if (userMeeting == null) {
                return toJoin(mid, uid);
            }
        }
        //会议加入方式为白名单 检查是否为白名单 修改状态
        if (meeting.getJoinType().equals(MeetingJoinType.WHITELIST)) {
            if (userMeeting == null) {
                throw new ServiceException(BizCodeEnum.NOT_WHITE_LISTED);
            }
            if (userMeeting.getJoinState().equals(MeetingJoinState.WHITELIST)) {
                toJoin(userMeeting);
            }
        }
        return userMeeting;
    }

    public UserMeeting toJoin(Long mid, Long uid) {
        UserMeeting userMeeting = new UserMeeting()
                .setUid(uid)
                .setMid(mid)
                .setJoinTime(new Date())
                .setJoinState(MeetingJoinState.Accepted);
        baseMapper.insert(userMeeting);
        return userMeeting;
    }

    public UserMeeting toJoin(UserMeeting userMeeting) {
        userMeeting.setJoinTime(new Date())
                .setJoinState(MeetingJoinState.Accepted);
        baseMapper.updateById(userMeeting);
        return userMeeting;
    }


    /**
     * 添加白名单
     */
    @Override
    public void addWhiteListed(List<Long> list, Long mid) {
        Meeting meeting = meetingService.getMeetingById(mid);
        if (!meeting.getCreateUid().equals(CurrentUserSession.getUserSession().getUserId())) {
            throw new ServiceException(BizCodeEnum.NO_ACCESS);
        }

        list.forEach(v -> {
            UserMeeting userMeeting = baseMapper.selectOne(new LambdaQueryWrapper<UserMeeting>()
                    .eq(UserMeeting::getUid, v)
                    .eq(UserMeeting::getMid, mid));

            if (userMeeting == null) {
                baseMapper.insert(new UserMeeting()
                        .setJoinState(MeetingJoinState.WHITELIST)
                        .setUid(v)
                        .setMid(mid));
            }
        });
    }

    @Override
    public void deleteWhiteListed(Long uid, Long mid) {
        log.warn("deleteWhiteListed");
        Meeting meeting = meetingService.getMeetingById(mid);
        if (!meeting.getCreateUid().equals(CurrentUserSession.getUserSession().getUserId())) {
            throw new ServiceException(BizCodeEnum.NO_ACCESS);
        }

        UserMeeting userMeeting = baseMapper.selectOne(new LambdaQueryWrapper<UserMeeting>()
                .eq(UserMeeting::getUid, uid)
                .eq(UserMeeting::getMid, mid));

        if (userMeeting == null) {
            throw new ServiceException(BizCodeEnum.NOT_WHITE_LISTED);
        }
        if (!userMeeting.getJoinState().equals(MeetingJoinState.WHITELIST.getExplain())) {
            throw new ServiceException(BizCodeEnum.NOT_WHITE_LISTED);
        }

        baseMapper.deleteById(userMeeting.getId());
    }

    @Override
    public void deleteByMid(Long meeting) {
        baseMapper.delete(new LambdaQueryWrapper<UserMeeting>()
                .eq(UserMeeting::getMid, meeting));
    }

    @Override
    public void quit(Long mid) {
        baseMapper.delete(new LambdaQueryWrapper<UserMeeting>()
                .eq(UserMeeting::getMid, mid)
                .eq(UserMeeting::getUid, CurrentUserSession.getUserSession().getUserId()));
    }
}




