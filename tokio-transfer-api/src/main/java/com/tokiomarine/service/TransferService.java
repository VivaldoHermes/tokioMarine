package com.tokiomarine.service;

import com.tokiomarine.dto.TransferRequestDTO;
import com.tokiomarine.dto.TransferResponseDTO;
import com.tokiomarine.exception.TransferDateBeforeTodayException;
import com.tokiomarine.model.Transfer;
import com.tokiomarine.model.enums.TransferStatus;
import com.tokiomarine.repository.TransferRepository;
import com.tokiomarine.service.fee.FeeCalculator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransferService {

    private static final ZoneId ZONE = ZoneId.of("America/Sao_Paulo");

    private final TransferRepository transferRepository;
    private final FeeCalculator feeCalculator;

    public TransferService(TransferRepository transferRepository, FeeCalculator feeCalculator) {
        this.transferRepository = transferRepository;
        this.feeCalculator = feeCalculator;
    }

    @Transactional
    public TransferResponseDTO create(TransferRequestDTO req) {
        LocalDate today = LocalDate.now(ZONE);
        LocalDate transferDate = req.getTransferDate();
        if (transferDate.isBefore(today)) {
            throw new TransferDateBeforeTodayException();
        }

        int days = (int) ChronoUnit.DAYS.between(today, transferDate);

        BigDecimal fee = feeCalculator.calculate(req.getAmount(), days);

        Transfer t = new Transfer();
        t.setSourceAccount(req.getSourceAccount());
        t.setDestinationAccount(req.getDestinationAccount());
        t.setAmount(req.getAmount().setScale(2, RoundingMode.HALF_UP));
        t.setFee(fee);
        t.setTransferDate(transferDate);
        t.setStatus(TransferStatus.SCHEDULED);

        Transfer saved = transferRepository.save(t);

        return new TransferResponseDTO(
                saved.getSourceAccount(),
                saved.getDestinationAccount(),
                saved.getAmount(),
                saved.getFee(),
                saved.getTransferDate(),
                saved.getScheduledDate(),
                saved.getStatus()
        );
    }

    @Transactional(readOnly = true)
    public List<TransferResponseDTO> getAllTransfers() {
        return transferRepository.findAll()
                .stream()
                .map(t -> new TransferResponseDTO(
                        t.getSourceAccount(),
                        t.getDestinationAccount(),
                        t.getAmount(),
                        t.getFee(),
                        t.getTransferDate(),
                        t.getScheduledDate(),
                        t.getStatus()
                ))
                .collect(Collectors.toList());
    }
}
