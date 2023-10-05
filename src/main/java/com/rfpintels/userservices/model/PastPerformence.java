package com.rfpintels.userservices.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "pastPerformence")
public class PastPerformence {
	
	@Id
	private String id;
	private String duns;
	
	private String contractTitle;

    private String 	contractPeriodofPerformance;

    private String contractValue;

    private String	awardingAgency;

	private String modifications;

	private String govtPointofContact;

}