package com.matsoft.exception;


/**
 * Custom exception class that is thrown in case that found URL isn't valid.
 */
public class UrlNotValidException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "URL is not valid";

    public UrlNotValidException() {
        super(DEFAULT_MESSAGE);
    }

    public UrlNotValidException(String url) {
        super((url != null) ? url + " this " + DEFAULT_MESSAGE : DEFAULT_MESSAGE);
    }
}
