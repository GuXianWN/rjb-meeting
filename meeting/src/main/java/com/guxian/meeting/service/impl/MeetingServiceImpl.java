package com.guxian.meeting.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guxian.common.entity.CountVo;
import com.guxian.common.entity.PageData;
import com.guxian.common.enums.CheckWay;
import com.guxian.common.enums.MeetingJoinType;
import com.guxian.common.enums.MeetingState;
import com.guxian.common.enums.RoleType;
import com.guxian.common.entity.UserSession;
import com.guxian.common.exception.BizCodeEnum;
import com.guxian.common.exception.ServiceException;
import com.guxian.common.redis.RedisUtils;
import com.guxian.common.utils.CurrentUserSession;
import com.guxian.common.utils.JwtUtils;
import com.guxian.common.utils.PageUtils;
import com.guxian.meeting.clients.UserClient;
import com.guxian.meeting.entity.*;
import com.guxian.meeting.entity.vo.UserVo;
import com.guxian.meeting.service.MeetingCheckService;
import com.guxian.meeting.service.UserMeetingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guxian.meeting.service.MeetingService;
import com.guxian.meeting.mapper.MeetingMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author GuXian
 * @description 针对表【meeting】的数据库操作Service实现
 * @createDate 2022-05-31 21:08:48
 */
@Service
@Slf4j
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
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    UserSession user = CurrentUserSession.getUserSession();

    @Override
    public Optional<Meeting> addMeeting(Meeting meeting, Long uid) {
        Date date = new Date();
        if (meeting.getBeginTime().before(date)) {
            meeting.setBeginTime(date);
        }
        if (meeting.getBeginTime().after(meeting.getEndTime())) {
            throw new ServiceException("开始时间需在结束时间前", 114514);
        }
        meeting.setCreateTime(date)
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

        System.out.println("=======>" + toMeeting.toString());
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
    public PageData getAll(PageUtils pageUtils) {
        Page<Meeting> iPage = baseMapper.selectPage(pageUtils.toPage(Meeting.class), new QueryWrapper<>());

        //检查状态
        List<Meeting> meetingList = iPage.getRecords();
        meetingList.forEach(this::checkMeetingState);

        return new PageData(pageUtils.getPage(),pageUtils.getSize(), iPage.getTotal(), meetingList);
    }

    @Override
    public PageData getMeetingJoinList(Long page, Long size, Long uid) {
        PageData joinList = userMeetingService.getMeetingJoinList(uid, page, size);
        List<UserMeeting> list = (List<UserMeeting>) joinList.getData();
        List<Meeting> meetings = new ArrayList<>();

        list.forEach(v -> {
            Meeting meeting = getMeetingById(v.getMid());
            meeting.setUserMeeting(v);
            meetings.add(meeting);
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

        ValueOperations<String, String> ops = redisTemplate.opsForValue();

        checkInList.forEach(v -> {
            MeetingCheck meetingCheck = v.getMeetingCheck();
            if (meetingCheck.getCheckWay().equals(CheckWay.CODE.getValue())) {
                String code = null;
                try {
                    code = ops.get("meeting_check:check_code:" + meetingCheck.getId()).toString();
                } catch (Exception e) {

                }
                meetingCheck.setCode(code);
            }
        });

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
    public PageData getListInfor(Long page, Long size) {
        Page<Meeting> page1 = new Page<>(page, size);
        IPage<Meeting> iPage = baseMapper.selectPage(page1, new LambdaQueryWrapper<Meeting>());
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
                .eq(Meeting::getState, over));
    }

    @Override
    public void deleteMeeting(Long mid) {
//        Meeting meeting = baseMapper.selectById(mid);
//        if (!meeting.getCreateUid().equals(CurrentUserSession.getUserSession().getUserId())) {
//            throw new ServiceException(BizCodeEnum.NO_ACCESS);
//        }
        userMeetingService.deleteByMid(mid);
        baseMapper.deleteById(mid);
    }

    @Override
    public void end(Long mid) {
        Meeting meeting = baseMapper.selectById(mid);
        if (!meeting.getCreateUid().equals(CurrentUserSession.getUserSession().getUserId())) {
            throw new ServiceException(BizCodeEnum.NO_ACCESS);
        }
        meeting.setEndTime(new Date())
                .setState(MeetingState.OVER);
        baseMapper.updateById(meeting);
    }

    @Override
    public List<CountVo> countTime() {
        LocalDateTime now = LocalDateTime.now();
        List<CountVo> list = new ArrayList<>();
        for (int i = 1; i < 24; i++) {
            LocalDateTime r = LocalDateTime.now().minusDays(i);
            LocalDateTime l = LocalDateTime.now().minusDays(i - 1);
            Long count = baseMapper.selectCount(new LambdaQueryWrapper<Meeting>()
                    .le(Meeting::getCreateTime, l)
                    .ge(Meeting::getCreateTime, r));
            list.add(new CountVo(l, r, count));
        }
        log.info("{}", now);
        return list;
    }
}




