package com.matsoft.crawler.impl;

import com.matsoft.crawler.WebCrawler;
import com.matsoft.exception.UrlNotValidException;
import com.matsoft.web.WebURL;
import com.matsoft.web.impl.SimpleWebURL;
import com.opencsv.CSVWriter;
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

/**
 * An implementation of WebCrawler.
 */
public class WebCrawlerImpl implements WebCrawler {

    private final WebURL url;
    private final List<String> terms;
    private final BlockingQueue<WebURL> urlsToVisit;
    private final Set<String> visitedURLs;
    private final CSVWriter fileWriter;
    private static final Logger LOGGER = Logger.getLogger(WebCrawlerImpl.class.getName());

    /**
     * Constructor of the WebCrawler.
     *
     * @param url         URL of the page to process. WebURL interface to know the depth.
     * @param terms       list of terms to search
     * @param urlsToVisit Blocking Queue of URLs passed by the controller to put found links in
     * @param visitedURLs Set of already visited urls to check found URLs
     * @param fileWriter  File Writer for output to file
     */
    public WebCrawlerImpl(WebURL url, final List<String> terms, BlockingQueue<WebURL> urlsToVisit,
                          final Set<String> visitedURLs, CSVWriter fileWriter) {
        this.url = url;
        this.terms = terms;
        this.urlsToVisit = urlsToVisit;
        this.visitedURLs = visitedURLs;
        this.fileWriter = fileWriter;
    }

    /**
     * Processes a single page set by the constructor if the url isn't too deep.
     * To process a web page the method uses JSOUP library methods.
     *
     * @see org.jsoup
     */
    @Override
    public void run() {
        if (url != null && url.getDepth() < 8) {
            processPage(url);
        }
    }

    @Override
    public WebURL getUrl() {
        return url;
    }

    @Override
    public List<String> getTerms() {
        return terms;
    }

    @Override
    public BlockingQueue<WebURL> getUrlsToVisit() {
        return urlsToVisit;
    }

    @Override
    public Set<String> getVisitedURLs() {
        return visitedURLs;
    }

    public CSVWriter getFileWriter() {
        return fileWriter;
    }

    private void processPage(WebURL url) {
        Document document;
        try {
            document = Jsoup.connect(url.getUrl()).get();
            List<String> foundURLs = searchForLinks(document);
            for (String newUrl : foundURLs) {
                try {
                    urlsToVisit.offer(new SimpleWebURL(newUrl, url.getDepth() + 1));
                } catch (UrlNotValidException e) {
                    LOGGER.log(Level.INFO, "URL couldn't pass a check ", e);
                }
            }
            Element body = document.body();

            if (body == null)
                return;

            Map<String, Integer> searchResult = searchForTerms(body, terms);
            String[] resultToCSV = new String[terms.size() + 2];
            resultToCSV[0] = url.getUrl();
            int i = 1;
            int totalHits = 0;
            for (String term : terms) {
                resultToCSV[i] = searchResult.get(term).toString();
                totalHits += searchResult.get(term);
                i++;
            }
            resultToCSV[i] = Integer.toString(totalHits);
            fileWriter.writeNext(resultToCSV);

        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.WARNING, "url " + url + " is not valid", e);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Couldn't get page from " + url.getUrl());
        }
    }

    private List<String> searchForLinks(Document document) {
        List<String> foundURLs = new ArrayList<>();
        Elements links = document.select("a[href]");
        for (Element link : links) {
            String url = link.attr("abs:href");
            if (!visitedURLs.contains(url) && !url.contains("#")) {
                foundURLs.add(url);
            }
        }
        return foundURLs;
    }

    private Map<String, Integer> searchForTerms(Element body, List<String> terms) {
        Map<String, Integer> result = new HashMap<>();
        String bodyText = body.text();
        for (String t : terms) {
            result.put(t, countSubstringOccurrences(t, bodyText));
        }
        return result;
    }

    private Integer countSubstringOccurrences(String term, String source) {
        Integer count = 0;
        int fromIndex = 0;
        while ((fromIndex = source.indexOf(term, fromIndex)) != -1) {
            count++;
            fromIndex++;
        }
        return count;
    }
}
