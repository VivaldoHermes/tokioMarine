package com.tokiomarine.service.fee;

import com.tokiomarine.exception.NoApplicableFeeException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class DefaultFeeCalculator implements FeeCalculator {

    private static final BigDecimal FIXED_SAME_DAY = new BigDecimal("3.00");
    private static final BigDecimal FIXED_1_TO_10  = new BigDecimal("12.00");

    private static final BigDecimal PCT_0D   = new BigDecimal("0.025");
    private static final BigDecimal PCT_11_20 = new BigDecimal("0.082");
    private static final BigDecimal PCT_21_30 = new BigDecimal("0.069");
    private static final BigDecimal PCT_31_40 = new BigDecimal("0.047");
    private static final BigDecimal PCT_41_50 = new BigDecimal("0.017");

    @Override
    public BigDecimal calculate(BigDecimal amount, int days) {
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("amount must be > 0");
        }
        if (days < 0 || days > 50) {
            throw new NoApplicableFeeException();
        }

        BigDecimal fee;
        if (days == 0) {
            fee = amount.multiply(PCT_0D).add(FIXED_SAME_DAY);
        } else if (days <= 10) {
            fee = FIXED_1_TO_10;
        } else if (days <= 20) {
            fee = amount.multiply(PCT_11_20);
        } else if (days <= 30) {
            fee = amount.multiply(PCT_21_30);
        } else if (days <= 40) {
            fee = amount.multiply(PCT_31_40);
        } else { // 41..50
            fee = amount.multiply(PCT_41_50);
        }

        return fee.setScale(2, RoundingMode.HALF_UP);
    }
}
