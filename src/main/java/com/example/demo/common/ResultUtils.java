package com.example.demo.common;

import com.example.demo.common.BaseResponse;

/**
 * @program: demo
 * @description:返回工具类
 * @author: zjx
 * @create: 2022-04-26 23:16
 **/
public class ResultUtils {
    /**
     * 返回成功
     * @param data
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> success(T data){
        return new BaseResponse<T>(0,data,"ok","");
    }


    /**
     * 返回失败值
     * @param errorCode
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode){
        return new BaseResponse<>(errorCode);
    }

    public static BaseResponse error(ErrorCode errorCode,String message,String description){
        return new BaseResponse(errorCode,message,description);
    }

    public static BaseResponse error(int code,String message,String description){
        return new BaseResponse<>(code,null,message,description);
    }
}
