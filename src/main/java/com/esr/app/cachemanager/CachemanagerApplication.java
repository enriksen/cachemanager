package com.esr.app.cachemanager;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * CacheManager Application initializer
 * @author Enrique Sanchez
 *
 */
@SpringBootApplication
public class CachemanagerApplication {

	private static final Logger LOGGER = Logger.getLogger(CachemanagerApplication.class);
	
    public static void main(String[] args) {
    	LOGGER.info("Starting CacheManager ...");
    	
        SpringApplication.run(CachemanagerApplication.class, args);
        
        LOGGER.info("CacheManager Initialized ...");
        LOGGER.info("Launch http://localhost:8181/cachemanager/cache/add to create Cache Node...");
        LOGGER.info("Launch http://localhost:8181/cachemanager/loader/run to load data from DB to Cache...");
        LOGGER.info("Launch http://localhost:8181/cachemanager/search in a browser to access the Search Form...");
        LOGGER.info("Go to https://github.com/enriksen/cachemanager for more info...");
        
    }
    
}
