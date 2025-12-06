package com.tenpo.transactions.infrastructure.adapters.api.controller;

import com.tenpo.transactions.application.service.TransactionService;
import com.tenpo.transactions.infrastructure.adapters.api.controller.dto.TransactionRequest;
import com.tenpo.transactions.infrastructure.adapters.api.controller.dto.TransactionResponse;
import com.tenpo.transactions.infrastructure.adapters.api.mapper.TransactionDtoMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.servlet.http.HttpServletResponse;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
@Tag(name = "Transactions", description = "Operaciones relacionadas con las transacciones del Tenpista")
public class TransactionController {

    private final TransactionService service;
    private final TransactionDtoMapper mapper;

    @Operation(
            summary = "Crear una nueva transacción",
            description = "Crea una transacción válida respetando las reglas de negocio.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Transacción creada correctamente",
                            content = @Content(schema =
                                    @Schema(implementation = TransactionResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Datos inválidos"
                    )
            }
    )
        @PostMapping
        public org.springframework.http.ResponseEntity<TransactionResponse> create(@Valid @RequestBody TransactionRequest request) {
                var transaction = mapper.toDomain(request);
                var created = service.create(transaction);

                java.net.URI location = org.springframework.web.util.UriComponentsBuilder
                        .fromPath("/transactions/{id}")
                        .buildAndExpand(created.id())
                        .toUri();

                return org.springframework.http.ResponseEntity.created(location).body(mapper.toResponse(created));
        }

    @Operation(
            summary = "Listar transacciones",
            description = "Devuelve todas las transacciones registradas.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Listado de transacciones",
                            content = @Content(schema =
                                    @Schema(implementation = TransactionResponse.class))
                    )
            }
    )
    @GetMapping
    public List<TransactionResponse> listAll() {
        return service.listAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }
}
