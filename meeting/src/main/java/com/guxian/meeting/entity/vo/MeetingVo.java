package com.guxian.meeting.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.guxian.common.valid.AddGroup;
import com.guxian.common.valid.UpdateGroup;
import com.guxian.meeting.entity.Meeting;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Date;

@Data
@Accessors(chain = true)
public class MeetingVo {
    @Null(message = "id必须为空",groups = {AddGroup.class})
    @NotNull(message = "id不能为空",groups = {UpdateGroup.class})
    private Long id;

    @Null(message = "createId必须为空",groups = {AddGroup.class})
    @NotNull(message = "createId不能为空",groups = {UpdateGroup.class})
    private Long createId;


    private Integer createUid; // 创建人uid
    @NotNull(message = "会议名称不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @NotBlank(message = "会议名称不能为空白",groups = {AddGroup.class,UpdateGroup.class})
    private String name;

    private String explain;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @CreatedDate
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Future(message = "开始时间必须大于当前时间",groups = {AddGroup.class})
    private Date beginTime;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Future(message = "结束时间必须大于开始时间",groups = {AddGroup.class})
    private Date endTime;
    private Integer state;


    public Meeting toMeeting() {
        return new Meeting()
                .setBeginTime(this.beginTime)
                .setCreateTime(this.createTime)
                .setCreateUid(this.createUid)
                .setEndTime(this.endTime)
                .setInstruction(this.explain)
                .setId(this.id)
                .setName(this.name)
                .setState(this.state);

    }
}
