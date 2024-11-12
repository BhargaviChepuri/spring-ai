package com.mss.demo.application;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface JokeRepository extends MongoRepository<JokeDocument, String> {
}
