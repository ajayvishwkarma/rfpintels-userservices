/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rfpintels.userservices.model;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import com.mongodb.lang.NonNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@Setter
@Document(collection = "USER")

public class User {

	@Id
	private String id;

	@NonNull
	private String email;

	@NonNull
	private String username;

	@NonNull
	private String password;

	@NonNull
	private String firstName;

	@NonNull
	private String lastName;

	private boolean active;

	@DBRef
	private Set<Role> roles = new HashSet<>();

	@DBRef
	private User superUser;

	private boolean emailVerified;

	@CreatedDate
	private Instant createdDate;

	@LastModifiedDate
	private Instant lastModifiedDate;

	private String companyName;

	private String title;

	private String officeNumber;

	private String cellNumber;

	private String licenceType;
	
	private String stripeCustomerId;
	
	public User() {
		super();
	}
	

	public User(User user) {
		id = user.getId();
		username = user.getUsername();
		password = user.getPassword();
		firstName = user.getFirstName();
		lastName = user.getLastName();
		email = user.getEmail();
		active = user.isActive();
		roles = user.getRoles();
		emailVerified = user.isEmailVerified();
	}

}
