package com.rfpintels.userservices.model.payload;

import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Mail {

	private String from;
	private String to;
	private String subject;
	private String content;
	private Map<String, String> model;
}
