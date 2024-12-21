package com.haole.bupthotelbackend.common;

import lombok.Data;


@Data
public class R<T>{
    private int code;

    private T data;

    private String message;

    public static <T> R<T> ok(String message,T data){
        R<T> r = new R<>();
        r.code=200;
        r.data=data;
        r.message=message;
        return r;
    }

    public static <T> R<T> ok(T data){
        R<T> r = new R<>();
        r.code=200;
        r.data=data;
        return r;
    }

    public static  <T> R<T> fail(error errorCode){
        R<T> r = new R<>();
        r.code=errorCode.getCode();
        r.message=errorCode.getMessage();
        return r;
    }

    public static <T> R<T> fail(String message){
        R<T> r = new R<>();
        r.message=message;
        return r;
    }

}
