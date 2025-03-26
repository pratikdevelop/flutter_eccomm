package com.example.invoiceapp.repository;

import com.example.invoiceapp.model.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface InvoiceRepository extends MongoRepository<Invoice, String> {
    @Query("{$or: [{'invoiceNumber': {$regex: ?0, $options: 'i'}}, " +
            "{'customer.name': {$regex: ?0, $options: 'i'}}, " +
            "{'customer.address': {$regex: ?0, $options: 'i'}}, " +
            "{'totalAmount': ?0}]}")
    Page<Invoice> findBySearch(String search, Pageable pageable);

    @Query("{'items.product.categoryId': ?0}")
    Page<Invoice> findByCategoryId(String categoryId, Pageable pageable);
}