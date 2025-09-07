package com.tokiomarine.service.fee;

import java.math.BigDecimal;

public interface FeeCalculator {

    BigDecimal calculate(BigDecimal amount, int days);
}
