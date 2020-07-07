package com.matsoft.web.impl;

import com.matsoft.exception.UrlNotValidException;
import com.matsoft.web.WebURL;
import org.apache.commons.validator.routines.UrlValidator;

/**
 * An implementation class of WebURL interface. Can be created only if the URL is valid.
 * Validation is performed in the constructor. To validate URL the class uses ready-made validator.
 * @see org.apache.commons.validator.routines.UrlValidator
 */
public class SimpleWebURL implements WebURL {

    private String url;
    private int depth;

    /**
     * The only constructor with two arguments
     *
     * @param url string value of URL
     * @param depth depth to assign for the object
     * @throws UrlNotValidException on invalid URL passed
     * @see UrlNotValidException
     */
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
