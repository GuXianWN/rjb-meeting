package com.guxian.meeting.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guxian.meeting.entity.Meeting;
import com.guxian.meeting.service.MeetingService;
import com.guxian.meeting.mapper.MeetingMapper;
import org.springframework.stereotype.Service;

/**
* @author GuXian
* @description 针对表【meeting】的数据库操作Service实现
* @createDate 2022-05-31 21:08:48
*/
@Service
public class MeetingServiceImpl extends ServiceImpl<MeetingMapper, Meeting>
    implements MeetingService{

}




