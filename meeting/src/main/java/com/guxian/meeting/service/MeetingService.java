package com.guxian.meeting.service;

import com.guxian.meeting.entity.Meeting;
import com.baomidou.mybatisplus.extension.service.IService;
import lombok.Setter;

import java.util.List;
import java.util.Optional;

/**
 * @author GuXian
 * @description 针对表【meeting】的数据库操作Service
 * @createDate 2022-05-31 21:08:48
 */

public interface MeetingService extends IService<Meeting> {
    Optional<Meeting> addMeeting(Meeting meeting);

    Optional<Meeting> updateMeeting(Meeting toMeeting);

    Optional<Meeting> getMeetingByName(String name);

    Optional<Meeting> getMeetingById(Long id);

    List<Meeting> getAll(int page, int size);
}
