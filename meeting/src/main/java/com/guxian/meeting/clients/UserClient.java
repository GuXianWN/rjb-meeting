package com.guxian.meeting.clients;

import com.guxian.common.entity.ResponseData;
import com.guxian.meeting.entity.vo.UserVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("auth")
public interface UserClient {
    @GetMapping("/auth/infor/{id}")
    ResponseData infor(@PathVariable("id") Long id);
}
