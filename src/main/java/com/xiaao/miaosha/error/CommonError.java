package com.xiaao.miaosha.error;
/**
 * @author: Xia-ao
 * @create: 2019-01-24 22:22
 **/

/**
 * @Interface: CommonError
 * @description: 通用错误接口
 * @author: Xia-ao
 * @create: 2019-01-24 22:22
 **/
public interface CommonError {

    public int getErrCode();

    public String getErrMsg();

    public CommonError setErrMsg(String errMsg);
}
