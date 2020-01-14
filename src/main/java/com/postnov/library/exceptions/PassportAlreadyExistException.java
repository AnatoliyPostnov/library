package com.postnov.library.exceptions;

public class PassportAlreadyExistException extends RuntimeException {
    public PassportAlreadyExistException() {
        super("Passport already exist");
    }
}
