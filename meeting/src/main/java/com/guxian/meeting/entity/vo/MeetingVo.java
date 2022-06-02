package com.guxian.meeting.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.guxian.meeting.entity.Meeting;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;

@Data
@Accessors(chain = true)
public class MeetingVo {
    private Long id;
    private Integer createUid;
    private String name;
    private String explain;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @CreatedDate
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date beginTime;
    private Integer checkWay;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;
    private Integer state;


    public Meeting toMeeting() {
        return new Meeting()
                .setBeginTime(this.beginTime)
                .setCheckWay(this.checkWay)
                .setCreateTime(this.createTime)
                .setCreateUid(this.createUid)
                .setEndTime(this.endTime)
                .setExplain(this.explain)
                .setId(this.id)
                .setName(this.name)
                .setState(this.state);

    }
}
