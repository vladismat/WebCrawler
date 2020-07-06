package com.matsoft.web.impl;

import com.matsoft.exception.UrlNotValidException;
import com.matsoft.web.WebURL;
import org.apache.commons.validator.routines.UrlValidator;

public class SimpleWebURL implements WebURL {

    private String url;
    private int depth;

    public SimpleWebURL(String url, int depth) throws UrlNotValidException {
        if (url == null) {
            throw new UrlNotValidException();
        }
        if (!isValidURL(url)) {
            throw new UrlNotValidException(url);
        }
        if (depth < 0) {
            throw new IllegalArgumentException("Depth cannot be negative");
        }
        this.url = url;
        this.depth = depth;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public int getDepth() {
        return depth;
    }

    private boolean isValidURL(String url) {
        String[] schemes = {"http", "https"};
        UrlValidator urlValidator = new UrlValidator(schemes);
        return urlValidator.isValid(url);
    }
}
