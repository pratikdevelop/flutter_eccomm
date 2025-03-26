package com.example.invoiceapp.service; // Corrected package

import com.example.invoiceapp.model.Invoice;
import com.example.invoiceapp.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    public Page<Invoice> getInvoices(Pageable pageable, String search, String category) {
        if (search != null && !search.isEmpty()) {
            return invoiceRepository.findBySearch(search, pageable);
        } else if (category != null && !category.isEmpty()) {
            return invoiceRepository.findByCategoryId(category, pageable);
        }
        return invoiceRepository.findAll(pageable);
    }

    public Invoice createInvoice(Invoice invoice) {
        invoice.setInvoiceNumber(UUID.randomUUID().toString().substring(0, 8));
        invoice.setCreatedAt(LocalDateTime.now());
        invoice.setUpdatedAt(LocalDateTime.now());
        invoice.setTotalAmount(calculateTotal(invoice));
        return invoiceRepository.save(invoice);
    }

    public void markAsComplete(String id) {
        Invoice invoice = invoiceRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Invoice not found"));
        if (!"PENDING".equals(invoice.getStatus())) {
            throw new RuntimeException("Invoice already completed");
        }
        invoice.setStatus("COMPLETED");
        invoice.setUpdatedAt(LocalDateTime.now());
        invoiceRepository.save(invoice);
    }

    public void deleteInvoice(String id) {
        Invoice invoice = invoiceRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Invoice not found"));
        if (!"PENDING".equals(invoice.getStatus())) {
            throw new RuntimeException("Only pending invoices can be deleted");
        }
        invoiceRepository.delete(invoice);
    }

    private double calculateTotal(Invoice invoice) {
        return invoice.getItems().stream()
            .mapToDouble(item -> item.getPrice() * item.getQuantity())
            .sum();
    }
}