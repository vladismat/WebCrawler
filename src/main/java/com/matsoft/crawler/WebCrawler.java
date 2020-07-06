package com.matsoft.crawler;

import com.matsoft.web.WebURL;

import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

public interface WebCrawler extends Runnable {

    WebURL getUrl();

    List<String> getTerms();

    BlockingQueue<WebURL> getUrlsToVisit();

    Set<String> getVisitedURLs();

}
