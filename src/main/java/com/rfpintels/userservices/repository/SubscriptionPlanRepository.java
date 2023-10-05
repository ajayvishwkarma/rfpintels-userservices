package com.rfpintels.userservices.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.rfpintels.userservices.model.SubscriptionPlan;


@Repository
public interface SubscriptionPlanRepository extends MongoRepository<SubscriptionPlan, String> {

	SubscriptionPlan findByPlanNameAndMaximumUserAllowed(String planName, int maximumUserAllowed);
	//SubscriptionPlan findByPlanName(String type);

}
