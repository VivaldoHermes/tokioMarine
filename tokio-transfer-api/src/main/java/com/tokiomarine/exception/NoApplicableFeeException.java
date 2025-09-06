package com.tokiomarine.exception;

public class NoApplicableFeeException extends RuntimeException {
    public NoApplicableFeeException() {
        super("Sem taxa aplicável para a data informada.");
    }
}
