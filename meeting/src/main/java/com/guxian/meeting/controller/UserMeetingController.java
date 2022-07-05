package com.guxian.meeting.controller;

import com.guxian.common.entity.PageData;
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
import java.util.List;

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
        if (meeting.getCreateUid() != cuid) {
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
     */
    @GetMapping("/{mid}")
    public ResponseData getUserByMeeting(@PathVariable("mid") Long mid) {
        return ResponseData.success().data(userMeetingService.getUserByMeeting(mid));
    }

    /**
     * 加入会议
     */
//    @PostMapping("/join/{mid}")
//    public ResponseData joinMeeting(@PathVariable Long mid, HttpServletRequest request) {
//        return ResponseData.success().data(userMeetingService.joinMeeting(mid, jwtUtils.getUid(request)));
//    }
//
    /**
     * 会议主持接受加入
     */
//    @PutMapping("/join/{umid}")
//    public ResponseData acceptJoin(@PathVariable Long umid) {
//        boolean join = userMeetingService.acceptJoin(umid);
//        return join ? ResponseData.success("加入成功") : ResponseData.error("加入失败");
//    }

    /**
     * 邀请别人加入
     */
//    @PostMapping("/invite/{mid}")
//    public ResponseData invite(@RequestBody List<Long> uids, @PathVariable Long mid) {
//        userMeetingService.invite(uids, mid);
//        return ResponseData.success();
//    }
//
    /**
     * 用户接受邀请
     */
//    @PutMapping("/invite/{umid}")
//    public ResponseData acceptInvite(@PathVariable Long umid) {
//        boolean invite = userMeetingService.acceptInvite(umid);
//        return invite ? ResponseData.success("加入成功") : ResponseData.error("加入失败");
//    }

    /**
     * 我加入的会议
     */
    @GetMapping("/list")
    public ResponseData list(Long page, Long size) {
        PageData pageData = userMeetingService.list(page, size);
        return ResponseData.success().data(pageData);
    }

    /**
     * 加入会议 (需要白名单)
     */
    @PostMapping("/join/{mid}")
    public ResponseData joinMeeting(@PathVariable Long mid, HttpServletRequest request) {
        return ResponseData.success().data(userMeetingService.joinMeeting(mid, jwtUtils.getUid(request)));
    }

    /**
     * 添加会议白名单
     */
    @PostMapping("/whiteListed/{mid}")
    public ResponseData addWhiteListed(@RequestBody List<Long> list,@PathVariable Long mid){
        userMeetingService.addWhiteListed(list,mid);
        return  ResponseData.success();
    }

    /**
     * 移出会议白名单
     */
    @DeleteMapping("/whiteListed/{mid}/{uid}")
    public ResponseData deleteWhiteListed(@PathVariable Long uid,@PathVariable Long mid){
        userMeetingService.deleteWhiteListed(uid,mid);
        return  ResponseData.success();
    }
}