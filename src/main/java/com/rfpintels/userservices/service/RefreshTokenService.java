package com.rfpintels.userservices.service;

import java.time.Instant;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.rfpintels.userservices.exception.TokenRefreshException;
import com.rfpintels.userservices.model.RefreshToken;
import com.rfpintels.userservices.repository.RefreshTokenRepository;
import com.rfpintels.userservices.util.Util;

@Service
public class RefreshTokenService {

	private final RefreshTokenRepository refreshTokenRepository;

	@Value("${app.token.refresh.duration}")
	private Long refreshTokenDurationMs;

	@Autowired
	public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
		this.refreshTokenRepository = refreshTokenRepository;
	}

	public Optional<RefreshToken> findByToken(String token) {
		return refreshTokenRepository.findByToken(token);
	}

	public RefreshToken save(RefreshToken refreshToken) {
		return refreshTokenRepository.save(refreshToken);
	}

	public RefreshToken createRefreshToken() {
		RefreshToken refreshToken = RefreshToken.builder()
				                    .expiryDate(Instant.now().plusMillis(refreshTokenDurationMs))
				                    .token(Util.generateRandomUuid())
				                    .refreshCount(0L)
				                    .build();
		return refreshToken;
	}

	public void verifyExpiration(RefreshToken token) {
		if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
			throw new TokenRefreshException(token.getToken(), "Expired token. Please issue a new request");
		}
	}

	public void deleteById(String id) {
		refreshTokenRepository.deleteById(id);
	}

	public void increaseCount(RefreshToken refreshToken) {
		refreshToken.setRefreshCount(refreshToken.getRefreshCount() + 1);
		save(refreshToken);
	}
}
