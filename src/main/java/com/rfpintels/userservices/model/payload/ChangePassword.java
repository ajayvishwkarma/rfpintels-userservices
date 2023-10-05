package com.rfpintels.userservices.model.payload;

import lombok.Data;
@Data
public class ChangePassword {
	private String userId;
	private String oldPassword;
	private String newPassword;
	private String conformPassword;

}
