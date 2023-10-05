package com.rfpintels.userservices.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "Contact_Details")
public class Contact {

	@Id
	private String id;

	private String firstName;

	private String lastName;

	private String workEmail;
	
	private String jobTittle;

	private String phoneNo;
	
	private String companyName;
	
	private String companySize;
	
	private String comments;

}
