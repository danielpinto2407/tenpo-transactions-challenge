package com.tenpo.transactions.infrastructure.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @Mock
    private HttpServletRequest request;

    @Mock
    private MethodArgumentNotValidException methodArgumentNotValidException;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private FieldError fieldError;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        when(request.getRequestURI()).thenReturn("/test");
    }

    // -----------------------------------------------------------------------------------------
    // VALIDATION EXCEPTION
    // -----------------------------------------------------------------------------------------

    @Test
    void testHandleValidationException_WithFieldError() {
        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(java.util.List.of(fieldError));
        when(fieldError.getField()).thenReturn("amount");
        when(fieldError.getDefaultMessage()).thenReturn("must be positive");

        ResponseEntity<ApiError> response =
                handler.handleValidationException(methodArgumentNotValidException, request);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().status());
        assertTrue(response.getBody().message().contains("amount"));
    }

    @Test
    void testHandleValidationException_NoFieldError() {
        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(Collections.emptyList());

        ResponseEntity<ApiError> response =
                handler.handleValidationException(methodArgumentNotValidException, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        // Tu handler devuelve "Datos inválidos" en este caso
        assertEquals("Datos inválidos", response.getBody().message());
    }

    // -----------------------------------------------------------------------------------------
    // ILLEGAL ARGUMENT
    // -----------------------------------------------------------------------------------------

    @Test
    void testHandleIllegalArgument() {
        IllegalArgumentException ex = new IllegalArgumentException("Fecha futura no permitida");

        ResponseEntity<ApiError> response = handler.handleIllegalArgument(ex, request);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(400, response.getBody().status());
        assertEquals("Fecha futura no permitida", response.getBody().message());
    }

    // -----------------------------------------------------------------------------------------
    // CONSTRAINT VIOLATION
    // -----------------------------------------------------------------------------------------

    @Test
    void testHandleConstraintViolation_WithViolations() {
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);

        when(violation.getMessage()).thenReturn("must not be null");
        when(violation.getPropertyPath()).thenReturn(null); // tu handler concatena esto

        ConstraintViolationException ex = new ConstraintViolationException(Set.of(violation));

        ResponseEntity<ApiError> response = handler.handleConstraintViolation(ex, request);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(400, response.getBody().status());

        // Resultado real del handler: "null: must not be null"
        assertEquals("null: must not be null", response.getBody().message());
    }

    @Test
    void testHandleConstraintViolation_NoViolations() {
        ConstraintViolationException ex = new ConstraintViolationException(Set.of());

        ResponseEntity<ApiError> response = handler.handleConstraintViolation(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        // Si tu handler retorna "", aquí debe coincidir
        assertEquals("", response.getBody().message());
    }

    // -----------------------------------------------------------------------------------------
    // GENERIC EXCEPTION
    // -----------------------------------------------------------------------------------------

    @Test
    void testHandleGeneric() {
        Exception ex = new Exception("Unexpected error");

        ResponseEntity<ApiError> response = handler.handleGeneric(ex, request);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(500, response.getBody().status());
        assertEquals("Error interno del servidor", response.getBody().message());
    }

    // -----------------------------------------------------------------------------------------
    // API ERROR EXTRA CHECKS
    // -----------------------------------------------------------------------------------------

    @Test
    void testApiErrorTimestampIsSet() {
        IllegalArgumentException ex = new IllegalArgumentException("Test");

        ResponseEntity<ApiError> response = handler.handleIllegalArgument(ex, request);

        assertNotNull(response.getBody().timestamp());
    }

    @Test
    void testApiErrorRequestUriIsSet() {
        when(request.getRequestURI()).thenReturn("/transactions");
        IllegalArgumentException ex = new IllegalArgumentException("Test");

        ResponseEntity<ApiError> response = handler.handleIllegalArgument(ex, request);

        assertEquals("/transactions", response.getBody().path());
    }
}
