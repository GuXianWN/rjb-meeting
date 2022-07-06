package com.guxian.meeting.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.guxian.common.RoleType;
import com.guxian.common.entity.ResponseData;
import com.guxian.common.exception.BizCodeEnum;
import com.guxian.common.exception.ServiceException;
import com.guxian.common.utils.CurrentUserSession;
import com.guxian.common.utils.JwtUtils;
import com.guxian.common.valid.AddGroup;
import com.guxian.common.valid.UpdateGroup;
import com.guxian.meeting.entity.Meeting;
import com.guxian.meeting.entity.UserMeeting;
import com.guxian.meeting.entity.vo.MeetingVo;
import com.guxian.meeting.service.MeetingService;
import com.guxian.meeting.service.UserMeetingService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 对会议的操作
 */
//todo 是不是应该直接给表加 joinNum 字段？ 如果不是，那么这个字段应该是计算出来的
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

    @PostMapping
    public ResponseData createMeeting(@RequestBody @Validated(AddGroup.class) MeetingVo meeting, HttpServletRequest request) {
        Long uid = jwtUtils.getUid(request);
        return ResponseData.success()
                .data(meetingService.addMeeting(meeting.toMeeting(), uid)
                        .orElseThrow(() -> new ServiceException(BizCodeEnum.CREATE_MEETING_FAILED)));
    }

    @PatchMapping
    public ResponseData updateMeeting(@RequestBody @Validated(UpdateGroup.class) @NotNull MeetingVo meeting, HttpServletRequest request) {
        Long uid = jwtUtils.getUid(request);
        return ResponseData.success()
                .data(
                        meetingService.updateMeeting(meeting.toMeeting(), uid)
                                .orElseThrow(() -> new ServiceException(BizCodeEnum.UPDATE_MEETING_FAILED)));
    }

    @DeleteMapping("/{id}")
    public ResponseData deleteMeeting(@PathVariable("id") Long mid, HttpServletRequest request) {
        Meeting meeting = meetingService.getMeetingById(mid);
        jwtUtils.RoleVerifyAndException(meeting.getCreateUid(), request, RoleType.ROLE_ADMIN);
        return ResponseData.is(meetingService.removeById(mid));
    }

    @GetMapping("/list/me")
    public ResponseData listMe(Long page,Long size){
        return ResponseData.success().data(meetingService.listMe(page,size,CurrentUserSession.getUserSession().getUserId()));
    }

    @GetMapping("/{id}")
    public ResponseData getMeeting(@PathVariable("id") Long id) {
        //todo 修改获取会议的加入人数的方式 ，并且把这个加入人数的方式放到service层
        // 而且添加获取签到人数和未签到人数 ， 方式也应该放到service层
        return ResponseData.success()
                .data(MeetingVo.fromMeeting(meetingService.getMeetingById(id))
                        .setJoinNum((int) userMeetingService.count(new QueryWrapper<UserMeeting>().eq("mid", id))));
    }

    /**
     * 会议详情，包括签到信息
     *
     * @param id
     * @return
     */
    @GetMapping("/info/{id}")
    public ResponseData getMeetingInfo(@PathVariable("id") Long id) {
        return ResponseData.success()
                .data(meetingService.getMeetingInfo(id));
    }

    @GetMapping("/list/me/info")
    public ResponseData
    getMeetingListInfo(Long page, Long size, HttpServletRequest request) {
        return ResponseData.success()
                .data(meetingService.getMeetingListInfo(jwtUtils.getUid(request), page, size));
    }

    @GetMapping
    public ResponseData getMeetingByName(String name) {
        return ResponseData.success()
                .data(meetingService.getMeetingByName(name).orElseThrow(() -> new ServiceException(BizCodeEnum.MEETING_NOT_EXIST)));
    }

    @GetMapping("/list")
    public ResponseData getMeetingList(Long page, Long size) {
        return ResponseData.success()
                .data(meetingService.getAll(page, size));
    }

    @GetMapping("/list/join")
    public ResponseData getMeetingJoinList(Long page, Long size) {
        return ResponseData.success()
                .data(meetingService.getMeetingJoinList(page, size, CurrentUserSession.getUserSession().getUserId()));
    }
}


