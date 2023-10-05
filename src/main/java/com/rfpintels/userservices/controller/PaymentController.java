package com.rfpintels.userservices.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rfpintels.userservices.model.payload.APIResponse;
import com.rfpintels.userservices.model.payload.PaymentRequest;
import com.rfpintels.userservices.model.payload.PaymentResponse;
import com.rfpintels.userservices.service.PaymentService;

@RestController
@RequestMapping("/payment")
@CrossOrigin
public class PaymentController {

	Logger logger = LoggerFactory.getLogger(PaymentController.class);

	@Autowired
	PaymentService service;

	@PostMapping("/payNow")
	public ResponseEntity<APIResponse> payNow(@RequestBody PaymentRequest paymentRequest) {
		PaymentResponse response = service.payNow(paymentRequest);
		if (response.isSuccess()) {
			return ResponseEntity.ok(APIResponse.builder().success(true).build());
		}
		return ResponseEntity.ok(APIResponse.builder().message(response.getMessage()).success(false).build());
	}

}
