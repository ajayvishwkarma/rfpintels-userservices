package com.rfpintels.userservices.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import com.rfpintels.userservices.event.EmailEvent;
import com.rfpintels.userservices.exception.InvalidTokenRequestException;
import com.rfpintels.userservices.exception.TokenRefreshException;
import com.rfpintels.userservices.exception.UserLoginException;
import com.rfpintels.userservices.exception.UserRegistrationException;
import com.rfpintels.userservices.model.CustomUserDetails;
import com.rfpintels.userservices.model.PersonalDetails;
import com.rfpintels.userservices.model.RefreshToken;
import com.rfpintels.userservices.model.TokenRefreshRequest;
import com.rfpintels.userservices.model.User;
import com.rfpintels.userservices.model.payload.APIResponse;
import com.rfpintels.userservices.model.payload.EmailType;
import com.rfpintels.userservices.model.payload.JwtAuthenticationResponse;
import com.rfpintels.userservices.model.payload.LoginRequest;
import com.rfpintels.userservices.model.payload.RegistrationRequest;
import com.rfpintels.userservices.model.payload.ResetPasswordView;
import com.rfpintels.userservices.repository.PersonalDetailsRepository;
import com.rfpintels.userservices.repository.UserRepository;
import com.rfpintels.userservices.security.JwtTokenProvider;
import com.rfpintels.userservices.service.AuthService;
import com.rfpintels.userservices.service.RefreshTokenService;
import com.rfpintels.userservices.service.UserService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	AuthService authService;

	@Autowired
	ApplicationEventPublisher applicationEventPublisher;

	@Autowired
	JwtTokenProvider tokenProvider;

	@Autowired
	UserService userService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	PersonalDetailsRepository personalDetailsRepository;

	@Autowired
	RefreshTokenService refreshTokenService;

//	@PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<?> registerUser(@RequestBody RegistrationRequest registrationRequest,
//			@RequestParam("userId") String userId) {
//		User superUser = userRepository.findById(userId).get();
//		String email = superUser.getEmail();
//
//		return authService.registerUser(registrationRequest, userId).map(user -> {
//			UriComponentsBuilder uriComponentsBuilder = ServletUriComponentsBuilder.fromCurrentContextPath()
//					.path("/api/auth/registrationConfirmation");
//			EmailEvent onUserRegistrationCompleteEvent = new EmailEvent(uriComponentsBuilder, user,
//					EmailType.REGISTRATION_SUCCESFULL_EMAIL_LINK, user.getEmail());
//			applicationEventPublisher.publishEvent(onUserRegistrationCompleteEvent);
//			// userRegisteredSuccesfull(email);
//			uriComponentsBuilder = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/auth/resetPassword");
//			EmailEvent emailEvent = new EmailEvent(uriComponentsBuilder, superUser,
//					EmailType.USER_REGISTERED_SUCCESFULL, email);
//			applicationEventPublisher.publishEvent(emailEvent);
//			return ResponseEntity.ok(APIResponse.builder()
//					.message("User registered successfully. Check your email for verification.").success(true).build());
//		}).orElseThrow(() -> new UserRegistrationException(registrationRequest.getEmail(),
//				"Missing user object in database."));
//	}

	@PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> registerUser(@RequestBody RegistrationRequest registrationRequest) {
		return authService.registerUser(registrationRequest).map(user -> {
			UriComponentsBuilder uriComponentsBuilder = ServletUriComponentsBuilder.fromCurrentContextPath()
					.path("/api/auth/registrationConfirmation");
			EmailEvent onUserRegistrationCompleteEvent = new EmailEvent(uriComponentsBuilder, user,
					EmailType.REGISTRATION_SUCCESFULL_EMAIL_LINK, user.getEmail());
			applicationEventPublisher.publishEvent(onUserRegistrationCompleteEvent);
			return ResponseEntity.ok(APIResponse.builder()
					.message("User registered successfully. Check your email for verification.").success(true).build());
		}).orElseThrow(() -> new UserRegistrationException(registrationRequest.getEmail(),
				"Missing user object in database."));
	}

	@GetMapping("/registrationConfirmation")
	public ResponseEntity<?> confirmRegistration(@RequestParam("token") String token) {
		return authService.confirmEmailRegistration(token)
				.map(user -> ResponseEntity
						.ok(APIResponse.builder().message("User verified successfully").success(true).build()))
				.orElseThrow(() -> new InvalidTokenRequestException("Email Verification Token", token,
						"Failed to confirm. Please generate a new email verification request"));
	}

	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {

		Authentication authentication = authService.authenticateUser(loginRequest)
				.orElseThrow(() -> new UserLoginException("Couldn't login user [" + loginRequest + "]"));

		CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
		LOGGER.info("Logged in User returned [API]: " + customUserDetails.getUsername());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = (User) auth.getPrincipal();
		String userId = user.getId();
		String ipAddress = request.getRemoteAddr();
		return authService.createAndPersistRefreshTokenForDevice(authentication, loginRequest)
				.map(refreshToken -> refreshToken.getToken()).map(t -> {
					String jwtToken = authService.generateToken(customUserDetails);
					try {
						PersonalDetails personalDetails = new PersonalDetails();
//						InetAddress localHost = InetAddress.getLocalHost();
//						String ipaddress = localHost.getHostAddress();
						System.out.println(ipAddress);
						System.out.println(userId);
						personalDetails.setIpAdresss(ipAddress);
						personalDetails.setUserId(userId);
						personalDetailsRepository.save(personalDetails);

					} catch (Exception e) {
						e.printStackTrace();
					}

					return ResponseEntity.ok(new JwtAuthenticationResponse(jwtToken, t,
							tokenProvider.getExpiryDuration(), loginRequest.getEmail()));
				})
				.orElseThrow(() -> new UserLoginException("Couldn't create refresh token for: [" + loginRequest + "]"));
	}

	@PostMapping("/refresh")
	public ResponseEntity<?> refreshJwtToken(@Valid @RequestBody TokenRefreshRequest tokenRefreshRequest) {

		String requestRefreshToken = tokenRefreshRequest.getRefreshToken();

		Optional<String> token = Optional.of(refreshTokenService.findByToken(requestRefreshToken).map(refreshToken -> {
			refreshTokenService.verifyExpiration(refreshToken);
//	          userService.verifyRefreshAvailability(refreshToken);
			refreshTokenService.increaseCount(refreshToken);
			return refreshToken;
		}).map(RefreshToken::getUser)
//	      .map(User::getId)
				.map(u -> tokenProvider.generateToken(new CustomUserDetails(u)))
				.orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
						"Missing refresh token in database. Please login again")));
		return ResponseEntity.ok().body(new JwtAuthenticationResponse(token.get(),
				tokenRefreshRequest.getRefreshToken(), tokenProvider.getExpiryDuration(), null));
	}

	@GetMapping("/getPersonalDetails")
	public ResponseEntity<PersonalDetails> getPersonalDetails(@RequestParam("id") String id) {
		PersonalDetails personalDetails = authService.get(id);
		return new ResponseEntity<PersonalDetails>(personalDetails, HttpStatus.OK);
	}

	@PostMapping(value = "/sendForgotPasswordLink")
	public ResponseEntity<?> forgoPassword(@RequestParam("emailId") String emailId) {
		User user = userRepository.findByEmailId(emailId);
		UriComponentsBuilder uriComponentsBuilder = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/api/auth/resetPassword");
		EmailEvent emailEvent = new EmailEvent(uriComponentsBuilder, user, EmailType.FORGOT_PASSWORD, emailId);
		applicationEventPublisher.publishEvent(emailEvent);
		return ResponseEntity
				.ok(APIResponse.builder().message("Reset password Link has been  send.").success(true).build());
	}

	@PutMapping("/resetPassword")
	public ResponseEntity<?> resetPasswordUser(@RequestBody ResetPasswordView resetPasswordView,
			@RequestParam("emailId") String emailId) {
		userService.resetPassword(resetPasswordView, emailId);
		return ResponseEntity.ok(APIResponse.builder().message("password reset successfully").success(true).build());
	}

}
