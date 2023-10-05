package com.rfpintels.userservices.service;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.rfpintels.userservices.event.EmailEvent;
import com.rfpintels.userservices.model.Contact;
import com.rfpintels.userservices.model.Subscription;
import com.rfpintels.userservices.model.SubscriptionPlan;
import com.rfpintels.userservices.model.User;
import com.rfpintels.userservices.model.payload.EmailType;
import com.rfpintels.userservices.repository.ContactDetailsRepository;
import com.rfpintels.userservices.repository.SubscriptionPlanRepository;
import com.rfpintels.userservices.repository.SubscriptionRepository;
import com.rfpintels.userservices.repository.UserRepository;

@Service
public class SubscriptionPlanService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SubscriptionPlanService.class);

	@Autowired
	SubscriptionPlanRepository subscriptionPlanRepository;

	@Autowired
	SubscriptionRepository subscriptionRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	ContactDetailsRepository contactDetailsRepository;

	@Autowired
	ApplicationEventPublisher applicationEventPublisher;

	public List<SubscriptionPlan> getSubscriptionPlans() {
		return subscriptionPlanRepository.findAll();
	}

	public Subscription addUpdatePlan(Subscription subscription) {
		String id = subscription.getId();
		if (id != null) {
			Subscription subscriptionSave = null;
			subscriptionSave = subscriptionRepository.findById(id).get();
			subscriptionSave.setEmail(subscription.getEmail());
			subscriptionSave.setSubscriptionType(subscription.getSubscriptionType());
			subscriptionSave.setRate(subscription.getRate());
			subscriptionSave.setBillingCycle(subscription.getBillingCycle());
			subscriptionSave.setStatus(subscription.getStatus());
			subscriptionSave.setStartDate(LocalDateTime.now());
			subscriptionSave.setEndDate(LocalDateTime.now().plusMonths(28));
			return subscriptionRepository.save(subscription);

		} else {

			Subscription subscriptionSave = new Subscription();
			subscriptionSave.setPlanId(subscription.getPlanId());
			subscriptionSave.setEmail(subscription.getEmail());
			subscriptionSave.setSubscriptionType(subscription.getSubscriptionType());
			subscriptionSave.setRate(subscription.getRate());
			subscriptionSave.setBillingCycle(subscription.getBillingCycle());
			subscriptionSave.setStatus(subscription.getStatus());
//			subscriptionSave.setStripeToken(subscription.getStripeToken());
			subscriptionSave.setStartDate(LocalDateTime.now());
			subscriptionSave.setEndDate(LocalDateTime.now().plusMonths(28));
			return subscriptionRepository.save(subscription);
		}

	}

	public List<Subscription> getTrialList(String status, String planName) {

		return subscriptionRepository.findByStatusAndPlanName(status, planName);
	}

	public ResponseEntity<?> getCompany(String companyName) {
		User user = userRepository.findByCompanyName(companyName);
		String email = user.getEmail();
		Subscription subscriptionDetails = subscriptionRepository.findByEmail(email);

		return ResponseEntity.ok(subscriptionDetails);

	}

	public Contact savecontactDetails(Contact contact) {
		String id = "2";
		List<User> userList = userRepository.findByRoleId(id);
		User user = userList.get(0);
		String email = user.getEmail();
		LOGGER.info(user.toString());
		String firstName = contact.getFirstName();
		String lastName = contact.getLastName();
		String jobTittle = contact.getJobTittle();
		String workEmail =  contact.getWorkEmail();
		String phoneNo = contact.getPhoneNo();
		String companyName = contact.getCompanyName();
		String companySize = contact.getCompanySize();
		String comments = contact.getComments();

		Contact contacts = new Contact();
		contacts.setFirstName(contact.getFirstName());
		contacts.setLastName(contact.getLastName());
		contacts.setWorkEmail(contact.getWorkEmail());
		contacts.setJobTittle(contact.getJobTittle());
		contacts.setPhoneNo(contact.getPhoneNo());
		contacts.setCompanyName(contact.getCompanyName());
		contacts.setCompanySize(contact.getCompanySize());
		contacts.setComments(contact.getComments());
		sendEmailContact(user, firstName, lastName, jobTittle,workEmail, phoneNo, companyName, companySize, comments, email);

		return contactDetailsRepository.save(contact);
	}

	public void sendEmailContact(User user, String firstName, String lastName, String jobTittle,String workEmail, String phoneNo,
			String companyName, String companySize, String comments, String email) {
		EmailEvent emailEvent = new EmailEvent(user, EmailType.CONTACT_EMAIL, firstName, lastName, jobTittle,workEmail, phoneNo,
				companyName, companySize, comments, email);
		applicationEventPublisher.publishEvent(emailEvent);
	}
}
