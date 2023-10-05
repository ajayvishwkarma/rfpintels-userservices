package com.rfpintels.userservices.model;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "PAYMENT_CHARGE")
public class PaymentCharge {
	
	private String id;
	
	private String email;
	
	private String userId;
	
	private String paymentId;
	
	private Long amount;
	
	private String subscriptionId;
	
	private String chargeId;
	
	private LocalDateTime startDate;
	
	private LocalDateTime endDate;

}
