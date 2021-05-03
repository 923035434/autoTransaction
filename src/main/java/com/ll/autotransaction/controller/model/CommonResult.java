package com.ll.autotransaction.controller.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

/**
 * API调用结果对象
 */
@Getter
@Setter
@NoArgsConstructor
public class CommonResult<T> {
    // 错误码
    private HttpStatus errorCode;

    // 错误消息
    private String errorMessage;

    // 返回结果
    private T data;

    public CommonResult(T data) {
        this.errorCode = HttpStatus.OK;
        this.data = data;
    }


    public CommonResult(T data,HttpStatus errorCode) {
        this.errorCode = errorCode;
        this.data = data;
    }

    public CommonResult(HttpStatus errorCode,String msg) {
        this.errorCode = errorCode;
        this.errorMessage = msg;
    }


    /**
     * API调用是否成功
     *
     * @return 是否成功
     */
    public boolean isSuccess() {
        return HttpStatus.OK.equals(errorCode);
    }

    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<>(data);
    }


    public static <T> CommonResult<T> error(String msg) {
        return new CommonResult<>(HttpStatus.BAD_REQUEST,msg);
    }


    public static <T> CommonResult<T> error(HttpStatus errorCode,String msg) {
        return new CommonResult<>(errorCode,msg);
    }

}
