package com.rfpintels.userservices.model.payload;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIResponse {

	private final String message;
	private final boolean success;
	private final String timestamp;
	private final String cause;

}
