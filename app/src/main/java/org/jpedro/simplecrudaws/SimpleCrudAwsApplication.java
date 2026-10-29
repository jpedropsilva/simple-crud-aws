package org.jpedro.simplecrudaws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SimpleCrudAwsApplication {

    private static final Logger log = LoggerFactory.getLogger(SimpleCrudAwsApplication.class);

    public static void main(String[] args) {
        log.info("Starting SimpleCrudAwsApplication...");
        SpringApplication.run(SimpleCrudAwsApplication.class, args);
        log.info("SimpleCrudAwsApplication started successfully.");
    }

}
