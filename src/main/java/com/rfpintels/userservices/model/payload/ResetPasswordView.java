package com.rfpintels.userservices.model.payload;

import lombok.Data;

@Data
public class ResetPasswordView {
	
	private String newPassword;
	private String confirmPassword;

}
