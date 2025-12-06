package com.tenpo.transactions.infrastructure.adapters.api.controller.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Solicitud para crear una transacción")
public record TransactionRequest(

        @Schema(description = "Monto de la transacción", example = "12000")
        @NotNull @Min(0) Integer amount,

        @Schema(description = "Comercio o giro", example = "Supermercado ABC")
        String business,

        @Schema(description = "Nombre del Tenpista", example = "Juan Perez")
        String tenpistaName,

        @Schema(description = "Fecha de la transacción", example = "2024-12-01T14:30:00")
        LocalDateTime transactionDate
) {}


