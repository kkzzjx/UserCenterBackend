package com.example.demo.exception;

import com.example.demo.common.BaseResponse;
import com.example.demo.common.ErrorCode;
import com.example.demo.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**利用SpringAOP 切面，在调用方法前后进行额外的处理
 * @program: demo
 * @description: 全局异常处理器 用于集中捕获异常
 * @author: zjx
 * @create: 2022-05-01 22:35
 **/
@RestControllerAdvice
@Slf4j  //打个日志
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)  //需要继承异常类 意思就是说这个方法只需要捕获BusinessException这个异常
    //利用注解 定义针对什么异常做什么处理
    public BaseResponse businessExceptionHandler(BusinessException e){ //捕获多个异常，参数为Exception
        log.error("businessException"+e.getMessage(),e);
        return ResultUtils.error(e.getCode(),e.getMessage(),e.getDescription());

    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse runtimeExceptionHandler(RuntimeException e){
        //对于系统内部异常
        log.error("runtimeError",e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR,e.getMessage(),"");

    }


}
