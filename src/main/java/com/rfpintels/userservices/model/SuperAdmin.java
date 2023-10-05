package com.rfpintels.userservices.model;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
@Data
@Document(collection = "Superadmin")
public class SuperAdmin extends User{
	
	private String adminId;
	
}
