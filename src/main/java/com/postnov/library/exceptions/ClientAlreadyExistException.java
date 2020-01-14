package com.postnov.library.exceptions;

public class ClientAlreadyExistException extends RuntimeException {
    public ClientAlreadyExistException() {
        super("Client already exist");
    }
}
