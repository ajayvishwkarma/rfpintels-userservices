package com.rfpintels.userservices;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.rfpintels.userservices.model.payload.PaymentRequest;
import com.rfpintels.userservices.model.payload.PaymentResponse;
import com.rfpintels.userservices.service.PaymentService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentMethod;
import com.stripe.param.PaymentMethodCreateParams;
import com.stripe.param.PaymentMethodCreateParams.CardDetails;

@SpringBootTest
@ActiveProfiles("dev")
class PaymentTests {

	private static String paymentMethod = "pm_1L2a0u2eZvKYlo2CS5tbdI76";

	@Autowired
	PaymentService service;

//	@BeforeEach
//	void setup() {
//		Stripe.apiKey = "sk_test_4eC39HqLyjWDarjtT1zdp7dc";
//		PaymentMethodCreateParams params = PaymentMethodCreateParams.builder().setCard(CardDetails.builder()
//				.setNumber("4242424242424242").setExpMonth(12L).setExpYear(34L).setCvc("123").build()).setType(PaymentMethodCreateParams.Type.CARD).build();
//		try {
//			PaymentMethod method = PaymentMethod.create(params);
//			paymentMethod = method.getId();
//		} catch (StripeException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}

//	@Test
//	 void testPayment() {
//		PaymentRequest request = new PaymentRequest();
//		request.setAmount(1000L);
//		request.setDescription("test payment2");
//		request.setEmail("vikalp.soni@manvish.com");
//		request.setPaymentMethodId(paymentMethod);
//		PaymentResponse response = service.payNow(request);
//		Assertions.assertTrue(response.isSuccess());
//	}



}
