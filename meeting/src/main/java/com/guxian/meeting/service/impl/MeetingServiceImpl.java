package com.guxian.meeting.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guxian.common.RoleType;
import com.guxian.common.entity.UserSession;
import com.guxian.common.exception.BizCodeEnum;
import com.guxian.common.exception.ServiceException;
import com.guxian.common.utils.CurrentUserSession;
import com.guxian.common.utils.JwtUtils;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guxian.meeting.entity.Meeting;
import com.guxian.meeting.service.MeetingService;
import com.guxian.meeting.mapper.MeetingMapper;
import org.apache.ibatis.reflection.wrapper.BaseWrapper;
import org.apache.ibatis.reflection.wrapper.BeanWrapper;
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

    UserSession user = CurrentUserSession.getUserSession();

    @Override
    public Optional<Meeting> addMeeting(Meeting meeting, Long id) {
        meeting.setCreateTime(new Date());
        this.save(meeting.setCreateUid(id));
        return Optional.ofNullable(meeting.getId() != null ? meeting : null);
    }

    @Override
    public Optional<Meeting> updateMeeting(Meeting toMeeting, Long uid) {
        Meeting meeting = baseMapper.selectById(toMeeting.getId());
        Long createUid = meeting.getCreateUid();
        if (!uid.equals(createUid)) {
            UserSession user1 = jwtUtils.getUserForRedis(uid);
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
    public List<Meeting> getAll(int page, int size) {
        Page<Meeting> meetingPage = new Page<>(page, size);
        IPage<Meeting> iPage = baseMapper.selectPage(meetingPage, new QueryWrapper<Meeting>());
        return iPage.getRecords();
    }

    @Override
    public List<Meeting> getAll(int page, int size, Long uid) {
        Page<Meeting> meetingPage = new Page<>(page, size);
        IPage<Meeting> iPage = baseMapper.selectPage(meetingPage,
                new QueryWrapper<Meeting>()
                        .eq("create_uid",uid));
        return iPage.getRecords();
    }
}




