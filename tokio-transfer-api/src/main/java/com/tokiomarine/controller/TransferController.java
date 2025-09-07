package com.tokiomarine.controller;

import com.tokiomarine.dto.TransferRequestDTO;
import com.tokiomarine.dto.TransferResponseDTO;
import com.tokiomarine.service.TransferService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/transfers")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping
    public ResponseEntity<TransferResponseDTO> create(@RequestBody @Valid TransferRequestDTO body) {
        TransferResponseDTO created = transferService.create(body);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping
    public ResponseEntity<List<TransferResponseDTO>> getAllTransfers() {
        List<TransferResponseDTO> transfers = transferService.getAllTransfers();
        return ResponseEntity.ok(transfers);
    }
}
