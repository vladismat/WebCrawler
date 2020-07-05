package com.matsoft.crawler.impl;

import com.matsoft.crawler.WebCrawler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.BlockingQueue;

public class WebCrawlerImpl implements WebCrawler {
    private String url;
    private List<String> foundURLs;
    private List<String> terms;
    private BlockingQueue<String> urlsToVisit;
    private Set<String> visitedURLs;

    public WebCrawlerImpl(String url, final List<String> terms, BlockingQueue<String> urlsToVisit, final Set<String> visitedURLs) {
        this.url = url;
        this.foundURLs = new ArrayList<>();
        this.terms = terms;
        this.urlsToVisit = urlsToVisit;
        this.visitedURLs = visitedURLs;
    }

    public void run() {
        if (url != null) {
            processPage(url);
        }
    }

    private void processPage(String url) {
        Document document;
        try {
            document = Jsoup.connect(url).get();
            searchForLinks(document);
            for (String newUrl: foundURLs) {
                urlsToVisit.offer(newUrl);
            }
            Element body = document.body();
            Map<String, Integer> searchResult = searchForTerms(body, terms);
            System.out.println("On page " + url + "were found: ");
            for (String term : terms) {
                System.out.println(term + " " + searchResult.get(term));
            }
        } catch (IllegalArgumentException e){
            System.out.println(url + " is not valid");
        } catch (IOException e) {
            System.out.println("Oh no, couldn't get page from "+ url );
        }
    }

    private void searchForLinks(Document document) {
        Elements links = document.select("a[href]");
        for (Element link : links) {
            String url = link.attr("abs:href");
            if (visitedURLs.add(url))
                foundURLs.add(url);
        }
    }

    private Map<String, Integer> searchForTerms(Element body, List<String> terms) {
        HashMap<String, Integer> result = new HashMap<>();
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
}
