package com.xiaao.miaosha.error;

/**
 * @author: Xia-ao
 * @create: 2019-01-24 22:25
 **/
public enum EmBusinessErr implements CommonError {

    //通用错误类型10001
    PARAMETER_VALIDATION_ERROR(10001, "参数不合法"),
    UNKONW_ERROR(10002, "未知错误"),

    // 20000开头用户信息错误
    USER_NOT_EXIST(20001, "用户不存在"),
    USER_LOGIN_FAIL(20002, "用户手机号或者密码错误"),
    USER_NOT_LOGIN(20003, "用户未登录"),


    // 30000开头，交易相关错误
    STOCK_NOT_ENOUGH(30001, "库存不足"),;


    private int errCode;

    private String errMsg;

    private EmBusinessErr(int errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    @Override
    public int getErrCode() {
        return this.errCode;
    }

    @Override
    public String getErrMsg() {
        return this.errMsg;
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.errMsg = errMsg;
        return this;
    }
}
