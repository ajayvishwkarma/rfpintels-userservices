package com.rfpintels.userservices.model;

import org.springframework.data.annotation.Id;

import com.mongodb.lang.NonNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserShortDetail {
	@Id
	private String id;

	@NonNull
	private String firstName;

	@NonNull
	private String lastName;

	private String title;

	private String emailaddress;

	private String phonenumber;

}
