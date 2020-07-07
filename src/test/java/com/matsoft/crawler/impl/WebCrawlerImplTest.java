package com.matsoft.crawler.impl;

import com.matsoft.CreateUtilities;
import com.matsoft.LoggerInitializer;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
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
        Scanner scanner = new Scanner(new File("test_output.txt"));
        assertEquals("On page "+TEST_URL+", that had depth of the search  2, were found: ", scanner.nextLine());
        assertEquals("Septimius Severus 57", scanner.nextLine());
        assertEquals("Rome 28", scanner.nextLine());
        assertEquals("the 426", scanner.nextLine());
        assertEquals("Ivan 0", scanner.nextLine());
        assertEquals("Total hits on page: 511", scanner.nextLine());
        scanner.close();
    }

}