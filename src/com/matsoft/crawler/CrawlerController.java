package com.matsoft.crawler;

import com.matsoft.crawler.impl.WebCrawlerImpl;


import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CrawlerController {
    private static final int LIMIT = 1000;
    private String seed;
    private List<String> terms;
    private Set<String> visitedURLs;
    private BlockingQueue<String> urlsToVisit;
    private FileWriter fileWriter = null;
    static private Logger LOGGER = Logger.getLogger(CrawlerController.class.getName());

    public CrawlerController(String seed, List<String> terms) {
        this.seed = seed;
        this.terms = terms;
        this.urlsToVisit = new ArrayBlockingQueue<>(1000);
        this.visitedURLs = new HashSet<>();
        try {
            this.fileWriter = new FileWriter("output.txt");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "File Writer couldn't create a file", e);
        }
    }

    public void start() {
        try {
            ExecutorService executor = Executors.newFixedThreadPool(5);
            visitedURLs.add(seed);
            Thread seedCrawler = new Thread(new WebCrawlerImpl(seed, terms, urlsToVisit, visitedURLs, fileWriter));
            seedCrawler.start();
            seedCrawler.join();
            int counter = 0;
            while (visitedURLs.size() < LIMIT && counter < 1000) {
                String url = urlsToVisit.take();
                executor.execute(new WebCrawlerImpl(url, terms, urlsToVisit, visitedURLs, fileWriter));
                counter++;
            }
            executor.shutdown();
            try {
                for (; ; ) {
                    if (executor.isTerminated()) {
                        fileWriter.close();
                        break;
                    }
                }
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "File Writer couldn't close", e);
            }
        } catch (InterruptedException e) {
            LOGGER.log(Level.SEVERE, "The thread were somehow interrupted", e);
        }
    }
}

