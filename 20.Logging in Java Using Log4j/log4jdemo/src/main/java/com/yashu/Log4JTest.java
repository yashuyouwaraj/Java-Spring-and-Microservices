package com.yashu;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log4JTest {
    private static final Logger logger = LogManager.getLogger(Log4JTest.class);

    public static void main(String[] args) {
        process();
    }
    public static void process(){
        logger.trace("From THE TRACE METHOD");
        logger.debug("From THE DEBUG METHOD");
        logger.info("From THE INFO METHOD");
        logger.warn("From THE WARN METHOD");
        logger.error("From THE ERROR METHOD");
        logger.fatal("From THE FATAL METHOD");
    }
}
