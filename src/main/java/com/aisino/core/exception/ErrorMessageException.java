package com.aisino.core.exception;

/**
 * Created by 为 on 2017-6-20.
 * 自定义错误信息异常
 */
public class ErrorMessageException extends Exception {
    public ErrorMessageException() {
        super();
    }

    public ErrorMessageException(String message, Throwable cause) {
        super(message, cause);
    }

    public ErrorMessageException(String message) {
        super(message);
    }

    public ErrorMessageException(Throwable cause) {
        super(cause);
    }
}
