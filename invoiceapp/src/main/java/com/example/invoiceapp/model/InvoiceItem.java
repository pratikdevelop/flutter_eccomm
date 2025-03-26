package com.example.invoiceapp.model;

import lombok.Data;

@Data
public class InvoiceItem {
    private Product product;
    private int quantity;
    private double price;
}