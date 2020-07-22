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

/**
 * <h1>Web Crawler</h1>
 *  This is a console application that searches predefined terms on different websites
 *  starting from the predefined seed URL and following all found links there.
 *  The program stops execution when the number of pages exceeds the limit (1000 by default) or when there is no more pages to visit.
 *  The maximum depth of the search is 8 pages deep from the seed page.
 *  The program takes seed URL and terms from the input.txt file that has the following format:
 *  Seed url + line break <br>
 *  term1 + line break <br>
 *  term2 + line break <br>
 *  ... <br>
 *  The terms can contain spaces.
 *  <p><b>Note:</b> The search is case-sensitive</p>
 *
 * @author Vladislav Matskevich
 * @version 1.0
 * @since 07.07.2020
 */
public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        try {
            LogManager.getLogManager().readConfiguration(
                    Main.class.getResourceAsStream("/logging.properties"));
        } catch (IOException e) {
            System.err.println("Could not setup logger configuration: " + e.toString());
        }
        String seed = "";
        List<String> terms = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File("input.txt"))) {
            seed = scanner.nextLine();
            while (scanner.hasNext()) {
                terms.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Input file wasn't found", e);
        }

        CrawlerController crawlerController = new CrawlerController(seed, terms);
        crawlerController.start();
    }
}
