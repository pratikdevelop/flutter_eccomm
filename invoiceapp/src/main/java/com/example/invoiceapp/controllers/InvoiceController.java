package com.example.invoiceapp.controllers;

import com.example.invoiceapp.model.ErrorResponse;
import com.example.invoiceapp.model.Invoice;
import com.example.invoiceapp.model.SuccessResponse;
import com.example.invoiceapp.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @GetMapping
    public ResponseEntity<?> getInvoices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String category) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Invoice> invoices = invoiceService.getInvoices(pageable, search, category);
            return ResponseEntity.ok(new SuccessResponse("Invoices retrieved", invoices));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createInvoice(@RequestBody Invoice invoice) {
        try {
            Invoice saved = invoiceService.createInvoice(invoice);
            return ResponseEntity.ok(new SuccessResponse("Invoice created", saved));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<?> markAsComplete(@PathVariable String id) {
        try {
            invoiceService.markAsComplete(id);
            return ResponseEntity.ok(new SuccessResponse("Invoice marked as complete"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteInvoice(@PathVariable String id) {
        try {
            invoiceService.deleteInvoice(id);
            return ResponseEntity.ok(new SuccessResponse("Invoice deleted"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }
}