package com.matsoft.crawler;

import java.net.URL;
import java.util.HashSet;
import java.util.List;

public class CrawlerController {
    private String seed;
    private List<String> terms;
    private HashSet<String> visitedURLs;

    public CrawlerController(String seed, List<String> terms) {
        this.seed = seed;
        this.terms = terms;
    }
    public void start() {

    }
}
