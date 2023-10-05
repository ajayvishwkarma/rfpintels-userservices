package com.rfpintels.userservices.model;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "SUBSCRIPTION_PLAN")
public class SubscriptionPlan {

	@Id
	private String subscriptionId;

	private String planId;

	private String subscriptionType;

	private String planName;
	
	private int maximumUserAllowed;
	
	private String subPlanName;

	private BigDecimal monthlyRate;

	private BigDecimal annualRate;

}
