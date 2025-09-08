package com.tokiomarine.dto;

import com.tokiomarine.model.enums.TransferStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@AllArgsConstructor
@lombok.Builder
public class TransferResponseDTO {
    private String sourceAccount;
    private String destinationAccount;
    private BigDecimal amount;
    private BigDecimal fee;
    private LocalDate transferDate;
    private LocalDate scheduledDate;
    private TransferStatus status;
}