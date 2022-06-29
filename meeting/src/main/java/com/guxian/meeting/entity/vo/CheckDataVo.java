package com.guxian.meeting.entity.vo;

import com.guxian.common.CheckWay;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.File;

/**
 * 签到时需要传递的数据
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CheckDataVo {
    @NotNull(message = "会议ID不能为空")
    Long meetingId;
    String code;
    File face;
    @NotNull(message = "签到方式不能为空")
    CheckWay checkWay;
}
