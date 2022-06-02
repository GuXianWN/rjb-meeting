package com.om.common;

import lombok.AllArgsConstructor;

import static javax.servlet.http.HttpServletResponse.*;

@AllArgsConstructor
public enum ResultStatus {
    /**
     * 成功
     */
    RESULT_STATUS_SUCCESS("success", SC_OK),

    /**
     * 失败,服务器内部错误
     */

    SERVER_ERROR("server error", SC_INTERNAL_SERVER_ERROR),

    /**
     * 失败,参数错误
     */
//    PARAMETER_INVAT

    ;
    String message = "";
    int code = 0;

}
