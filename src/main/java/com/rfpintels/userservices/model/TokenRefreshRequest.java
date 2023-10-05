package com.rfpintels.userservices.model;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class TokenRefreshRequest {
	@NotBlank(message = "Refresh token cannot be blank")
	private String refreshToken;

	// Getters, Setters, Constructor
}