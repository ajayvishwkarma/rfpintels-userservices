package com.rfpintels.userservices.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import com.rfpintels.userservices.event.EmailEvent;
import com.rfpintels.userservices.exception.NotAllowedEmailException;
import com.rfpintels.userservices.model.Subscription;
import com.rfpintels.userservices.model.User;
import com.rfpintels.userservices.model.payload.APIResponse;
import com.rfpintels.userservices.model.payload.EmailType;
import com.rfpintels.userservices.repository.SubscriptionRepository;
import com.rfpintels.userservices.service.AuthService;
import com.rfpintels.userservices.service.SubscriptionPlanService;
import com.rfpintels.userservices.service.UserService;

@RestController
@RequestMapping("/api/super")
@CrossOrigin
public class SuperAdminController {
	@Autowired
	AuthService authService;

	@Autowired
	ApplicationEventPublisher applicationEventPublisher;

	@Autowired
	UserService userService;
	
	@Autowired
	SubscriptionRepository subscriptionRepository;
	
	@Autowired
	SubscriptionPlanService subscriptionPlanService;

	@Value("${app.business.mail}")
	String businessEmail;

	private static final Logger LOGGER = LoggerFactory.getLogger(SuperAdminController.class);
 
	@PostMapping(value = "/sendRegistrationLink")
	public ResponseEntity<?> sendRegistrationLink(@RequestParam("emailId") String emailId) {
//		List<String> businessEmail = new ArrayList<String>();
//		businessEmail.add("manvish.com");
		String emailParts[] = emailId.split("@");
		if (emailParts != null && emailParts.length > 1) {
			String domain = emailParts[1];
			if (!businessEmail.contains(domain.toLowerCase())) {
				throw new NotAllowedEmailException(emailId);
			}
		}
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User superUser = (User) auth.getPrincipal();
		String userid = superUser.getId();
		LOGGER.info(userid);
		UriComponentsBuilder uriComponentsBuilder = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/api/auth/register");
		EmailEvent emailEvent = new EmailEvent(uriComponentsBuilder, superUser, EmailType.REGISTRATION_EMAIL_LINK,
				emailId);
		applicationEventPublisher.publishEvent(emailEvent);
		LOGGER.info(superUser.toString());
		LOGGER.info(emailId);

		return ResponseEntity
				.ok(APIResponse.builder().message("Registration Link has been sent.").success(true).build());

	}

//	@PostMapping("/adduser")
//	public Optional<User> addUser(@RequestBody User user, String superId) {
//		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//		User superUser = (User) auth.getPrincipal();
//		superId = superUser.getId();
//		return userService.updateAddUser(user, superId);
//
//	}
	
	@GetMapping("/approve")
	public ResponseEntity<?> approval(@RequestParam("email") String email, User user) {
		Subscription subscription = subscriptionRepository.findByEmail(email);

		String Status = subscription.getStatus();
		String emailId = subscription.getEmail();
		LOGGER.info(Status);
		subscription.setStatus("Approve");
		subscriptionRepository.save(subscription);

		UriComponentsBuilder uriComponentsBuilder = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/api/auth/login");
		EmailEvent emailEvent = new EmailEvent(uriComponentsBuilder, user, EmailType.USER_APPROVE_EMAIL_LINK, emailId);
		applicationEventPublisher.publishEvent(emailEvent);

		return ResponseEntity.ok(APIResponse.builder().message("User is Approve.").success(true).build());

	}

	@GetMapping("/deny")
	public ResponseEntity<?> deny(@RequestParam("email") String email, User user) {
		Subscription subscription = subscriptionRepository.findByEmail(email);

		String Status = subscription.getStatus();
		String emailId = subscription.getEmail();
		LOGGER.info(Status);
		subscription.setStatus("Deny");
		subscriptionRepository.save(subscription);
		UriComponentsBuilder uriComponentsBuilder = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/api/auth/login");
		EmailEvent emailEvent = new EmailEvent( uriComponentsBuilder,user, EmailType.USER_DENY_EMAIL_LINK, emailId);
		applicationEventPublisher.publishEvent(emailEvent);

		return ResponseEntity.ok(APIResponse.builder().message("User is Deny.").success(true).build());

	}
	
	/*
	 * view Subscription details by companyName
	 */
	@GetMapping("/getByCompany")
	public ResponseEntity<?>  getCompanys(@RequestParam("companyName") String companyName) {
		
	return subscriptionPlanService.getCompany(companyName);
	}
	
	@GetMapping("/getListOfCompany")
	public  ResponseEntity<?> getAllCompany(){
		return authService.getAllCompanys();
	}
	
	@GetMapping("/viewListOfUser")
	public List<User> viewList(@RequestParam("companyName") String companyName,@RequestParam("id") String id){
		return userService.viewListOfUser(companyName,id);
		
	}
}
