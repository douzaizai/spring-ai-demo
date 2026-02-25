package com.example.response;

import com.example.enums.ReturnCodeEnum;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Setter
@Getter
public class Response<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = -5316597326293972581L;
    /**
     * 返回状态码：返回200表示请求接口成功，非200表示请求接口失败
     */
    private int code;
    /**
     * 返回消息
     */
    private String msg;

    /**
     * 返回数据
     */
    private T data;

    public static <T> Response<T> success() {
        Response<T> response = new Response<>();
        response.code = ReturnCodeEnum.SUCCESS.getCode();
        response.msg = ReturnCodeEnum.SUCCESS.getMsg();
        return response;
    }

    public static <T> Response<T> success(T data) {
        Response<T> response = new Response<>();
        response.code = ReturnCodeEnum.SUCCESS.getCode();
        response.msg = ReturnCodeEnum.SUCCESS.getMsg();
        response.data = data;
        return response;
    }

    public static <T> Response<T> error() {
        Response<T> response = new Response<>();
        response.code = ReturnCodeEnum.ERROR.getCode();
        response.msg = ReturnCodeEnum.ERROR.getMsg();
        return response;
    }

    public static <T> Response<T> result(boolean b) {
        return b ? Response.success() : Response.error();
    }

    public static <T> Response<T> result(boolean b, ReturnCodeEnum returnCodeEnum) {
        return b ? Response.success() : Response.error(returnCodeEnum);
    }


    public static <T> Response<T> error(ReturnCodeEnum returnCodeEnum) {
        Response<T> response = new Response<>();
        response.code = returnCodeEnum.getCode();
        response.msg = returnCodeEnum.getMsg();
        return response;
    }

    public static <T> Response<T> error(int code, String msg) {
        Response<T> response = new Response<>();
        response.code = code;
        response.msg = msg;
        return response;
    }

}
