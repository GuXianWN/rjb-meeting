package com.guxian.meeting.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.guxian.common.entity.ResponseData;
import com.guxian.meeting.clients.UserClient;
import com.guxian.meeting.entity.vo.UserVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class opTest {
    @Autowired
    private UserClient userClient;

    @Test
    public void test() {
        ResponseData data = userClient.infor(1L);
        Object vo= data.getData();
        System.out.println(vo);
        Object o = JSON.toJSON(vo);
        System.out.println(JSON.parseObject(o.toString(), UserVo.class));
    }
}
