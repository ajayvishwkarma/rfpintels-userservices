package com.rfpintels.userservices.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "CompanyProfile")
public class CompanyProfile {

	@Id
	private String id;

	private String address;

	private String duns;

	private String cage;

	private String pocEmail;

	private String setAside;

	private String corporateCertification;

	private String naicsCodes;

	private String keyWords;

	private String capabilityBriefing;

	private String websites;

	private String email;

	private String companyName;

}