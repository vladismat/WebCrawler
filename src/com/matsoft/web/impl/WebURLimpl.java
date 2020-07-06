package com.matsoft.web.impl;

import com.matsoft.web.WebURL;

public class WebURLimpl implements WebURL {

    private String url;
    private int depth;

    public WebURLimpl(String url, int depth) {
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
}
