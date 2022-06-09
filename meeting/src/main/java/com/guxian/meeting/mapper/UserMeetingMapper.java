package com.guxian.meeting.mapper;

import com.guxian.meeting.entity.UserMeeting;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Entity com.guxian.meeting.entity.UserMeeting
 */
public interface UserMeetingMapper extends BaseMapper<UserMeeting> {

    @Select("select * from user_meeting where uid = #{uid} and mid = #{mid}")
    List<UserMeeting> queryByMidAndUid(long mid, long uid);
}




