package com.guxian.meeting.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guxian.meeting.entity.CheckIn;
import com.guxian.meeting.service.CheckInService;
import com.guxian.meeting.mapper.CheckInMapper;
import org.springframework.stereotype.Service;

/**
* @author GuXian
* @description 针对表【check_in】的数据库操作Service实现
* @createDate 2022-05-31 21:08:48
*/
@Service
public class CheckInServiceImpl extends ServiceImpl<CheckInMapper, CheckIn>
    implements CheckInService{

    @Override
    public boolean checkIn(Long meetingId) {
        return true;
    }
}




