package com.tokiomarine.model;

import com.tokiomarine.model.enums.TransferStatus;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "transfers")
public class Transfer {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Setter
    @Column(name = "source_account", nullable = false, length = 10)
    private String sourceAccount;

    @Setter
    @Column(name = "destination_account", nullable = false, length = 10)
    private String destinationAccount;

    @Setter
    @Column(name = "amount", nullable = false, precision = 18, scale = 2)
    private BigDecimal amount;

    @Setter
    @Column(name = "fee", nullable = false, precision = 18, scale = 2)
    private BigDecimal fee;

    @Setter
    @Column(name = "transfer_date", nullable = false)
    private LocalDate transferDate;

    @Column(name = "scheduled_date", nullable = false)
    private LocalDate scheduledDate;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private TransferStatus status;

    @PrePersist
    void prePersist() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
        if (this.scheduledDate == null) {
            this.scheduledDate = LocalDate.now(ZoneId.of("America/Sao_Paulo"));
        }
        if (this.status == null) {
            this.status = TransferStatus.SCHEDULED;
        }
    }
}
