package com.guxian.common.entity;

import com.guxian.common.exception.BizCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

/**
 * 公共返回对象
 *
 * @author GuXianWN
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class RespondseData {
    private Integer code;
    private String message;
    private Map<String, Object> data = new HashMap<>();

    public static RespondseData atBizCodeEnum(BizCodeEnum bizCodeEnum) {
        RespondseData respBean = new RespondseData();
        respBean.setCode(bizCodeEnum.getCode())
                .setMessage(bizCodeEnum.getMsg());
        return respBean;
    }

    public static RespondseData success(Integer code, String message) {
        RespondseData respData = new RespondseData();
        respData.setCode(code);
        respData.setMessage(message);
        return respData;
    }

    public static RespondseData error(Integer code, String message) {
        RespondseData respBean = new RespondseData();
        respBean.setCode(code);
        respBean.setMessage(message);
        return respBean;
    }

    public static RespondseData success(String message) {
        RespondseData respBean = new RespondseData();
        respBean.setCode(0);
        respBean.setMessage(message);
        return respBean;
    }

    public static RespondseData error(String message) {
        RespondseData respBean = new RespondseData();
        respBean.setCode(-1);
        respBean.setMessage(message);
        return respBean;
    }

    public static RespondseData success() {
        RespondseData respBean = new RespondseData();
        respBean.setCode(0);
        respBean.setMessage("success");
        return respBean;
    }

    public static RespondseData error() {
        RespondseData respBean = new RespondseData();
        respBean.setCode(-1);
        respBean.setMessage("error");
        return respBean;
    }

    public RespondseData data(String key, Object value) {
        this.data.put(key, value);
        return this;
    }
}