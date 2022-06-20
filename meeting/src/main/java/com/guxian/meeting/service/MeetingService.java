package com.guxian.meeting.service;

import com.guxian.common.entity.PageData;
import com.guxian.meeting.entity.Meeting;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guxian.meeting.entity.MeetingInfor;
import com.guxian.meeting.entity.vo.UserVo;

import java.util.List;
import java.util.Optional;

/**
 * @author GuXian
 * @description 针对表【meeting】的数据库操作Service
 * @createDate 2022-05-31 21:08:48
 */

public interface MeetingService extends IService<Meeting> {
    Optional<Meeting> addMeeting(Meeting meeting,Long uid);

    Optional<Meeting> updateMeeting(Meeting toMeeting);

    Optional<Meeting> getMeetingByName(String name);

    Meeting getMeetingById(Long id);

    PageData getAll(Long page, Long size); // 获取所有会议

    PageData getMe(Long page, Long size, Long uid); // 获取我参加的会议

    MeetingInfor getMeetingInfo(Long id);
}
