package com.xiaao.miaosha.controller;
/**
 * @author: Xia-ao
 * @create: 2019-01-24 23:08
 **/

import com.xiaao.miaosha.error.BusinessException;
import com.xiaao.miaosha.error.EmBusinessErr;
import com.xiaao.miaosha.response.CommonReturnType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @className: BaseController
 * @description: NULL
 * @author: Xia-ao
 * @create: 2019-01-24 23:08
 **/
public class BaseController {

    public static final String CONTENT_TYPE_FORMED= "application/x-www-form-urlencoded";

    // 定义ExceptionHandler处理未被controller吸收的Exception异常
    @ExceptionHandler(Exception.class)

    //一些异常是因为请求逻辑导致，而非服务器本身内部处理异常，这时服务器端是接受了请求，而在返回时发生异常，这时服务器接受请求
    //的状态是成功的，此时再处理请求逻辑异常,将会进入这个方法处理。需要ResponseStatus注解
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object handlerException(HttpServletRequest request, Exception ex){
        Map<String , Object> responseData= new HashMap<>();
        if(ex instanceof BusinessException){
            BusinessException businessException= (BusinessException) ex;
            responseData.put("errCode",businessException.getErrCode());
            responseData.put("errMsg", businessException.getErrMsg());

        }else {
            responseData.put("errCode", EmBusinessErr.UNKONW_ERROR.getErrCode());
            responseData.put("errMsg", ex.getMessage());
        }
        return  CommonReturnType.create(responseData,"fail");

    }
}
