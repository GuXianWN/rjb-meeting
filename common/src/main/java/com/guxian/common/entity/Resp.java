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
public class Resp {
    private Integer code;
    private String message;
    private Map<String, Object> data = new HashMap<>();

    public static Resp atBizCodeEnum(BizCodeEnum bizCodeEnum) {
        Resp respBean = new Resp();
        respBean.setCode(bizCodeEnum.getCode())
                .setMessage(bizCodeEnum.getMsg());
        return respBean;
    }

    public static Resp success(Integer code, String message) {
        Resp respBean = new Resp();
        respBean.setCode(code);
        respBean.setMessage(message);
        return respBean;
    }

    public static Resp error(Integer code, String message) {
        Resp respBean = new Resp();
        respBean.setCode(code);
        respBean.setMessage(message);
        return respBean;
    }

    public static Resp success(String message) {
        Resp respBean = new Resp();
        respBean.setCode(0);
        respBean.setMessage(message);
        return respBean;
    }

    public static Resp error(String message) {
        Resp respBean = new Resp();
        respBean.setCode(-1);
        respBean.setMessage(message);
        return respBean;
    }

    public static Resp success() {
        Resp respBean = new Resp();
        respBean.setCode(0);
        respBean.setMessage("success");
        return respBean;
    }

    public static Resp error() {
        Resp respBean = new Resp();
        respBean.setCode(-1);
        respBean.setMessage("error");
        return respBean;
    }

    public Resp data(String key, Object value) {
        this.data.put(key, value);
        return this;
    }
}