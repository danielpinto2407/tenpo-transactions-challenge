package com.tenpo.transactions.infrastructure.adapters.api.controller.dto;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class TransactionRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidTransactionRequest() {
        TransactionRequest request = new TransactionRequest(
            5000,
            "Supermercado ABC",
            "Juan Perez",
            LocalDateTime.of(2024, 12, 1, 14, 30, 0)
        );

        Set<ConstraintViolation<TransactionRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty(), "Valid transaction request should have no violations");
    }

    @Test
    void testAmountNotNull() {
        TransactionRequest request = new TransactionRequest(null, "Business", "Name", LocalDateTime.now());
        Set<ConstraintViolation<TransactionRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty(), "Amount should not be null");
    }

    @Test
    void testAmountMinimum() {
        TransactionRequest request = new TransactionRequest(-100, "Business", "Name", LocalDateTime.now());
        Set<ConstraintViolation<TransactionRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty(), "Amount should be >= 0");
    }

    @Test
    void testAmountZero() {
        TransactionRequest request = new TransactionRequest(0, "Business", "Name", LocalDateTime.now());
        Set<ConstraintViolation<TransactionRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty(), "Amount 0 should be valid");
    }

    @Test
    void testNullBusinessIsValid() {
        TransactionRequest request = new TransactionRequest(1000, null, "Name", LocalDateTime.now());
        Set<ConstraintViolation<TransactionRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty(), "Business can be null");
    }

    @Test
    void testNullTenpistaNameIsValid() {
        TransactionRequest request = new TransactionRequest(1000, "Business", null, LocalDateTime.now());
        Set<ConstraintViolation<TransactionRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty(), "Tenpista name can be null");
    }

    @Test
    void testNullTransactionDateIsValid() {
        TransactionRequest request = new TransactionRequest(1000, "Business", "Name", null);
        Set<ConstraintViolation<TransactionRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty(), "Transaction date can be null");
    }
}