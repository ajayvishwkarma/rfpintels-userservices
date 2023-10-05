package com.rfpintels.userservices.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.rfpintels.userservices.model.PastPerformence;

@Repository
public interface PastPerformenceRepository extends MongoRepository<PastPerformence, String> {
	
	@Query(value = "{duns:?0}")
	public PastPerformence findByDuns(String Duns);

}