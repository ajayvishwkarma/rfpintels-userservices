package com.rfpintels.userservices.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.rfpintels.userservices.model.Subscription;

@Repository
public interface SubscriptionRepository extends MongoRepository<Subscription, String> {

	Optional<Subscription> findOneByemailAndStatus(String email, String status);
	
	Subscription findByEmail(String email);
	
	List<Subscription> findByStatusAndPlanName(String status, String planName);
}