package com.rfpintels.userservices.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rfpintels.userservices.exception.NotAllowedEmailException;
import com.rfpintels.userservices.exception.ResourceAlreadyInUseException;
import com.rfpintels.userservices.exception.ResourceNotFoundException;
import com.rfpintels.userservices.model.CustomUserDetails;
import com.rfpintels.userservices.model.EmailVerificationToken;
import com.rfpintels.userservices.model.PersonalDetails;
import com.rfpintels.userservices.model.RefreshToken;
import com.rfpintels.userservices.model.SuperAdmin;
import com.rfpintels.userservices.model.TokenStatus;
import com.rfpintels.userservices.model.User;
import com.rfpintels.userservices.model.payload.LoginRequest;
import com.rfpintels.userservices.model.payload.RegistrationRequest;
import com.rfpintels.userservices.repository.CompanyProfileRepository;
import com.rfpintels.userservices.repository.PersonalDetailsRepository;
import com.rfpintels.userservices.repository.UserRepository;
import com.rfpintels.userservices.security.JwtTokenProvider;

@Service
public class AuthService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

	@Autowired
	UserService userService;

	@Autowired
	EmailVerificationTokenService emailVerificationTokenService;

	@Autowired
	RefreshTokenService refreshTokenService;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtTokenProvider tokenProvider;

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	CompanyProfileRepository companyProfileRepository;

	@Autowired
	PersonalDetailsRepository personalDetailsRepository;

	public Optional<User> registerUser(RegistrationRequest registrationRequest) {
//		User user=userRepository.findById(userId).get();
//		String emailId=user.getEmail();  
		String newRegistrationEmailId = registrationRequest.getEmail();
		Optional.of(EmailAlreadyExists(newRegistrationEmailId)).ifPresent(b -> {
			if (b) {
				LOGGER.error("Email Already exists: " + newRegistrationEmailId);
				throw new ResourceAlreadyInUseException("Email",
						"Email Already exists Please Register With Another Email", newRegistrationEmailId);
			}
		});

		List<String> businessEmail = new ArrayList<String>();
		businessEmail.add("manvish.com");
		String emailParts[] = newRegistrationEmailId.split("@");
		if (emailParts != null && emailParts.length > 1) {
			String domain = emailParts[1];
			if (!businessEmail.contains(domain.toLowerCase())) {
				throw new NotAllowedEmailException(newRegistrationEmailId);
			}
		}

		User registerUser = userService.createUser(registrationRequest);
//		User superUser = getUserById(userId);
//		registerUser.setSuperUser(superUser);
		registerUser = userService.save(registerUser);

		return Optional.ofNullable(registerUser);
	}

	public Boolean EmailAlreadyExists(String email) {
		return userService.existsByEmail(email);
	}

	public Optional<User> confirmEmailRegistration(String emailToken) {
		EmailVerificationToken emailVerificationToken = emailVerificationTokenService.findByToken(emailToken)
				.orElseThrow(() -> new ResourceNotFoundException("Token", "Email verification", emailToken));

		User registeredUser = emailVerificationToken.getUser();
		if (registeredUser.isEmailVerified()) {
			LOGGER.info("User [" + emailToken + "] already registered.");
			return Optional.of(registeredUser);
		}

		emailVerificationTokenService.verifyExpiration(emailVerificationToken);
		emailVerificationToken.setTokenStatus(TokenStatus.STATUS_CONFIRMED);
		emailVerificationTokenService.save(emailVerificationToken);

		registeredUser.setEmailVerified(true);
		userService.save(registeredUser);
		return Optional.of(registeredUser);
	}

	public Optional<Authentication> authenticateUser(LoginRequest loginRequest) {
		return Optional.ofNullable(authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())));
	}

	public Optional<RefreshToken> createAndPersistRefreshTokenForDevice(Authentication authentication,
			LoginRequest loginRequest) {
		User currentUser = (User) authentication.getPrincipal();
		RefreshToken refreshToken = refreshTokenService.createRefreshToken();
		refreshToken.setUser(currentUser);
		refreshToken = refreshTokenService.save(refreshToken);
		return Optional.ofNullable(refreshToken);
	}

	public String generateToken(CustomUserDetails customUserDetails) {
		return tokenProvider.generateToken(customUserDetails);
	}

	public Optional<User> sendRegistrationLink(User user, String email) {

		return Optional.ofNullable(user);
	}

	public Optional<SuperAdmin> admin(SuperAdmin superAdmin, String email) {
		return Optional.ofNullable(superAdmin);
	}

	public User getUserById(String userId) {
		User superUser = userRepository.findById(userId).get();
		LOGGER.info(superUser.toString());
		return superUser;

	}

	public User updateUser(User user) {
		return userRepository.save(user);
	}

	public PersonalDetails get(String id) {
		return personalDetailsRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("id", "Invalid Id", id));
	}

	public ResponseEntity<?> getAllCompanys() {
		return ResponseEntity.ok(companyProfileRepository.findAll());
	}

}
