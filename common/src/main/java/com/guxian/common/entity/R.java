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
public class R {
    private Integer code;
    private String message;
    private Map<String, Object> data = new HashMap<>();

    public static R atBizCodeEnum(BizCodeEnum bizCodeEnum) {
        R respBean = new R();
        respBean.setCode(bizCodeEnum.getCode())
                .setMessage(bizCodeEnum.getMsg());
        return respBean;
    }

    public static R success(Integer code, String message) {
        R respBean = new R();
        respBean.setCode(code);
        respBean.setMessage(message);
        return respBean;
    }

    public static R error(Integer code, String message) {
        R respBean = new R();
        respBean.setCode(code);
        respBean.setMessage(message);
        return respBean;
    }

    public static R success(String message) {
        R respBean = new R();
        respBean.setCode(0);
        respBean.setMessage(message);
        return respBean;
    }

    public static R error(String message) {
        R respBean = new R();
        respBean.setCode(-1);
        respBean.setMessage(message);
        return respBean;
    }

    public static R success() {
        R respBean = new R();
        respBean.setCode(0);
        respBean.setMessage("success");
        return respBean;
    }

    public static R error() {
        R respBean = new R();
        respBean.setCode(-1);
        respBean.setMessage("error");
        return respBean;
    }

    public R data(String key, Object value) {
        this.data.put(key, value);
        return this;
    }
}