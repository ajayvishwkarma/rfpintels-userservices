package com.rfpintels.userservices.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class NotAllowedEmailException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private final String email;

	public NotAllowedEmailException(String email) {
		super(String.format("%s email is not allowed.", email));
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

}
