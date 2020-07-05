package com.matsoft;

import com.matsoft.crawler.CrawlerController;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        String seed = "";
        List<String> terms = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new File("input.txt"));
            seed = scanner.nextLine();
            while (scanner.hasNext()) {
                terms.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println("File wasn't found");
        }
        CrawlerController crawlerController = new CrawlerController(seed, terms);
        crawlerController.start();
    }
}
