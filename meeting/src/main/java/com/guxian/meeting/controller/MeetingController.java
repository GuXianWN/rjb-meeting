package com.guxian.meeting.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.guxian.common.entity.PageData;
import com.guxian.common.entity.Resp;
import com.guxian.common.entity.ResponseData;
import com.guxian.common.enums.MeetingState;
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
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 对会议的操作
 */
@RestController
@Slf4j
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

    public MeetingController(JwtUtils jwtUtils
            , MeetingService meetingService
            , UserMeetingService userMeetingService) {
        this.jwtUtils = jwtUtils;
        this.meetingService = meetingService;
        this.userMeetingService = userMeetingService;
    }


    @GetMapping("/count")
    public ResponseData count(){
        return ResponseData.success().data(meetingService.countTime());
    }

    /**
     * 添加会议
     *
     * @param meeting
     * @return
     */

    @PostMapping
    public ResponseData createMeeting(@RequestBody @Validated(AddGroup.class) MeetingVo meeting) {
        Long uid = CurrentUserSession.getUserSession().getUserId();
        return ResponseData.success()
                .data(meetingService.addMeeting(meeting.toMeeting(), uid)
                        .orElseThrow(() -> new ServiceException(BizCodeEnum.CREATE_MEETING_FAILED)));
    }

    @PostMapping("/end/{mid}")
    public ResponseData end(@PathVariable Long mid){
        meetingService.end(mid);
        return ResponseData.success();
    }

    @PatchMapping
    public ResponseData updateMeeting(@RequestBody @Validated(UpdateGroup.class) @NotNull MeetingVo meeting) {
        log.info("{}",meeting);
        Long uid = CurrentUserSession.getUserSession().getUserId();
        return ResponseData.success()
                .data(
                        meetingService.updateMeeting(meeting.toMeeting(), uid)
                                .orElseThrow(() -> new ServiceException(BizCodeEnum.UPDATE_MEETING_FAILED)));
    }

    @DeleteMapping("/{id}")
    public ResponseData deleteMeeting(@PathVariable("id") Long mid) {
        meetingService.deleteMeeting(mid);
        return ResponseData.success();
    }

    @GetMapping("/list/me")
    public ResponseData listMe(Long page, Long size) {
        PageData data = meetingService.listMe(page, size, CurrentUserSession.getUserSession().getUserId());
        return ResponseData.success().data(data);
    }

    @GetMapping("/list/me/info")
    public ResponseData getMeetingListInfo(Long page, Long size) {
        return ResponseData.success()
                .data(meetingService.getMeetingListInfo(CurrentUserSession.getUserSession().getUserId(), page, size));
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
    @GetMapping("/infor/{id}")
    public ResponseData getMeetingInfo(@PathVariable("id") Long id) {
        return ResponseData.success()
                .data(meetingService.getMeetingInfo(id));
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

    @GetMapping("/list/status")
    public Resp  countMeetingStatus() {
        return Resp.success()
                .data(MeetingState.WAIT_TO_START.getDis(), meetingService.countMeetingStatus(MeetingState.WAIT_TO_START))
                .data(MeetingState.PROCESSING.getDis(), meetingService.countMeetingStatus(MeetingState.PROCESSING))
                .data(MeetingState.OVER.getDis(), meetingService.countMeetingStatus(MeetingState.OVER));
    }
}


