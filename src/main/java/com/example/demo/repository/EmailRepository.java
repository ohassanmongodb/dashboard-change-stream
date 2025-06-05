package com.example.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.demo.model.Email;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepository extends MongoRepository<Email, String> {
    long countByStatus(String status);
    long countByStatusIs(String status);
    long countByUrgentTrue();
}