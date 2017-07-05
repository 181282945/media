package com.aisino.core.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Created by 为 on 2017-4-24.
 * 自定义验证码异常
 */
public class VerificationException extends AuthenticationException {
	
	private static final long serialVersionUID = 355680329604361176L;

	public VerificationException(String msg) {
        super(msg);
    }

    public VerificationException(String msg, Throwable t) {
        super(msg, t);
    }
}
