package com.matsoft;

import com.matsoft.crawler.CrawlerController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        String seed = args[0];
        List<String> terms = new ArrayList<String>();

        for (int i = 1 ; i < args.length; i++) {
            terms.add(args[i]);
        }
        CrawlerController crawlerController = new CrawlerController(seed, terms);
        crawlerController.start();
    }
}
