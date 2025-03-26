package com.example.invoiceapp.repository;

import com.example.invoiceapp.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryRepository extends MongoRepository<Category, String> {
}