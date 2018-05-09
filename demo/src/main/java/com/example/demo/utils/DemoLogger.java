package com.example.demo.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DemoLogger {

    static final Logger logger = LogManager.getLogger(DemoLogger.class.getName());

    public static void displayMessage(String classLabel, String message) {
        logger.debug(classLabel + " " + message);
    }

    public static void error(String message){
        logger.error(message);
    }

    public static void fatal(String message){
        logger.fatal(message);
    }

    public static void info(String message){
        logger.info(message);
    }

    public static void debug(String message){
        logger.debug(message);
    }

    public static void warn(String message){
        logger.debug(message);
    }

}
