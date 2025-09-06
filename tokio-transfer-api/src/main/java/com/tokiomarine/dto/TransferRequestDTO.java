package com.tokiomarine.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class TransferRequestDTO {

    @NotBlank(message = "Conta de origem é obrigatória.")
    @Pattern(regexp = "\\d{10}", message = "Conta de origem deve ter exatamente 10 dígitos.")
    private String sourceAccount;

    @NotBlank(message = "Conta de destino é obrigatória.")
    @Pattern(regexp = "\\d{10}", message = "Conta de destino deve ter exatamente 10 dígitos.")
    private String destinationAccount;

    @NotNull(message = "Valor é obrigatório.")
    @DecimalMin(value = "0.00", inclusive = false, message = "O valor deve ser maior que 0.")
    private BigDecimal amount;

    @NotNull(message = "Data da transferência é obrigatória.")
    private LocalDate transferDate;
}