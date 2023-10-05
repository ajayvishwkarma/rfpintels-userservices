package com.rfpintels.userservices.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.rfpintels.userservices.model.EmailVerificationToken;

public interface EmailVerificationTokenRepository extends MongoRepository<EmailVerificationToken,String>{

	Optional<EmailVerificationToken> findByToken(String token);
}
