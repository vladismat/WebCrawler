package com.matsoft.crawler.impl;

import com.matsoft.crawler.WebCrawler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebCrawlerImpl implements WebCrawler {
    private String url;
    private List<String> foundURLs;
    private List<String> terms;

    public WebCrawlerImpl(String url, final List<String> terms) {
        this.url = url;
        this.foundURLs = new ArrayList<>();
        this.terms = terms;
    }

    public void run() {
        if (url != null) {
            processPage(url);
        }
    }

    public List<String> getFoundURLs () {
        return this.foundURLs;
    }

    private void processPage(String url) {
        Document document;
        try {
            document = Jsoup.connect(url).get();
            searchForLinks(document);
            Element body = document.body();
            Map<String, Integer> searchResult = searchForTerms(body, terms);
        } catch (IOException e) {
            //TODO: implement exception handler
        }
    }

    private void searchForLinks(Document document) {
        Elements links = document.select("a[href]");
        for (Element link : links) {
            foundURLs.add(link.attr("abs:href"));
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
        while ((fromIndex = source.indexOf(term, fromIndex)) != -1 ){
            count++;
            fromIndex++;
        }
        return count;
    }
}
