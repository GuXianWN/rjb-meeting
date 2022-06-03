package com.guxian.common.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@Getter
public class ServiceException extends RuntimeException {

    private Integer status;


    public ServiceException(String message, HttpStatus status) {
        super(message);
        this.status = status.value();
    }

    public ServiceException(BizCodeEnum bizCodeEnum) {
        super(bizCodeEnum.getMsg());
        this.status = bizCodeEnum.getCode();
    }
}