package com.matsoft.web.impl;

import com.matsoft.LoggerInitializer;
import com.matsoft.exception.UrlNotValidException;
import com.matsoft.web.WebURL;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleWebURLTest {

    private static String DEFAULT_URL = "https://en.wikipedia.org/wiki/Brown_bear";
    private static int DEFAULT_DEPTH = 2;
    private WebURL defaultWebURL;

    @BeforeClass
    public static void initEnvironment() {
        LoggerInitializer.initLogger();
    }

    @Before
    public void setUp() {
        defaultWebURL = new SimpleWebURL(DEFAULT_URL, DEFAULT_DEPTH);
    }

    @Test(expected = UrlNotValidException.class)
    public void constructorNullUrl() {
        WebURL url = new SimpleWebURL(null, 0);

        assertNull(url.getUrl());
        assertEquals(0, url.getDepth());

    }

    @Test(expected = UrlNotValidException.class)
    public void constructorNotValidUrl() {
        WebURL url = new SimpleWebURL("The Matrix has you, Neo", 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorNegativeDepth() {
        WebURL url = new SimpleWebURL(DEFAULT_URL, -5);
    }

    @Test
    public void getUrl() {
        assertEquals(DEFAULT_URL, defaultWebURL.getUrl());
    }

    @Test
    public void getDepth() {
        assertEquals(DEFAULT_DEPTH, defaultWebURL.getDepth());
    }
}