package com.rfpintels.userservices.model.payload;

import com.rfpintels.userservices.model.RoleName;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class RegistrationRequest {

	@NonNull
	private String userName;

	@NonNull
	private String email;

	@NonNull
	private String password;

	@NonNull
	private RoleName roleName;
	
	private String firstName;
	
	private String lastName;
	
	private String companyName;
	
	private String title;
	
	private String officeNumber;
	
	private String cellNumber;
	
	private String licenceType;
	
	private String planName;
	
	private int maximumUserAllowed;
	
	private String billingCycle;

}
