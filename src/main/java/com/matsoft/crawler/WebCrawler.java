package com.matsoft.crawler;

import com.matsoft.web.WebURL;

import java.io.FileWriter;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

/**
 * Web Crawler interface. <br>
 * The main reason of it is supportability and division principle. The interface extends Runnable interface
 * because the implementation uses different Web Crawlers to search different websites in parallel threads.
 */
public interface WebCrawler extends Runnable {

    WebURL getUrl();

    List<String> getTerms();

    BlockingQueue<WebURL> getUrlsToVisit();

    Set<String> getVisitedURLs();

    FileWriter getFileWriter();

}
