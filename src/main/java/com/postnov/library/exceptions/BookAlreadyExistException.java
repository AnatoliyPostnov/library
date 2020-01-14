package com.postnov.library.exceptions;

public class BookAlreadyExistException extends RuntimeException {
    public BookAlreadyExistException() {
        super("Book already exist");
    }
}
