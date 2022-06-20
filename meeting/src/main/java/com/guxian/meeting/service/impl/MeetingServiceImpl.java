package com.guxian.meeting.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guxian.common.RoleType;
import com.guxian.common.entity.PageData;
import com.guxian.common.entity.UserSession;
import com.guxian.common.exception.BizCodeEnum;
import com.guxian.common.exception.ServiceException;
import com.guxian.common.utils.CurrentUserSession;
import com.guxian.common.utils.JwtUtils;
import com.guxian.meeting.clients.UserClient;
import com.guxian.meeting.entity.MeetingInfor;
import com.guxian.meeting.entity.vo.UserVo;
import com.guxian.meeting.service.MeetingCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guxian.meeting.entity.Meeting;
import com.guxian.meeting.service.MeetingService;
import com.guxian.meeting.mapper.MeetingMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    private UserClient userClient;

    UserSession user = CurrentUserSession.getUserSession();

    @Override
    public Optional<Meeting> addMeeting(Meeting meeting,Long uid) {
        meeting.setCreateTime(new Date());
        this.save(meeting.setCreateUid(uid));
        return Optional.ofNullable(meeting.getId() != null ? meeting : null);
    }

    @Override
    public Optional<Meeting> updateMeeting(Meeting toMeeting) {
        Meeting meeting = baseMapper.selectById(toMeeting.getId());
        var createUid = meeting.getCreateUid();
        var currentUserId = CurrentUserSession.getUserSession().getUserId();
        if (!currentUserId.equals(createUid)) {
            UserSession user1 = jwtUtils.getUserForRedis(currentUserId);
            if (user1.getRole() < RoleType.ROLE_ADMIN.getExplain()) {
                throw new ServiceException(BizCodeEnum.NO_ACCESS);
            }
        }
        baseMapper.updateById(toMeeting);
        return Optional.ofNullable(toMeeting);
    }

    @Override
    public Meeting getMeetingById(Long id) {
        return Optional.ofNullable(baseMapper.selectById(id))
                .orElseThrow(() -> new ServiceException(BizCodeEnum.MEETING_NOT_EXIST));
    }

    @Override
    public Optional<Meeting> getMeetingByName(String name) {
        return Optional.ofNullable(baseMapper.selectOne(new QueryWrapper<Meeting>().like("name", name)));
    }

    @Override
    public PageData getAll(Long page, Long size) {
        Page<Meeting> meetingPage = new Page<>(page, size);
        IPage<Meeting> iPage = baseMapper.selectPage(meetingPage, new QueryWrapper<Meeting>());
        return new PageData(page, size, iPage.getTotal(), iPage.getRecords());
    }

    @Override
    public PageData getMe(Long page, Long size, Long uid) {
        Page<Meeting> meetingPage = new Page<>(page, size);
        IPage<Meeting> iPage = baseMapper.selectPage(meetingPage,
                new LambdaQueryWrapper<Meeting>().eq(Meeting::getCreateUid, uid));
        return new PageData(page, size, iPage.getTotal(), iPage.getRecords());
    }

    @Override
    public MeetingInfor getMeetingInfo(Long id) {
        Meeting meeting = baseMapper.selectById(id);
        if (meeting == null) {
            throw new ServiceException(BizCodeEnum.MEETING_NOT_EXIST);
        }
        MeetingInfor meetingInfor = MeetingInfor.from(meeting);
        meetingInfor.setOwner(JSON.parseObject(JSON.toJSONString(userClient.infor(meeting.getCreateUid()).getData()), UserVo.class))
                //获取当前会议所有的签到
                .setAttendDetail(meetingCheckService.getCheckInList(id));
        return meetingInfor;
    }
}




