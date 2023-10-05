package com.rfpintels.userservices.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rfpintels.userservices.model.Contact;
import com.rfpintels.userservices.model.Subscription;
import com.rfpintels.userservices.model.SubscriptionPlan;
import com.rfpintels.userservices.service.SubscriptionPlanService;

@RestController
@RequestMapping("/subscriptionPlans")
@CrossOrigin
public class SubscriptionPlanController {

	private static final Logger LOGGER = LoggerFactory.getLogger(SubscriptionPlanController.class);

	@Autowired
	SubscriptionPlanService subscriptionPlanService;
	
	@GetMapping("/ListOfSubscriptionPlan")
	public ResponseEntity<List<SubscriptionPlan>> getSubscriptionPlans() {
		return new ResponseEntity<List<SubscriptionPlan>>(subscriptionPlanService.getSubscriptionPlans(),HttpStatus.OK);
	}
	
	@PostMapping("/contact")
	public ResponseEntity<Contact> savecontactDetails(@RequestBody Contact contact){
		return new ResponseEntity<Contact>(subscriptionPlanService.savecontactDetails(contact),HttpStatus.OK);
		
	}
	
	
	@PutMapping("addUpdateSubscription")
	public Subscription addUpdateSubscription(@RequestBody Subscription subscription) {
		return subscriptionPlanService.addUpdatePlan(subscription);
	}
	
	@GetMapping("/trialList")
	public List<Subscription> getTrialList(@RequestParam("status") String status, @RequestParam("planName") String planName){
		return subscriptionPlanService.getTrialList(status, planName);
	}
}
