package com.matsoft;

import java.io.IOException;
import java.util.logging.LogManager;

public class LoggerInitializer {
    public static void initLogger() {
        try {
            LogManager.getLogManager().readConfiguration(
                    Main.class.getResourceAsStream("/logging.properties"));
        } catch (IOException e) {
            System.err.println("Could not setup logger configuration: " + e.toString());
        }
    }
}
