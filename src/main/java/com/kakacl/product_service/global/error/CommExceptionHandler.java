package com.kakacl.product_service.global.error;

import com.kakacl.product_service.utils.Resp;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 全局异常
 * @date 2019-01-10
 */
@ControllerAdvice
public class CommExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Resp handle(Exception e) {
        return Resp.fail();
    }
}

