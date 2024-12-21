package com.haole.bupthotelbackend.common;

public enum error {

    OK(0,"请求成功"),
    PARAMS_ERROR(40000, "请求参数错误"),
    NULL_ERROR(40001, "请求数据为空"),
    NOT_LOGIN(40100, "未登录"),
    NO_AUTH(40101, "无权限"),
    FORBIDDEN(40301, "禁止操作"),
    SYSTEM_ERROR(50000, "系统内部异常");

    public int code;

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String message;

    error(int code, String message){
        this.code=code;
        this.message=message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
