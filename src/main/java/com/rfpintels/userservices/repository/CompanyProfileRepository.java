package com.rfpintels.userservices.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.rfpintels.userservices.model.CompanyProfile;

@Repository
public interface CompanyProfileRepository extends MongoRepository<CompanyProfile, String> {

	CompanyProfile findByCompanyName(String companyName);

}