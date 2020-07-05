package com.matsoft.crawler;

import com.matsoft.crawler.impl.WebCrawlerImpl;


import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class CrawlerController {
    private static final int LIMIT = 10000;
    private String seed;
    private List<String> terms;
    private Set<String> visitedURLs;
    private BlockingQueue<String> urlsToVisit;


    public CrawlerController(String seed, List<String> terms) {
        this.seed = seed;
        this.terms = terms;
        this.urlsToVisit = new ArrayBlockingQueue<>(1000);
        this.visitedURLs = new HashSet<>();
    }

    public void start() {
        try {
            visitedURLs.add(seed);
            Thread seedCrawler = new Thread(new WebCrawlerImpl(seed, terms, urlsToVisit, visitedURLs));
            seedCrawler.start();
            seedCrawler.join();
            int counter = 0;
            while (visitedURLs.size() < LIMIT && counter < 10000) {
                String url = urlsToVisit.take();
                Thread crawler = new Thread(new WebCrawlerImpl(url, terms, urlsToVisit, visitedURLs));
                crawler.start();
                System.out.println("active threads: " + Thread.getAllStackTraces().keySet().size());
                counter++;
            }
            System.out.println(Thread.getAllStackTraces().keySet().size());
        } catch (InterruptedException e) {
            //TODO: implement exception handler
        }
    }
}

