package com.postnov.library.exceptions;

public class BookAlreadyExistException extends RuntimeException {
    public BookAlreadyExistException(String message){
        super("Book already exist");
    }
}
