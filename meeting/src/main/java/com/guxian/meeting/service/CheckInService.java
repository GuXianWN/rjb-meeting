package com.guxian.meeting.service;

import com.guxian.meeting.entity.CheckIn;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guxian.meeting.entity.vo.CheckDataVo;
import org.springframework.cloud.client.loadbalancer.RequestData;

import java.util.List;

/**
 * @author GuXian
 * @description 针对表【check_in】的数据库操作Service
 * @createDate 2022-05-31 21:08:48
 */
public interface CheckInService extends IService<CheckIn> {

    boolean checkIn(CheckDataVo checkDataVo);

    List<CheckIn> getCheckInList(Long checkId);
}
