package com.rfpintels.userservices.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.rfpintels.userservices.model.PaymentCharge;
import com.rfpintels.userservices.model.Subscription;
import com.rfpintels.userservices.model.User;
import com.rfpintels.userservices.model.payload.PaymentRequest;
import com.rfpintels.userservices.model.payload.PaymentResponse;
import com.rfpintels.userservices.repository.PaymentChargeRepository;
import com.rfpintels.userservices.repository.SubscriptionRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.SetupIntent;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.SetupIntentCreateParams;

/**
 * @author PraneethMantri
 *
 */
@Component
public class PaymentService {

	private static final String PAYMENT_METHOD_TYPE_CARD = "card";

	private static final String CURRENCY_USD = "usd";

	private static Logger LOGGER = LoggerFactory.getLogger(PaymentService.class);

	@Autowired
	private UserService userService;
	
	@Autowired
	PaymentChargeRepository paymentChargeRepository;
	
	@Autowired
	SubscriptionRepository subscriptionRepository;

	@Value(value = "${payments.stripe.privateKey}")
	private String stripePrivateKey;

	@PostConstruct
	public void init() {
		Stripe.apiKey = this.stripePrivateKey;
	}

	public PaymentResponse payNow(PaymentRequest paymentRequest) {

		PaymentResponse response = new PaymentResponse();

		// Set your secret key. Remember to switch to your live secret key in
		// production.
		// See your keys here: https://dashboard.stripe.com/apikeys

		try {
			User user = userService.getUserByEmail(paymentRequest.getEmail());
			String customerId = user.getStripeCustomerId();
			if (customerId == null) {
				Customer customer = createStripeCustomer(paymentRequest);
				customerId = customer.getId();
				user.setStripeCustomerId(customerId);
				userService.save(user);
			}
			// store customerID for future use
			// SetupIntent setupIntent = createStripeSetupIntent(customerId);
			PaymentIntent intent = createStripePaymentIntent(paymentRequest, customerId);
			intent = intent.confirm();
			generateResponse(response, intent, paymentRequest );
		} catch (StripeException e) {
			LOGGER.error(e.getMessage());
			generateResponse(response, null,null);
		}

		return response;

	}

	/**
	 * Create a payment intent for given customer and payment details.
	 * 
	 * @param request
	 * @param customer
	 * @return
	 * @throws StripeException
	 */
	private PaymentIntent createStripePaymentIntent(PaymentRequest request, String customer) throws StripeException {
		PaymentIntentCreateParams params = PaymentIntentCreateParams.builder().setAmount(request.getAmount())
				.setCurrency(CURRENCY_USD).setCustomer(customer).addPaymentMethodType(PAYMENT_METHOD_TYPE_CARD)
				.setDescription(request.getDescription()).setPaymentMethod(request.getPaymentMethodId())
				.setConfirmationMethod(PaymentIntentCreateParams.ConfirmationMethod.AUTOMATIC).build();

		PaymentIntent paymentIntent = PaymentIntent.create(params);
		return paymentIntent;
	}

	private Customer createStripeCustomer(PaymentRequest paymentRequest) throws StripeException {
		CustomerCreateParams params = CustomerCreateParams.builder().setEmail(paymentRequest.getEmail())
				.setPaymentMethod(paymentRequest.getPaymentMethodId()).build();
		Customer customer = Customer.create(params);
		return customer;
	}

	private SetupIntent createStripeSetupIntent(String customerId) throws StripeException {
		SetupIntentCreateParams params = SetupIntentCreateParams.builder().setCustomer(customerId)
				.addPaymentMethodType(PAYMENT_METHOD_TYPE_CARD).build();

		SetupIntent setupIntent = SetupIntent.create(params);
		return setupIntent;
	}

	private void generateResponse(PaymentResponse response, PaymentIntent intent,PaymentRequest paymentRequest) {
		if (intent != null && intent.getStatus().equals("succeeded")) {
			savePayment(paymentRequest, intent);
			response.setSuccess(true);
		} else {
			response.setSuccess(false);
			response.setMessage("Unable to complete payment, please try again later");
		}
	}

	/**
	 * Save payment information into DB payment amount, email, sub ID, userId,
	 * charge startDate, end date
	 */
	private void savePayment(PaymentRequest paymentRequest, PaymentIntent intent) {
		PaymentCharge payment = new PaymentCharge();
		User user = userService.getUserByEmail(paymentRequest.getEmail());
		String email = paymentRequest.getEmail();
	    Subscription subscription = subscriptionRepository.findByEmail(email);
	    payment.setPaymentId(intent.getId());
		payment.setAmount(paymentRequest.getAmount());
		payment.setEmail(paymentRequest.getEmail());
		payment.setSubscriptionId(subscription.getId());
		payment.setUserId(user.getId());
		payment.setStartDate(LocalDateTime.now());
		payment.setEndDate(LocalDateTime.now().plusMonths(28));
		paymentChargeRepository.save(payment);
	}

}
