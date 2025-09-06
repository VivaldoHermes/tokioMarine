package com.tokiomarine.exception;

public class TransferDateBeforeTodayException extends RuntimeException {
    public TransferDateBeforeTodayException() {
        super("A data da transferência deve ser hoje ou futura.");
    }
}
