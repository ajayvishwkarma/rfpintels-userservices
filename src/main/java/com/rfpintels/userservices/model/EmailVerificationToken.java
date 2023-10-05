package com.rfpintels.userservices.model;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
@Document(collection = "EMAIL_VERIFICATION_TOKEN")
public class EmailVerificationToken {

	@Id
	private String id;

	private String token;

	private User user;

	private TokenStatus tokenStatus;

	private Instant expiryDate;

	@CreatedDate
	private Instant createdDate;

	@LastModifiedDate
	private Instant lastModifiedDate;

}
