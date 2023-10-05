package com.rfpintels.userservices.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.rfpintels.userservices.model.PersonalDetails;

@Repository
public interface PersonalDetailsRepository extends MongoRepository<PersonalDetails, String> {

}
