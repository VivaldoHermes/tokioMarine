package com.tokiomarine.controller;

import com.tokiomarine.dto.TransferRequestDTO;
import com.tokiomarine.dto.TransferResponseDTO;
import com.tokiomarine.service.TransferService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/transfers")
@Tag(name = "Transfers", description = "Endpoints para criação e consulta de transferências")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping
    @Operation(summary = "Cria uma transferência")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Criado"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida")
    })
    public ResponseEntity<TransferResponseDTO> create(@RequestBody @Valid TransferRequestDTO body) {
        TransferResponseDTO created = transferService.create(body);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping
    @Operation(summary = "Lista todas as transferências")
    public ResponseEntity<List<TransferResponseDTO>> getAllTransfers() {
        List<TransferResponseDTO> transfers = transferService.getAllTransfers();
        return ResponseEntity.ok(transfers);
    }
}
