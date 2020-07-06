package com.matsoft.crawler.impl;

import com.matsoft.LoggerInitializer;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class WebCrawlerImplTest {

    private WebCrawlerImpl webCrawler;

    @BeforeClass
    public static void initEnvironment() {
        LoggerInitializer.initLogger();
    }

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
        webCrawler = null;
    }

    @Test
    public void run() {
    }

    @Test
    public void searchForLinks() {
    }

    @Test
    public void searchForTerms() {
    }

    @Test
    public void printToFile() {
    }
}