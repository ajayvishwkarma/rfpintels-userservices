package com.rfpintels.userservices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;


@SpringBootApplication
@Profile({"default","dev","stage","prod"})
public class RfpintelsUserservicesApplication {

	private static final Logger logger = LoggerFactory.getLogger(RfpintelsUserservicesApplication.class);

	public static void main(String[] args) {
		logger.info("Starting userservices application ... ");
		SpringApplication.run(RfpintelsUserservicesApplication.class, args);
	}

}
