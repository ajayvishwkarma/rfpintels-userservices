package com.rfpintels.userservices.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document(collection = "PersonalDetails")
public class PersonalDetails {
	
	@Id
	private String id;

	private String userId;

	private String ipAdresss;

}
