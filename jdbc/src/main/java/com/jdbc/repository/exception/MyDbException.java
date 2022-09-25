package com.jdbc.repository.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyDbException extends RuntimeException{
    public MyDbException() {
        super();
        log.info("myDbException error");
    }

    public MyDbException(String message) {
        super(message);
        log.info("myDbException error");
    }

    public MyDbException(String message, Throwable cause) {
        super(message, cause);
        log.info("myDbException error");
    }

    public MyDbException(Throwable cause) {
        super(cause);
        log.info("myDbException error");
    }

    public MyDbException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        log.info("myDbException error");
    }
}
