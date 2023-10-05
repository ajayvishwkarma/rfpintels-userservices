package com.rfpintels.userservices.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.rfpintels.userservices.exception.InvalidTokenRequestException;
import com.rfpintels.userservices.model.EmailVerificationToken;
import com.rfpintels.userservices.model.TokenStatus;
import com.rfpintels.userservices.model.User;
import com.rfpintels.userservices.repository.EmailVerificationTokenRepository;

@Service
public class EmailVerificationTokenService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EmailVerificationTokenService.class);

	@Autowired
	public EmailVerificationTokenRepository emailVerificationTokenRepository;

	@Value("${app.token.email.verification.duration}")
	private Long emailVerificationTokenExpiryDuration;

	public String generateNewToken() {
		return UUID.randomUUID().toString();
	}

	public void createVerificationToken(User user, String token) {
      EmailVerificationToken emailVerificationToken = EmailVerificationToken.builder()
    		                                          .token(token)
    		                                          .tokenStatus(TokenStatus.STATUS_PENDING)
    		                                          .user(user)
    		                                          .expiryDate(Instant.now().plusMillis(emailVerificationTokenExpiryDuration))
    		                                          .build();
      LOGGER.info("Generated Email verification token " + emailVerificationToken);
      emailVerificationTokenRepository.save(emailVerificationToken);
	}
	
    public Optional<EmailVerificationToken> findByToken(String token) {
        return emailVerificationTokenRepository.findByToken(token);
    }
    
    public void verifyExpiration(EmailVerificationToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            throw new InvalidTokenRequestException("Email Verification Token", token.getToken(), "Expired token. Please issue a new request");
        }
    }

    public EmailVerificationToken save(EmailVerificationToken emailVerificationToken) {
        return emailVerificationTokenRepository.save(emailVerificationToken);
    }
}
