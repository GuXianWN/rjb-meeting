package com.guxian.common.exception;

import com.guxian.common.entity.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class ExceptionHandle {

    @ExceptionHandler(org.springframework.web.HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ResponseData> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("请求方式出现问题: {},类型: {}", e.getMessage(), e.getClass());
        return ResponseEntity.ok().body(ResponseData.error(BizCodeEnum.RequestMethodNotSupported));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseData> handleValidException(MethodArgumentNotValidException e) {
        log.error("数据校验出现错误: {},类型: {}", e.getMessage(), e.getClass());
        BindingResult bindingResult = e.getBindingResult();
        Map<String, String> errorMap = bindingResult.getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, DefaultMessageSourceResolvable::getDefaultMessage));

        return ResponseEntity.ok().body(ResponseData.error(BizCodeEnum.VALID_EXCEPTION.getMsg())
                .setCode(BizCodeEnum.VALID_EXCEPTION.getCode())
                .data(errorMap));

    }


    @ExceptionHandler(org.springframework.validation.BindException.class)
    public ResponseEntity<ResponseData> bindExceptionHandle(BindException e) {
        return ResponseEntity.ok().body(
                ResponseData.error("参数缺失！"));
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ResponseData> handleValidException(ServiceException e) {
        log.error("业务异常: {},类型: {}", e.getMessage(), e.getClass());
        return ResponseEntity.ok().body(ResponseData.error(e.getMessage())
                .setCode(e.getStatus()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseData> handleFileNotFoundException(Exception e) {
        e.printStackTrace();
        return ResponseEntity.ok().body(
                ResponseData.error(BizCodeEnum.SERVER_ERROR)
        );
    }

}
