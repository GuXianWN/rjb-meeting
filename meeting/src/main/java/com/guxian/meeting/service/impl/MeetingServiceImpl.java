package com.guxian.meeting.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guxian.meeting.entity.Meeting;
import com.guxian.meeting.service.MeetingService;
import com.guxian.meeting.mapper.MeetingMapper;
import org.apache.ibatis.reflection.wrapper.BaseWrapper;
import org.apache.ibatis.reflection.wrapper.BeanWrapper;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

/**
* @author GuXian
* @description 针对表【meeting】的数据库操作Service实现
* @createDate 2022-05-31 21:08:48
*/
@Service
public class MeetingServiceImpl extends ServiceImpl<MeetingMapper, Meeting>
    implements MeetingService{

    @Override
    public Optional<Meeting> addMeeting(Meeting meeting) {
        baseMapper.insert(meeting);
        return Optional.ofNullable(meeting.getId()!=null?meeting:null);
    }

    @Override
    public Optional<Meeting> updateMeeting(Meeting toMeeting) {
        baseMapper.updateById(toMeeting);
        return Optional.ofNullable(toMeeting);
    }

    @Override
    public Optional<Meeting> getMeetingById(Integer id) {
        return Optional.ofNullable(baseMapper.selectById(id));
    }

    @Override
    public Optional<Meeting> getMeetingByName(String name) {
        return Optional.ofNullable(new QueryWrapper<Meeting>().select("*").eq("name", name).last("limit 1").getEntity());
    }
}




