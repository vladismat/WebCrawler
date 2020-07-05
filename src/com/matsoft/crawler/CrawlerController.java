package com.matsoft.crawler;

import com.matsoft.crawler.impl.WebCrawlerImpl;


import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

public class CrawlerController {
    private static final int LIMIT = 1000;
    private String seed;
    private List<String> terms;
    private Set<String> visitedURLs;
    private BlockingQueue<String> urlsToVisit;
    private FileWriter fileWriter = null;

    public CrawlerController(String seed, List<String> terms) {
        this.seed = seed;
        this.terms = terms;
        this.urlsToVisit = new ArrayBlockingQueue<>(1000);
        this.visitedURLs = new HashSet<>();
        try {
            this.fileWriter = new FileWriter("output.txt");
        } catch (IOException e) {

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
            System.out.println(Thread.getAllStackTraces().keySet().size());
            try {
                for (; ; ) {
                    if (executor.isTerminated()) {
                        fileWriter.close();
                        break;
                    }
                }
            } catch (IOException e) {
            }
        } catch (InterruptedException e) {
            //TODO: implement exception handler
        }
    }
}

