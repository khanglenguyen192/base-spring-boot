package com.example.base.repository.mongo;

import com.example.base.entities.mongo.SampleMongoEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SampleMongoRepository extends MongoRepository<SampleMongoEntity, String> {
}
