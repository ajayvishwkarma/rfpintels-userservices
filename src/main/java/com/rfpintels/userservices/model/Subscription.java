package com.rfpintels.userservices.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "SUBSCRIPTION_DETAILS")
public class Subscription {

	private String id;

	private String planId;

	private String email;

	private String subscriptionType;

	private BigDecimal Rate;

	private String billingCycle;

	private String status;
	
	private String planName;
	
	private int maximumUserAllowed;

	private LocalDateTime startDate;

	private LocalDateTime endDate;

}
