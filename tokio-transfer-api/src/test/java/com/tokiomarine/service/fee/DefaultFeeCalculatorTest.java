package com.tokiomarine.service.fee;

import com.tokiomarine.exception.NoApplicableFeeException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class DefaultFeeCalculatorTest {

    private final DefaultFeeCalculator calc = new DefaultFeeCalculator();

    @Test
    void boundaries_amount1000() {
        BigDecimal amount = new BigDecimal("1000.00");

        assertEquals(new BigDecimal("28.00"), calc.calculate(amount, 0));
        assertEquals(new BigDecimal("12.00"), calc.calculate(amount, 1));
        assertEquals(new BigDecimal("12.00"), calc.calculate(amount, 10));
        assertEquals(new BigDecimal("82.00"), calc.calculate(amount, 11));
        assertEquals(new BigDecimal("82.00"), calc.calculate(amount, 20));
        assertEquals(new BigDecimal("69.00"), calc.calculate(amount, 21));
        assertEquals(new BigDecimal("69.00"), calc.calculate(amount, 30));
        assertEquals(new BigDecimal("47.00"), calc.calculate(amount, 31));
        assertEquals(new BigDecimal("47.00"), calc.calculate(amount, 40));
        assertEquals(new BigDecimal("17.00"), calc.calculate(amount, 41));
        assertEquals(new BigDecimal("17.00"), calc.calculate(amount, 50));
    }

    @Test
    void outOfRange_throws() {
        assertThrows(NoApplicableFeeException.class, () -> calc.calculate(new BigDecimal("100.00"), -1));
        assertThrows(NoApplicableFeeException.class, () -> calc.calculate(new BigDecimal("100.00"), 51));
    }

    @Test
    void rounding_halfUp_to2Decimals() {
        // D=11 usa 8.2%
        BigDecimal fee = calc.calculate(new BigDecimal("123.456"), 11);
        assertEquals(new BigDecimal("10.12"), fee);
        assertEquals(2, fee.scale());
    }
}
