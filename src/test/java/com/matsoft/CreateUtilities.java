package com.matsoft;

import com.matsoft.crawler.impl.WebCrawlerImpl;
import com.matsoft.web.impl.SimpleWebURL;
import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

public class CreateUtilities {

    private static final int DEFAULT_DEPTH = 2;

    public static WebCrawlerImpl createWebCrawler(String url, List<String> terms) {
        try {
        return new WebCrawlerImpl(new SimpleWebURL(url, DEFAULT_DEPTH), terms,
                new ArrayBlockingQueue<>(1000), new HashSet<>(), new CSVWriter(new FileWriter("test_output.csv")));
        } catch (IOException e) {
            return null;
        }
    }
}
