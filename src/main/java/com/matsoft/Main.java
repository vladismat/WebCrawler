package com.matsoft;

import com.matsoft.crawler.CrawlerController;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Main {

    private static Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        long begin = System.currentTimeMillis();
        try {
            LogManager.getLogManager().readConfiguration(
                    Main.class.getResourceAsStream("/logging.properties"));
        } catch (IOException e) {
            System.err.println("Could not setup logger configuration: " + e.toString());
        }
        String seed = "";
        List<String> terms = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new File("input.txt"));
            seed = scanner.nextLine();
            while (scanner.hasNext()) {
                terms.add(scanner.nextLine());
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Input file wasn't found", e);
        }

        CrawlerController crawlerController = new CrawlerController(seed, terms);
        crawlerController.start();
        long end = System.currentTimeMillis();
        long time = (end - begin) / 1000;
        System.out.println("It took " + time + " seconds");
    }
}
