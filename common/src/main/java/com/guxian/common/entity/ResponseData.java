package com.guxian.common.entity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.guxian.common.exception.BizCodeEnum;
import com.guxian.common.exception.ServiceException;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Objects;

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

    private static final String DEFAULT_SUCCESS_MESSAGE = "success";
    private static final String DEFAULT_ERROR_MESSAGE = "error";
    private static final Integer DEFAULT_ERROR_CODE = -1;
    private static final Integer DEFAULT_SUCCESS_CODE = 0;

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

    /*************************
     *        success
     ************************/
    public static ResponseData success(Integer code, String message) {
        ResponseData respData = new ResponseData();
        respData.setCode(code);
        respData.setMessage(message);
        return respData;
    }

    public static ResponseData success(Object data) {
        ResponseData respBean = new ResponseData();
        respBean.setCode(DEFAULT_SUCCESS_CODE);
        respBean.setMessage(DEFAULT_SUCCESS_MESSAGE);
        respBean.setData(data);
        return respBean;
    }

    public static ResponseData success(String key, Object data) {
        ResponseData respBean = new ResponseData();
        respBean.setCode(DEFAULT_SUCCESS_CODE);
        respBean.setMessage(DEFAULT_SUCCESS_MESSAGE);
        var tmp = new HashMap<>();
        tmp.put(key, data);
        respBean.setData(tmp);
        return respBean;
    }

    public static ResponseData success(String message) {
        ResponseData respBean = new ResponseData();
        respBean.setCode(DEFAULT_SUCCESS_CODE);
        respBean.setMessage(message);
        return respBean;
    }


    public static ResponseData success() {
        ResponseData respBean = new ResponseData();
        respBean.setCode(DEFAULT_SUCCESS_CODE);
        respBean.setMessage(DEFAULT_SUCCESS_MESSAGE);
        return respBean;
    }

    public static ResponseData parser(String json) {
        var parse = JSONObject.parseObject(json,ResponseData.class);
        return ResponseData.returnHas(returnHas(parse));
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


    /*************************
     *         error
     ************************/

    public static ResponseData error(BizCodeEnum bizCodeEnum) {
        return error().setMessage(bizCodeEnum.getMsg()).setCode(bizCodeEnum.getCode());
    }


    public static ResponseData error(Integer code, String message) {
        ResponseData respBean = new ResponseData();
        respBean.setCode(code);
        respBean.setMessage(message);
        return respBean;
    }


    public static ResponseData error(String message) {
        ResponseData respBean = new ResponseData();
        respBean.setCode(DEFAULT_ERROR_CODE);
        respBean.setMessage(message);
        return respBean;
    }


    public static ResponseData error() {
        ResponseData respBean = new ResponseData();
        respBean.setCode(DEFAULT_ERROR_CODE);
        respBean.setMessage(DEFAULT_ERROR_MESSAGE);
        return respBean;
    }


    /************
     * judge
     ************/
    public static ResponseData is(boolean bool) {
        return bool ? success() : error();
    }

    public static ResponseData is(boolean bool, BizCodeEnum bizCodeEnum) {
        return bool ? success() : atBizCodeEnum(bizCodeEnum);
    }

    public static <T> ResponseData is(boolean bool, BizCodeEnum bizCodeEnum, T object) {
        return bool ? success().data(object) : atBizCodeEnum(bizCodeEnum);
    }


    //检测响应是否正常
    public static boolean returnIs(ResponseData responseData) {
        if (!Objects.equals(DEFAULT_SUCCESS_CODE, responseData.getCode())) {
            throw new ServiceException(responseData.message, responseData.code);
        }
        return true;
    }

    //检测响应是否正常
    public static ResponseData returnHas(ResponseData responseData) {
        if (!Objects.equals(DEFAULT_SUCCESS_CODE, responseData.getCode())) {
            throw new ServiceException(responseData.message, responseData.code);
        }
        return responseData;
    }

}