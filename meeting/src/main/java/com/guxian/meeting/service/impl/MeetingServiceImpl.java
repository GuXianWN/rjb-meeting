package com.guxian.meeting.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guxian.common.enums.MeetingJoinType;
import com.guxian.common.enums.MeetingState;
import com.guxian.common.enums.RoleType;
import com.guxian.common.entity.PageData;
import com.guxian.common.entity.UserSession;
import com.guxian.common.exception.BizCodeEnum;
import com.guxian.common.exception.ServiceException;
import com.guxian.common.utils.CurrentUserSession;
import com.guxian.common.utils.JwtUtils;
import com.guxian.meeting.clients.UserClient;
import com.guxian.meeting.entity.*;
import com.guxian.meeting.entity.vo.UserVo;
import com.guxian.meeting.service.MeetingCheckService;
import com.guxian.meeting.service.UserMeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guxian.meeting.service.MeetingService;
import com.guxian.meeting.mapper.MeetingMapper;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author GuXian
 * @description 针对表【meeting】的数据库操作Service实现
 * @createDate 2022-05-31 21:08:48
 */
@Service
public class MeetingServiceImpl extends ServiceImpl<MeetingMapper, Meeting>
        implements MeetingService {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private MeetingCheckService meetingCheckService;
    @Autowired
    private UserMeetingService userMeetingService;
    @Autowired
    private UserClient userClient;

    UserSession user = CurrentUserSession.getUserSession();

    @Override
    public Optional<Meeting> addMeeting(Meeting meeting, Long uid) {
        meeting.setCreateTime(new Date())
                .setCreateUid(uid)
                .setJoinType(MeetingJoinType.DEFAULT)
                .setState(MeetingState.WAIT_TO_START);
        this.save(meeting);
        return Optional.ofNullable(meeting.getId() != null ? meeting : null);
    }

    @Override
    public Optional<Meeting> updateMeeting(Meeting toMeeting, Long uid) {
        Meeting meeting = baseMapper.selectById(toMeeting.getId());
        var createUid = meeting.getCreateUid();
        if (!createUid.equals(uid)) {
            UserSession user1 = jwtUtils.getUserForRedis(uid);
            if (user1.getRole() < RoleType.ROLE_ADMIN.getExplain()) {
                throw new ServiceException(BizCodeEnum.NO_ACCESS);
            }
        }

        System.out.println("=======>"+toMeeting.toString());
        baseMapper.updateById(toMeeting);
        return Optional.ofNullable(toMeeting);
    }

    @Override
    public Meeting getMeetingById(Long id) {
        Meeting meeting = Optional.ofNullable(baseMapper.selectById(id))
                .orElseThrow(() -> new ServiceException(BizCodeEnum.MEETING_NOT_EXIST));
        //检查状态
        checkMeetingState(meeting);
        return meeting;
    }

    @Override
    public Optional<Meeting> getMeetingByName(String name) {
        return Optional.ofNullable(baseMapper.selectOne(new QueryWrapper<Meeting>().like("name", name)));
    }

    @Override
    public PageData getAll(Long page, Long size) {
        Page<Meeting> meetingPage = new Page<>(page, size);
        IPage<Meeting> iPage = baseMapper.selectPage(meetingPage, new QueryWrapper<Meeting>());
        //检查状态
        List<Meeting> meetingList = iPage.getRecords();
        meetingList.forEach(this::checkMeetingState);

        return new PageData(page, size, iPage.getTotal(), meetingList);
    }

    @Override
    public PageData getMeetingJoinList(Long page, Long size, Long uid) {
        PageData joinList = userMeetingService.getMeetingJoinList(uid, page, size);
        List<UserMeeting> list = (List<UserMeeting>) joinList.getData();
        List<Meeting> meetings = new ArrayList<>();

        list.forEach(v -> {
            meetings.add(getMeetingById(v.getMid()));
        });

        //检查状态
        meetings.forEach(this::checkMeetingState);
        return joinList.setData(meetings);
    }

    @Override
    public MeetingInfor getMeetingInfo(Long id) {
        Meeting meeting = baseMapper.selectById(id);

        //检查状态
        checkMeetingState(meeting);

        if (meeting == null) {
            throw new ServiceException(BizCodeEnum.MEETING_NOT_EXIST);
        }
        MeetingInfor meetingInfor = MeetingInfor.from(meeting);
        List<CheckInfor> checkInList = meetingCheckService.getCheckInList(id);
        meetingInfor.setOwner(JSON.parseObject(JSON.toJSONString(userClient.infor(meeting.getCreateUid()).getData()), UserVo.class))
                //获取当前会议所有的签到
                .setAttendDetail(checkInList);
        return meetingInfor;
    }

    @Override
    public PageData getMeetingListInfo(Long uid, Long page, Long size) {
        Page<Meeting> page1 = new Page<>(page, size);
        IPage<Meeting> iPage = baseMapper.selectPage(page1, new LambdaQueryWrapper<Meeting>()
                .eq(Meeting::getCreateUid, uid));
        List<MeetingInfor> list = new ArrayList<>();
        List<Meeting> meetingList = iPage.getRecords();

        //检查状态
        meetingList.forEach(this::checkMeetingState);

        meetingList.forEach(v -> {
            list.add(getMeetingInfo(v.getId()));
        });
        return new PageData(page, size, iPage.getTotal(), list);
    }

    @Override
    public void checkMeetingState(Meeting meeting) {
        Date date = new Date();
        //会议还没开始
        if (date.before(meeting.getBeginTime())) {
            check(meeting, MeetingState.WAIT_TO_START);
            return;
        }
        //会议在进行中
        if (date.after(meeting.getBeginTime()) && date.before(meeting.getEndTime())) {
            check(meeting, MeetingState.PROCESSING);
            return;
        }
        //会议结束
        if (date.after(meeting.getEndTime())) {
            check(meeting, MeetingState.OVER);
            return;
        }
    }

    public void check(Meeting meeting, MeetingState state) {
        if (!meeting.getState().equals(state)) {
            meeting.setState(state);
            baseMapper.updateById(meeting);
        }
    }

    @Override
    public PageData listMe(Long page, Long size, Long userId) {
        Page<Meeting> page1 = new Page<>(page, size);
        Page<Meeting> page2 = baseMapper.selectPage(page1, new LambdaQueryWrapper<Meeting>()
                .eq(Meeting::getCreateUid, userId));
        return new PageData(page, size, page2.getTotal(), page2.getRecords());
    }

    @Override
    public Long countMeetingStatus(MeetingState over) {
        return baseMapper.selectCount(new LambdaQueryWrapper<Meeting>()
                .eq(Meeting::getCreateUid, CurrentUserSession.getUserSession().getUserId())
                .eq(Meeting::getState, over));
    }
}




