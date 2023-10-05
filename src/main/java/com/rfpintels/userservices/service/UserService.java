package com.rfpintels.userservices.service;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rfpintels.userservices.event.EmailEvent;
import com.rfpintels.userservices.exception.ResourceAlreadyInUseException;
import com.rfpintels.userservices.model.CompanyProfile;
import com.rfpintels.userservices.model.PastPerformence;
import com.rfpintels.userservices.model.Role;
import com.rfpintels.userservices.model.RoleName;
import com.rfpintels.userservices.model.Subscription;
import com.rfpintels.userservices.model.SubscriptionPlan;
import com.rfpintels.userservices.model.User;
import com.rfpintels.userservices.model.payload.EmailType;
import com.rfpintels.userservices.model.payload.RegistrationRequest;
import com.rfpintels.userservices.model.payload.ResetPasswordView;
import com.rfpintels.userservices.repository.CompanyProfileRepository;
import com.rfpintels.userservices.repository.PastPerformenceRepository;
import com.rfpintels.userservices.repository.SubscriptionPlanRepository;
import com.rfpintels.userservices.repository.SubscriptionRepository;
import com.rfpintels.userservices.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	RoleService roleService;

	@Autowired
	PastPerformenceRepository pastPerformenceRepository;

	@Autowired
	CompanyProfileRepository companyProfileRepository;

	@Autowired
	ApplicationEventPublisher applicationEventPublisher;

	@Autowired
	SubscriptionPlanRepository subscriptionPlanRepository;

	@Autowired
	SubscriptionRepository subscriptionRepository;

	public User createUser(RegistrationRequest registrationRequest) {
		User newUser = new User();
		Subscription subscription = initialSuscriptionPlan(registrationRequest.getPlanName(),
				registrationRequest.getMaximumUserAllowed(), registrationRequest.getEmail(),
				registrationRequest.getBillingCycle());
		String email = registrationRequest.getEmail();
		String companyName = registrationRequest.getCompanyName();

		RoleName roleName = registrationRequest.getRoleName();
		newUser.setUsername(registrationRequest.getUserName());
		newUser.setEmail(registrationRequest.getEmail());
		newUser.setCompanyName(registrationRequest.getCompanyName());
		newUser.setFirstName(registrationRequest.getFirstName());
		newUser.setLastName(registrationRequest.getLastName());
		newUser.setTitle(registrationRequest.getTitle());
		newUser.setOfficeNumber(registrationRequest.getOfficeNumber());
		newUser.setCellNumber(registrationRequest.getCellNumber());
		newUser.setLicenceType(registrationRequest.getLicenceType());
		newUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
		newUser.setRoles(addRoleToNewUser(roleName));
		newUser.setActive(true);
		newUser.setEmailVerified(false);
		saveCompanyProfile(email, companyName);
//		newUser.setSubscription(subscription);
		return newUser;
	}

	public Subscription initialSuscriptionPlan(String planName, int maximumUserAllowed, String email,
			String billingCycle) {
		Subscription subscriptionSave = new Subscription();
		SubscriptionPlan subscriptionPlan = subscriptionPlanRepository.findByPlanNameAndMaximumUserAllowed(planName,
				maximumUserAllowed);
		String subscriptionType = subscriptionPlan.getSubscriptionType();

		subscriptionSave.setPlanId(subscriptionPlan.getPlanId());
		subscriptionSave.setSubscriptionType(subscriptionPlan.getSubscriptionType());
		subscriptionSave.setEmail(email);
		subscriptionSave.setPlanName(planName);
		subscriptionSave.setBillingCycle(billingCycle);

		if (subscriptionType.equalsIgnoreCase("FREE")) {
			subscriptionSave.setStatus("Approval_Pending");
		} else {
			subscriptionSave.setStatus("Payment_Pending");
		}
		subscriptionSave.setMaximumUserAllowed(maximumUserAllowed);

		if (billingCycle != null) {
			if ("Monthly".equalsIgnoreCase(billingCycle)) {
				subscriptionSave.setRate(subscriptionPlan.getMonthlyRate());
			} else {
				subscriptionSave.setRate(subscriptionPlan.getAnnualRate());
			}
		}
		subscriptionSave.setStartDate(LocalDateTime.now());
		return subscriptionRepository.save(subscriptionSave);

	}

	public void saveCompanyProfile(String email, String companyName) {
		CompanyProfile companyProfile = new CompanyProfile();
		companyProfile.setCompanyName(companyName);
		companyProfile.setEmail(email);
		companyProfileRepository.save(companyProfile);
	}

	public User save(User user) {
		return userRepository.save(user);
	}

	public Boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	private Set<Role> addRoleToNewUser(RoleName roleName) {
		Set<Role> newUserRole = new HashSet<Role>();
		newUserRole.add(roleService.findByRoleName(roleName));
		return newUserRole;
	}

	public PastPerformence getPastPerformence(String dunsNo) {

		PastPerformence pastPerformences = pastPerformenceRepository.findByDuns(dunsNo);
		return pastPerformences;
	}

	public User resetPassword(ResetPasswordView resetPasswordView, String emailId) {
		User user = userRepository.findByEmail(emailId).get();

		String newPassword = resetPasswordView.getNewPassword();
		String confirmPassword = resetPasswordView.getConfirmPassword();
		if (user != null) {
			if (newPassword.equals(confirmPassword)) {
				user.setPassword(passwordEncoder.encode(newPassword));
			}
		}
		System.out.println(user.getPassword());
		return userRepository.save(user);

	}

	public Optional<User> updateAddUser(User user) {
		String ID = user.getId();

		if (ID != null) {
			User userSave = null;
			userSave = userRepository.findById(ID).get();
			userSave.setEmail(user.getEmail());
			userSave.setFirstName(user.getFirstName());
			userSave.setLastName(user.getLastName());
			userSave.setTitle(user.getTitle());
			userSave.setOfficeNumber(user.getOfficeNumber());
			userSave.setCellNumber(user.getCellNumber());
			userSave.setLicenceType(user.getLicenceType());
			return Optional.ofNullable(userRepository.save(userSave));
		} else {
			User userSave = new User();
			userSave.setEmail(user.getEmail());
			String newRegistrationEmailId = user.getEmail();
			Optional.of(emailAlreadyExists(newRegistrationEmailId)).ifPresent(b -> {
				if (b) {
					// LOGGER.error("Email Already exists: " + newRegistrationEmailId);
					throw new ResourceAlreadyInUseException("Email", "Address", newRegistrationEmailId);
				}
			});
			String password = getAlphaNumericString(12);
			userSave.setFirstName(user.getFirstName());
			userSave.setLastName(user.getLastName());
			userSave.setTitle(user.getTitle());
			userSave.setOfficeNumber(user.getOfficeNumber());
			userSave.setCellNumber(user.getCellNumber());
			userSave.setLicenceType(user.getLicenceType());
			userSave.setPassword(passwordEncoder.encode(password));
			sendEmailPassword(userSave, password, user.getEmail());
			userSave.setActive(true);
			userSave.setEmailVerified(true);
			return Optional.ofNullable(userRepository.save(userSave));
		}

	}

	public void sendEmailPassword(User user, String password, String email) {
		EmailEvent emailEvent = new EmailEvent(user, EmailType.PASSWORD_EMAIL, password, email);
		applicationEventPublisher.publishEvent(emailEvent);
	}

	public String getAlphaNumericString(int n) {
		byte[] array = new byte[256];
		new Random().nextBytes(array);
		String randomString = new String(array, Charset.forName("UTF-8"));
		StringBuffer r = new StringBuffer();
		String AlphaNumericString = randomString.replaceAll("[^A-Za-z0-9]", "");
		for (int k = 0; k < AlphaNumericString.length(); k++) {
			if (Character.isLetter(AlphaNumericString.charAt(k)) && (n > 0)
					|| Character.isDigit(AlphaNumericString.charAt(k)) && (n > 0)) {
				r.append(AlphaNumericString.charAt(k));
				n--;
			}
		}
		return r.toString();
	}

	public Boolean emailAlreadyExists(String email) {
		return userRepository.existsByEmail(email);
	}

//	public List<UserShortDetail> getAllUser() {
//
//		List<UserShortDetail> userShortDetail = null;
//		userShortDetail = userRepository.findAll().stream().map(u -> {
//			UserShortDetail userDetail = new UserShortDetail();
//			userDetail.setId(u.getId());
//			userDetail.setFirstName(u.getFirstName());
//			userDetail.setLastName(u.getLastName());
//			userDetail.setTitle(u.getTitle());
//			userDetail.setEmailaddress(u.getEmail());
//			userDetail.setPhonenumber(u.getCellNumber());
//			return userDetail;
//		}).collect(Collectors.toList());
//		return userShortDetail;
//	}

	public void deleteUser(String userId) {
		userRepository.deleteById(userId);
	}

	public List<User> getAllDetailedUser() {
		return userRepository.findAll();
	}

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	public void changePassword(String userId, String oldPassword, String newPassword, String conformPassword) {
		User userTemp = userRepository.findById(userId).get();

		String passTemp = userTemp.getPassword();
		if (passwordEncoder.matches(oldPassword, passTemp)) {
			if (newPassword.equals(conformPassword) && !oldPassword.equals(newPassword)) {
				userTemp.setPassword(passwordEncoder.encode(newPassword));
				userRepository.save(userTemp);
			}
		}
	}

	public User getUserByEmail(String email) {
		Optional<User> userOptional = userRepository.findByEmail(email);
		if (userOptional.isPresent()) {
			return userOptional.get();
		}
		return null;
	}

	public List<User> viewListOfUser(String companyName, String id) {
		List<User> user = userRepository.findByCompanyNameAndId(companyName, id);
		return user;
	}

//	public List<EntityDatum> getProfileByDuns(String ueiSAM) {
//		List<EntityDatum> companyProfile = companyProfileRepo.findAll();
//		List<EntityDatum> entityDatum = new ArrayList<EntityDatum>();
//		String ueisAM;
//		for (EntityDatum c : companyProfile) {
//			ueisAM = c.getEntityRegistration().getUeiSAM();
//			if (ueisAM.equals(ueiSAM)) {
//				entityDatum.add(c);
//
//			}
//
//		}
//
//		return companyProfile;
//	}
//
//	public DisplayAllData displayAllFields(String dunsNo) {
//		DisplayAllData displayData = new DisplayAllData();
//		List<EntityDatum> companyProfile = companyProfileRepo.findAll();
//		List<EntityDatum> entityDatum = new ArrayList<EntityDatum>();
//		String ueisAM;
//		for (EntityDatum c : companyProfile) {
//			ueisAM = c.getEntityRegistration().getUeiSAM();
//			if (ueisAM.equals(dunsNo)) {
//				displayData.setCompanyProfile(c);
//				PastPerformence pastPerformence = pastPerformenceRepository.findByDuns(dunsNo);
//				displayData.setPastPerformence(pastPerformence);
//			}
//		}
//		return displayData;
//
//	}

	public CompanyProfile editCompanyProfile(CompanyProfile companyProfile) {

		String id = companyProfile.getId();
		String companyName = companyProfile.getCompanyName();

		if (companyName != null) {
			CompanyProfile profile = null;
//			profile = companyProfileRepository.findById(id).get();
			profile = companyProfileRepository.findByCompanyName(companyName);
			profile.setAddress(companyProfile.getAddress());
			profile.setDuns(companyProfile.getDuns());
			profile.setCage(companyProfile.getCage());
			profile.setPocEmail(companyProfile.getPocEmail());
			profile.setSetAside(companyProfile.getSetAside());
			profile.setCorporateCertification(companyProfile.getCorporateCertification());
			profile.setNaicsCodes(companyProfile.getNaicsCodes());
			profile.setKeyWords(companyProfile.getKeyWords());
			profile.setCapabilityBriefing(companyProfile.getCapabilityBriefing());
			profile.setWebsites(companyProfile.getWebsites());
			return companyProfileRepository.save(profile);
		} else {
			CompanyProfile profile = new CompanyProfile();
			profile.setAddress(companyProfile.getAddress());
			profile.setDuns(companyProfile.getDuns());
			profile.setCage(companyProfile.getCage());
			profile.setPocEmail(companyProfile.getPocEmail());
			profile.setSetAside(companyProfile.getSetAside());
			profile.setCorporateCertification(companyProfile.getCorporateCertification());
			profile.setNaicsCodes(companyProfile.getNaicsCodes());
			profile.setKeyWords(companyProfile.getKeyWords());
			profile.setCapabilityBriefing(companyProfile.getCapabilityBriefing());
			profile.setWebsites(companyProfile.getWebsites());
			return companyProfileRepository.save(profile);
		}

	}

}
