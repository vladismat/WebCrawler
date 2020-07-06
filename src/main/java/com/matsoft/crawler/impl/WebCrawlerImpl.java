package com.matsoft.crawler.impl;

import com.matsoft.crawler.WebCrawler;
import com.matsoft.exception.UrlNotValidException;
import com.matsoft.web.WebURL;
import com.matsoft.web.impl.SimpleWebURL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WebCrawlerImpl implements WebCrawler {
    private WebURL url;
    private List<String> foundURLs;
    private List<String> terms;
    private BlockingQueue<WebURL> urlsToVisit;
    private Set<String> visitedURLs;
    private FileWriter fileWriter;
    static private Logger LOGGER = Logger.getLogger(WebCrawlerImpl.class.getName());

    public WebCrawlerImpl(WebURL url, final List<String> terms, BlockingQueue<WebURL> urlsToVisit,
                          final Set<String> visitedURLs, FileWriter fileWriter) {
        this.url = url;
        this.foundURLs = new ArrayList<>();
        this.terms = terms;
        this.urlsToVisit = urlsToVisit;
        this.visitedURLs = visitedURLs;
        this.fileWriter = fileWriter;
    }

    public void run() {
        if (url != null && !url.getUrl().equals("") && url.getDepth() < 8) {
            processPage(url);
        }
    }

    private void processPage(WebURL url) {
        Document document;
        try {
            document = Jsoup.connect(url.getUrl()).get();
            searchForLinks(document);
            for (String newUrl : foundURLs) {
                WebURL offeringURL;
                try {
                    urlsToVisit.offer(new SimpleWebURL(newUrl, url.getDepth() + 1));
                } catch (UrlNotValidException e) {
                    LOGGER.log(Level.INFO, "URL couldn't pass a check ", e);
                }
            }
            Element body = document.body();
            Map<String, Integer> searchResult = searchForTerms(body, terms);
            printToFile("On page " + url.getUrl() + ", that had depth of the search  " + url.getDepth() + ", were found: \n");
            int totalHits = 0;
            for (String term : terms) {
                printToFile(term + " " + searchResult.get(term) + "\n");
                totalHits += searchResult.get(term);
            }
            printToFile("Total hits on page: " + totalHits + "\n \n");
            visitedURLs.add(url.getUrl());
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.WARNING, "url " + url + " is not valid", e);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Couldn't get page from " + url);
        }
    }

    private void searchForLinks(Document document) {
        Elements links = document.select("a[href]");
        for (Element link : links) {
            String url = link.attr("abs:href");
            if (!visitedURLs.contains(url))
                foundURLs.add(url);
        }
    }

    private Map<String, Integer> searchForTerms(Element body, List<String> terms) {
        Map<String, Integer> result = new HashMap<>();
        String bodyText = body.text();
        for (String t : terms) {
            result.put(t, countSubstringOccurences(t, bodyText));
        }
        return result;
    }

    private Integer countSubstringOccurences(String term, String source) {
        Integer count = 0;
        int fromIndex = 0;
        while ((fromIndex = source.indexOf(term, fromIndex)) != -1) {
            count++;
            fromIndex++;
        }
        return count;
    }

    private void printToFile(String s) {
        try {
            fileWriter.write(s);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Couldn't write the result", e);
        }
    }
}
