package com.rfpintels.userservices.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.rfpintels.userservices.model.PaymentCharge;

@Repository
public interface PaymentChargeRepository extends MongoRepository<PaymentCharge, String>{

}
