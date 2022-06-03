package com.om.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ResultData {
    private int code;
    private String message;
    private Object data;

    public static ResultData success(Object data) {
        return new ResultData(0, "success", data);
    }

    public static ResultData success() {
        return new ResultData(0, "success", null);
    }

    public static ResultData error(String message) {
        return new ResultData(1, message, null);
    }
}
