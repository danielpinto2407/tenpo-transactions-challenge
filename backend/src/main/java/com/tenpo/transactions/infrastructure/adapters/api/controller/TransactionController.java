package com.tenpo.transactions.infrastructure.adapters.api.controller;

import com.tenpo.transactions.application.service.TransactionService;
import com.tenpo.transactions.domain.model.Transaction;
import com.tenpo.transactions.infrastructure.adapters.api.controller.dto.TransactionRequest;
import com.tenpo.transactions.infrastructure.adapters.api.controller.dto.TransactionResponse;
import com.tenpo.transactions.infrastructure.adapters.api.controller.dto.PaginatedTransactionResponse;
import com.tenpo.transactions.infrastructure.adapters.api.mapper.TransactionDtoMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<TransactionResponse> create(@Valid @RequestBody TransactionRequest request) {
        Transaction transaction = mapper.toDomain(request);
        Transaction created = service.create(transaction);

        URI location = org.springframework.web.util.UriComponentsBuilder
                .fromPath("/transactions/{id}")
                .buildAndExpand(created.id())
                .toUri();

        return ResponseEntity.created(location).body(mapper.toResponse(created));
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

    @GetMapping(params = {"page", "size"})
    public ResponseEntity<PaginatedTransactionResponse> listPaged(
            @RequestParam int page,
            @RequestParam int size) {

        Page<Transaction> result = service.list(PageRequest.of(page, size,
                org.springframework.data.domain.Sort.by("createdAt").descending()));

        List<TransactionResponse> content = result.getContent()
                .stream()
                .map(mapper::toResponse)
                .toList();

        PaginatedTransactionResponse response = new PaginatedTransactionResponse(
                content,
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages()
        );

        return ResponseEntity.ok(response);
    }
}
