package com.rfpintels.userservices.model.payload;

import lombok.Data;

@Data
public class PaymentRequest {
	private String description;
	private Long amount;
	private String email;
	private String paymentMethodId;
	private String paymentIntentId;

}
