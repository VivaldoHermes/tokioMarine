package com.tokiomarine.service;

import com.tokiomarine.dto.TransferRequestDTO;
import com.tokiomarine.dto.TransferResponseDTO;
import com.tokiomarine.exception.NoApplicableFeeException;
import com.tokiomarine.exception.TransferDateBeforeTodayException;
import com.tokiomarine.model.Transfer;
import com.tokiomarine.model.enums.TransferStatus;
import com.tokiomarine.repository.TransferRepository;
import com.tokiomarine.service.fee.FeeCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransferServiceTest {

    @Mock
    private TransferRepository repository;

    @Mock
    private FeeCalculator feeCalculator;

    @InjectMocks
    private TransferService service;

    private final ZoneId ZONE = ZoneId.of("America/Sao_Paulo");

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_whenDateBeforeToday_throws() {
        LocalDate today = LocalDate.now(ZONE);
        TransferRequestDTO req = new TransferRequestDTO();
        req.setSourceAccount("1234567890");
        req.setDestinationAccount("0987654321");
        req.setAmount(new BigDecimal("100.00"));
        req.setTransferDate(today.minusDays(1));

        assertThrows(TransferDateBeforeTodayException.class, () -> service.create(req));
        verifyNoInteractions(feeCalculator, repository);
    }

    @Test
    void create_whenValid_callsCalculatorWithCorrectDays_andSaves() {
        LocalDate today = LocalDate.now(ZONE);
        int D = 11;
        LocalDate transferDate = today.plusDays(D);

        TransferRequestDTO req = new TransferRequestDTO();
        req.setSourceAccount("1234567890");
        req.setDestinationAccount("0987654321");
        req.setAmount(new BigDecimal("1000.00"));
        req.setTransferDate(transferDate);

        BigDecimal expectedFee = new BigDecimal("82.00");
        when(feeCalculator.calculate(new BigDecimal("1000.00"), D)).thenReturn(expectedFee);


        Transfer saved = Transfer.builder()
                .sourceAccount(req.getSourceAccount())
                .destinationAccount(req.getDestinationAccount())
                .amount(new BigDecimal("1000.00"))
                .fee(expectedFee)
                .transferDate(transferDate)
                .scheduledDate(today)        // define aqui
                .status(TransferStatus.SCHEDULED)
                .build();

        try {
            var idField = Transfer.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(saved, UUID.randomUUID());
        } catch (Exception ignored) {}

        when(repository.save(ArgumentMatchers.any(Transfer.class))).thenReturn(saved);

        TransferResponseDTO resp = service.create(req);

        verify(feeCalculator).calculate(new BigDecimal("1000.00"), D);
        verify(repository).save(ArgumentMatchers.any(Transfer.class));
        
        assertEquals("1234567890", resp.getSourceAccount());
        assertEquals("0987654321", resp.getDestinationAccount());
        assertEquals(new BigDecimal("1000.00"), resp.getAmount());
        assertEquals(expectedFee, resp.getFee());
        assertEquals(transferDate, resp.getTransferDate());
        assertEquals(today, resp.getScheduledDate()); // se VO: resp.getScheduledDate().equals(today)
        assertEquals(TransferStatus.SCHEDULED, resp.getStatus());
    }

    @Test
    void create_whenNoApplicableFee_throws() {
        LocalDate today = LocalDate.now(ZONE);
        int D = 51;
        LocalDate transferDate = today.plusDays(D);

        TransferRequestDTO req = new TransferRequestDTO();
        req.setSourceAccount("1234567890");
        req.setDestinationAccount("0987654321");
        req.setAmount(new BigDecimal("100.00"));
        req.setTransferDate(transferDate);

        when(feeCalculator.calculate(new BigDecimal("100.00"), D)).thenThrow(new NoApplicableFeeException());

        assertThrows(NoApplicableFeeException.class, () -> service.create(req));
        verify(repository, never()).save(any());
    }

    @Test
    void getAllTransfers_returnsMappedList() {
        Transfer t = Transfer.builder()
                .sourceAccount("1111111111")
                .destinationAccount("2222222222")
                .amount(new BigDecimal("10.00"))
                .fee(new BigDecimal("3.00"))
                .transferDate(LocalDate.now(ZONE).plusDays(2))
                .scheduledDate(LocalDate.now(ZONE))
                .status(TransferStatus.SCHEDULED)
                .build();

        try {
            var idField = Transfer.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(t, UUID.randomUUID());
        } catch (Exception ignored) {}

        when(repository.findAll()).thenReturn(java.util.List.of(t));

        var list = service.getAllTransfers();

        assertEquals(1, list.size());
        var dto = list.get(0);
        assertEquals("1111111111", dto.getSourceAccount());
        assertEquals("2222222222", dto.getDestinationAccount());
        assertEquals(new BigDecimal("10.00"), dto.getAmount());
        assertEquals(new BigDecimal("3.00"), dto.getFee());
        assertEquals(TransferStatus.SCHEDULED, dto.getStatus());
        verify(repository).findAll();
    }

}
