package com.rfpintels.userservices.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.rfpintels.userservices.model.Contact;

@Repository
public interface ContactDetailsRepository extends MongoRepository<Contact, String> {

}
