package com.guxian.common.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.guxian.common.exception.BizCodeEnum;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

/**
 * 公共返回对象
 *
 * @author GuXianWN
 */
@ToString
@Data
@Accessors(chain = true)
public class ResponseData {

    private ResponseData() {
    }

    @JsonProperty("code")
    private Integer code;
    @JsonProperty("msg")
    private String message;
    @JsonProperty("data")
    private Object data = null;


    public static ResponseData atBizCodeEnum(BizCodeEnum bizCodeEnum) {
        ResponseData respBean = new ResponseData();
        respBean.setCode(bizCodeEnum.getCode())
                .setMessage(bizCodeEnum.getMsg());
        return respBean;
    }

    public static ResponseData success(Integer code, String message) {
        ResponseData respData = new ResponseData();
        respData.setCode(code);
        respData.setMessage(message);
        return respData;
    }

    public static ResponseData success(Object data) {
        ResponseData respBean = new ResponseData();
        respBean.setCode(0);
        respBean.setMessage("success");
        respBean.setData(data);
        return respBean;
    }

    public static ResponseData success(String key, Object data) {
        ResponseData respBean = new ResponseData();
        respBean.setCode(0);
        respBean.setMessage("success");
        var tmp = new HashMap<>();
        tmp.put(key, data);
        respBean.setData(tmp);
        return respBean;
    }

    public static ResponseData error(Integer code, String message) {
        ResponseData respBean = new ResponseData();
        respBean.setCode(code);
        respBean.setMessage(message);
        return respBean;
    }

    public static ResponseData success(String message) {
        ResponseData respBean = new ResponseData();
        respBean.setCode(0);
        respBean.setMessage(message);
        return respBean;
    }

    public static ResponseData error(String message) {
        ResponseData respBean = new ResponseData();
        respBean.setCode(-1);
        respBean.setMessage(message);
        return respBean;
    }

    public static ResponseData success() {
        ResponseData respBean = new ResponseData();
        respBean.setCode(0);
        respBean.setMessage("success");
        return respBean;
    }


    public static ResponseData error() {
        ResponseData respBean = new ResponseData();
        respBean.setCode(-1);
        respBean.setMessage("error");
        return respBean;
    }


    public ResponseData data(String key, Object value) {
        if (data == null) {
            var tmp = new HashMap<>();
            tmp.put(key, value);
            data = tmp;
        }
        return this;
    }

    public ResponseData data(Object value) {
        this.data = value;
        return this;
    }

    public static ResponseData is(boolean bool) {
        return bool ? success() : error();
    }

    public static ResponseData is(boolean bool, BizCodeEnum bizCodeEnum) {
        return bool ? success() : atBizCodeEnum(bizCodeEnum);
    }

    //检测响应是否正常
    public static boolean returnIs(ResponseData responseData) {
        return responseData.getCode() != 0;
    }
}