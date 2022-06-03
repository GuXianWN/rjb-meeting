package com.guxian.common.exception;

import com.guxian.common.entity.RespondseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class ExceptionHandle {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public RespondseData handleValidException(MethodArgumentNotValidException e) {
        log.error("数据校验出现错误: {},类型: {}", e.getMessage(), e.getClass());
        BindingResult bindingResult = e.getBindingResult();
        Map<String, String> errorMap = bindingResult.getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, DefaultMessageSourceResolvable::getDefaultMessage));
        return RespondseData.error(BizCodeEnum.VALID_EXCEPTION.getMsg())
                .setCode(BizCodeEnum.VALID_EXCEPTION.getCode())
                .data("data", errorMap);
    }

    @ExceptionHandler(ServiceException.class)
    public RespondseData handleValidException(ServiceException e) {
        log.error("业务异常: {},类型: {}", e.getMessage(), e.getClass());
        return RespondseData.error(e.getMessage()).setCode(e.getStatus());
    }
}
