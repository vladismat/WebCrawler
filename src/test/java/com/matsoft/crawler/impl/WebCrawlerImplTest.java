package com.matsoft.crawler.impl;

import com.matsoft.CreateUtilities;
import com.matsoft.LoggerInitializer;
import com.opencsv.CSVReader;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import static org.junit.Assert.*;

public class WebCrawlerImplTest {

    private static final String TEST_URL = "https://en.wikipedia.org/wiki/Septimius_Severus";
    private static final List<String> TEST_TERMS = Arrays.asList("Septimius Severus", "Rome", "the", "Ivan");
    private WebCrawlerImpl webCrawler;

    @BeforeClass
    public static void initEnvironment() {
        LoggerInitializer.initLogger();
    }

    @Before
    public void setUp() {
        webCrawler = CreateUtilities.createWebCrawler(TEST_URL, TEST_TERMS);
    }

    @After
    public void tearDown() {
        webCrawler = null;
    }

    @Test
    public void run() throws Exception {
        webCrawler.run();
        webCrawler.getFileWriter().close();
        CSVReader reader = new CSVReader(new FileReader(new File("test_output.csv")));
        String[] result = reader.readNext();
        String[] expected = {TEST_URL, "57", "28", "426", "0", "511"};
        assertArrayEquals(expected, result);
        reader.close();
    }

}