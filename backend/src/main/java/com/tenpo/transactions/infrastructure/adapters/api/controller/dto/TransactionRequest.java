package com.tenpo.transactions.infrastructure.adapters.api.controller.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

@Schema(description = "Solicitud para crear una transacción")
public record TransactionRequest(

        @Schema(description = "Monto de la transacción", example = "12000")
        @NotNull @Min(0) Integer amount,

        @Schema(description = "Comercio o giro", example = "Supermercado ABC")
        @Size(max = 255) String business,

        @Schema(description = "Nombre del Tenpista", example = "Juan Perez")
        @Size(max = 255) String tenpistaName,

        @Schema(description = "Fecha de la transacción", example = "2024-12-01T14:30:00")
        @PastOrPresent LocalDateTime transactionDate
) {}


