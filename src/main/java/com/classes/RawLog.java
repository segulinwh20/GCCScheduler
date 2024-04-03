package com.classes;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class RawLog {
    public static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /**
     * RawLog constructor that sets up logger to be properly formatted.
     * Creates a log in the data folder
     */
    public RawLog() {
        try {
            FileHandler fileHandler = new FileHandler("data/log.log");
            logger.addHandler(fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            logger.setUseParentHandlers(false);
        }
        catch(IOException e){
            System.out.println(e);
        }

    }

}
