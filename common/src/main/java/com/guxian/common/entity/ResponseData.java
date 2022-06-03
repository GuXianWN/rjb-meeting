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
@Setter
@Accessors(chain = true)
public class ResponseData {

    private  ResponseData() {}

    private Integer code;
    private String message;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, Object> data = null;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("data")
    private Object useInObject = null;


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
            data = new HashMap<>();
        }

        data.put(key, value);
        return this;
    }

    public ResponseData data(Object value) {
        this.useInObject = value;
        return this;
    }
}