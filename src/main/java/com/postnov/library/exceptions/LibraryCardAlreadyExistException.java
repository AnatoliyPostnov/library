package com.postnov.library.exceptions;

public class LibraryCardAlreadyExistException extends RuntimeException {
    public LibraryCardAlreadyExistException() {
        super("library card already exist");
    }
}
