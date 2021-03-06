package com.matsoft.crawler;

import com.matsoft.crawler.impl.WebCrawlerImpl;
import com.matsoft.exception.UrlNotValidException;
import com.matsoft.web.WebURL;
import com.matsoft.web.impl.SimpleWebURL;
import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * CrawlerController class initiates the process of crawling, sends crawlers
 * to different websites and manages multithreading.
 */
public class CrawlerController {
    private static final int LIMIT = 1000; //Maximum number of pages to crawl. 1000 by default.
    private final String seed;
    private final List<String> terms;
    private final Set<String> visitedURLs;
    private final BlockingQueue<WebURL> urlsToVisit;
    private CSVWriter fileWriter;

    private static final Logger LOGGER = Logger.getLogger(CrawlerController.class.getName());

    /**
     * Constructor. Initiates collections and File Writer. It's impossible to s
     * tart crawling without a seed and a list of terms.
     *
     * @param seed  seed URL to start crawling
     * @param terms a list of terms to search for
     */
    public CrawlerController(String seed, List<String> terms) {
        this.seed = seed;
        this.terms = terms;
        this.urlsToVisit = new ArrayBlockingQueue<>(LIMIT);
        this.visitedURLs = new HashSet<>();
        try {
            this.fileWriter = new CSVWriter(new FileWriter("output.csv"));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "File Writer couldn't create a file", e);
        }
    }

    /**
     * A method that initiates the process of crawling.
     */
    public void start() {
        try {
            String[] head = new String[terms.size() + 2];
            head[0] = "URL";
            System.arraycopy(terms.toArray(), 0, head, 1, terms.size());
            head[terms.size() + 1] = "Total";
            fileWriter.writeNext(head);

            ExecutorService executor = Executors.newCachedThreadPool();
            visitedURLs.add(seed);

            //Seed processing
            try {
                WebURL seedURL = new SimpleWebURL(seed, 0);
                Thread seedCrawler = new Thread(
                        new WebCrawlerImpl(seedURL, terms, urlsToVisit, visitedURLs, fileWriter));
                seedCrawler.start();
                seedCrawler.join();
            } catch (UrlNotValidException e) {
                System.out.println("Seed URL " + seed + " is not valid. Please, change the input");
            }

            while (visitedURLs.size() < LIMIT) {
                WebURL url = urlsToVisit.poll();
                if (url != null) {
                    executor.execute(new WebCrawlerImpl(url, terms, urlsToVisit, visitedURLs, fileWriter));
                    visitedURLs.add(url.getUrl());
                } else {
                    Thread.sleep(1000);
                    if (urlsToVisit.isEmpty()) {
                        break;
                    }
                }
            }
            executor.shutdown();
            try {
                for (;;) {
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
