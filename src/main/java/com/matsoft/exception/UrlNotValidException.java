package com.matsoft.exception;

import com.matsoft.web.WebURL;

public class UrlNotValidException extends RuntimeException {

    private WebURL notValidURL;
    private static final String DEFAULT_MESSAGE = "URL is not valid";

    public UrlNotValidException() {
        super(DEFAULT_MESSAGE);
    }

    public UrlNotValidException(String url) {
        super((url != null) ? url + " this " + DEFAULT_MESSAGE : DEFAULT_MESSAGE);
    }
}
