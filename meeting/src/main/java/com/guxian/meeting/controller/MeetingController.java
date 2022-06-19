package com.guxian.meeting.controller;

import com.guxian.common.entity.ResponseData;
import com.guxian.common.exception.BizCodeEnum;
import com.guxian.common.exception.ServiceException;
import com.guxian.common.utils.JwtUtils;
import com.guxian.common.valid.AddGroup;
import com.guxian.common.valid.UpdateGroup;
import com.guxian.meeting.entity.vo.MeetingVo;
import com.guxian.meeting.service.MeetingService;
import com.guxian.meeting.service.UserMeetingService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 对会议的操作
 */
@RestController
@RequestMapping("/meeting")
public class MeetingController {
    /**
     * JWT工具类
     */
    private final JwtUtils jwtUtils;
    /**
     * 会议服务类
     */
    private final MeetingService meetingService;
    /**
     * 用户与会议<strong style="color:red">关系</strong>服务类
     **/
    private final UserMeetingService userMeetingService;

    public MeetingController(@Autowired MeetingService meetingService
            , @Autowired JwtUtils jwtUtils
            , @Autowired UserMeetingService userMeetingService) {
        this.meetingService = meetingService;
        this.jwtUtils = jwtUtils;
        this.userMeetingService = userMeetingService;
    }

    /**
     * 添加会议
     *
     * @param meeting
     * @return
     */

    @PostMapping("/")
    public ResponseData createMeeting(@RequestBody @Validated(AddGroup.class) MeetingVo meeting, HttpServletRequest request) {
        return ResponseData.success()
                .data(meetingService.addMeeting(meeting.toMeeting(), jwtUtils.getUid(request))
                        .orElseThrow(() -> new ServiceException(BizCodeEnum.CREATE_MEETING_FAILED)));
    }

    @PatchMapping("/")
    public ResponseData updateMeeting(@RequestBody @Validated(UpdateGroup.class) MeetingVo meeting, HttpServletRequest request) {
        Long uid = jwtUtils.getUid(request);
        return ResponseData.success()
                .data(
                        meetingService.updateMeeting(meeting.toMeeting(), uid)
                                .orElseThrow(() -> new ServiceException(BizCodeEnum.UPDATE_MEETING_FAILED)));
    }

    @DeleteMapping("/{id}")
    public ResponseData deleteMeeting(@PathVariable("id") Long id) {
        return ResponseData.is(meetingService.removeById(id));
    }

    @GetMapping("/{id}")
    public ResponseData getMeeting(@PathVariable("id") Long id) {
        return ResponseData.success()
                .data(MeetingVo.fromMeeting(meetingService.getMeetingById(id)));
    }

    @GetMapping("/")
    public ResponseData getMeetingByName(String name) {
        return ResponseData.success()
                .data(meetingService.getMeetingByName(name).orElseThrow(() -> new ServiceException(BizCodeEnum.MEETING_NOT_EXIST)));
    }

    @GetMapping("/list")
    public ResponseData getMeetingList(int page, int size) {
        return ResponseData.success()
                .data(meetingService.getAll(page, size));
    }

    @GetMapping("/list/me")
    public ResponseData getMeetingListForMe(int page, int size, HttpServletRequest request) {
        Long uid = jwtUtils.getUid(request);
        return ResponseData.success()
                .data(meetingService.getAll(page, size, uid));
    }
}


