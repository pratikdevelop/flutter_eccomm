package com.example.invoiceapp.model;

import lombok.Data;

@Data
public class SuccessResponse {
    private String message;
    private Object data;

    public SuccessResponse(String message) {
        this.message = message;
    }

    public SuccessResponse(String message, Object data) {
        this.message = message;
        this.data = data;
    }
}