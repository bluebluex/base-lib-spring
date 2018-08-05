package com.loo.lesson.spring.service.exception;

/**
 * @program: base-core
 * @description:
 * @author: Tomax
 * @create: 2018-07-20 18:05
 **/
public class ServiceException extends Exception {

    private static final long serialVersionUID = -7420250807686764032L;

    public ServiceException() {
        super();
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

    public ServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

