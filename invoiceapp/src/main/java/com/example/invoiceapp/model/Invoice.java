package com.example.invoiceapp.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "invoices")
public class Invoice {
    @Id
    private String id;
    private String invoiceNumber;
    private Customer customer;
    private List<InvoiceItem> items;
    private double totalAmount;
    private String status = "PENDING"; // PENDING, COMPLETED
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}