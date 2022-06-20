package com.guxian.meeting.controller;

import com.guxian.common.entity.ResponseData;
import com.guxian.common.exception.BizCodeEnum;
import com.guxian.common.exception.ServiceException;
import com.guxian.common.utils.JwtUtils;
import com.guxian.meeting.entity.Meeting;
import com.guxian.meeting.service.MeetingService;
import com.guxian.meeting.service.UserMeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/meeting/user")
public class UserMeetingController {
    @Autowired
    private UserMeetingService userMeetingService;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private MeetingService meetingService;

    @DeleteMapping("/{mid}/{uid}")
    public ResponseData removeUserByMeeting(@PathVariable("mid") Long mid, @PathVariable("uid") Long uid, HttpServletRequest request) {
        Long cuid = jwtUtils.getUid(request);
        Meeting meeting = meetingService.getMeetingById(mid);
        if(meeting.getCreateUid()!=cuid){
            throw new ServiceException(BizCodeEnum.NO_ACCESS);
        }
        int i = userMeetingService.removeUserByMeeting(mid, uid);
        if (i == 0) {
            return ResponseData.error("删除失败");
        }
        return ResponseData.success();
    }

    /**
     * 会议的参与者
     * @param mid
     * @return
     */
    @GetMapping("/{mid}")
    public ResponseData getUserByMeeting(@PathVariable("mid") Long mid) {
        return ResponseData.success().data(userMeetingService.getUserByMeeting(mid));
    }
}
