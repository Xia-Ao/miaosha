package com.xiaao.miaosha.response;
/**
 * @author: Xia-ao
 * @create: 2019-01-24 22:04
 **/

/**
 * @className: CommonReturnType
 * @description: 定义通用返回信息
 * @author: Xia-ao
 * @create: 2019-01-24 22:04
 **/
public class CommonReturnType {

    // status为返回状态 success fail
    private String status;
    private Object data;
    //若status=success，则data内返回前端需要的json数据
    //若status=fail,则data内使用通用的错误码格式


    public static CommonReturnType create(Object data){
        return CommonReturnType.create(data,"success");
    }

    public static CommonReturnType create(Object data, String status){
        CommonReturnType type = new CommonReturnType();
        type.setStatus(status);
        type.setData(data);
        return type;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
