package com.guxian.meeting.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guxian.meeting.entity.UserMeeting;
import com.guxian.meeting.service.UserMeetingService;
import com.guxian.meeting.mapper.UserMeetingMapper;
import org.springframework.stereotype.Service;

/**
* @author GuXian
* @description 针对表【user_meeting】的数据库操作Service实现
* @createDate 2022-05-31 21:08:48
*/
@Service
public class UserMeetingServiceImpl extends ServiceImpl<UserMeetingMapper, UserMeeting>
    implements UserMeetingService{

}




