package com.rfpintels.userservices.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rfpintels.userservices.cache.LoggedOutJwtTokenCache;
import com.rfpintels.userservices.event.OnUserLogoutSuccessEvent;
import com.rfpintels.userservices.model.CompanyProfile;
import com.rfpintels.userservices.model.PastPerformence;
import com.rfpintels.userservices.model.User;
import com.rfpintels.userservices.model.payload.APIResponse;
import com.rfpintels.userservices.model.payload.ChangePassword;
import com.rfpintels.userservices.model.payload.LogOutRequest;
import com.rfpintels.userservices.repository.UserRepository;
import com.rfpintels.userservices.service.UserService;

/*
 * rfpintels-80
 */

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

	Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	HttpServletRequest request;

	@Autowired
	UserRepository userRepository;

	@Autowired
	LoggedOutJwtTokenCache loggedOutJwtTokenCache;

	@Autowired
	UserService userService;

	@GetMapping(value = "/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
	public User getUser(@PathVariable String email) {
		logger.info("email: {}", email);

		Optional<User> user = userRepository.findById(email);

		if (user.isPresent()) {
			logger.info("user found | email: {}", email);
		} else {
			logger.info("user not found | email: {}", email);
		}

		return user.orElse(null);
	}

	@PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> addUser(@RequestBody User user) {
		User insertedUser = userRepository.insert(user);
		logger.info("added new user: {}", insertedUser);
		return ResponseEntity.ok(APIResponse.builder().message("User Added Successfully.").success(true).build());
	}

	@GetMapping("/test")
	public String test() {
		return "userService is running.";
	}

	@PutMapping("/logOut")
	public ResponseEntity<?> logOut() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = (User) auth.getPrincipal();
		String token = (String) auth.getCredentials();
		String emailId = user.getEmail();
		logger.info(emailId);
		OnUserLogoutSuccessEvent onUserLogoutSuccessEvent = new OnUserLogoutSuccessEvent(emailId, token,
				new LogOutRequest());
		loggedOutJwtTokenCache.markLogoutEventForToken(onUserLogoutSuccessEvent);
		return ResponseEntity.ok(APIResponse.builder().message("User logout successfully.").success(true).build());
	}

//	@GetMapping("/getAllUser")
//	public List<UserShortDetail> getAllUser() {
//		return userService.getAllUser();
//	}

	@GetMapping("/getAllUser")
	public List<User> getAllUsers() {
		return userService.getAllUsers();
	}

	@DeleteMapping("/deleteUser/{userId}")
	public void deleteUser(@PathVariable("userId") String userId) {
		userService.deleteUser(userId);
	}

	@GetMapping("/getDetailedUser")
	public ResponseEntity<List<User>> getAllDetailedUser() {
		return new ResponseEntity<List<User>>(userService.getAllDetailedUser(), HttpStatus.OK);
	}

	@PutMapping("/updateAddUser")
	public ResponseEntity<Optional<User>> editAddUser(@RequestBody User user) {
		return new ResponseEntity<Optional<User>>(userService.updateAddUser(user), HttpStatus.OK);
	}

	@PostMapping("/changePassword")
	public ResponseEntity<?> changePassword(@RequestBody ChangePassword changePassword) {
		String userId = changePassword.getUserId();
		String oldPassword = changePassword.getOldPassword();
		String newPassword = changePassword.getNewPassword();
		String conformPassword = changePassword.getConformPassword();
		userService.changePassword(userId, oldPassword, newPassword, conformPassword);
		return ResponseEntity.ok(APIResponse.builder().message("Password Change successfully.").success(true).build());
	}

	@GetMapping("/userByEmail")
	public ResponseEntity<User> getUserByEmail(String email) {
		User user = userService.getUserByEmail(email);
		return ResponseEntity.ok(user);
	}

	@GetMapping("getPastperformenceById")
	public ResponseEntity<PastPerformence> getPastData(@RequestParam("duns") String dunsNo) {
		return new ResponseEntity<PastPerformence>(userService.getPastPerformence(dunsNo), HttpStatus.OK);
	}

//	@GetMapping("getCompanyProfileByDuns")
//	public ResponseEntity<EntityDatum> getCompanytData(@RequestParam("ueiSAM") String ueiSAM) {
//		return new ResponseEntity(userService.getProfileByDuns(ueiSAM), HttpStatus.OK);
//	}
//	
//	
//	@GetMapping("/displayAllTheFieldsCaptured")
//	public  ResponseEntity<DisplayAllData> displayAllFields(@RequestParam("dunsnumber") String dunsnumber) {
//		return new ResponseEntity<DisplayAllData>(userService.displayAllFields(dunsnumber),HttpStatus.OK);
//	}

	@PutMapping("/editCompanyProfile")
	public CompanyProfile editCompanyProfile(@RequestBody CompanyProfile companyProfile) {
		return userService.editCompanyProfile(companyProfile);
	}

}
