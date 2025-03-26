package com.example.invoiceapp.repository;

import com.example.invoiceapp.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<Customer, String> {
}