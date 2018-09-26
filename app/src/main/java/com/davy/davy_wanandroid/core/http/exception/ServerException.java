package com.davy.davy_wanandroid.core.http.exception;

/**
 * author: Davy
 * date: 2018/9/26
 */
public class ServerException extends Exception {
    private int code;

    public ServerException(String message) {
        super(message);
    }

    public ServerException(String message, int code) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}

