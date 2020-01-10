package com.postnov.library.exceptions;

public class IncorrectSavedBookFormatException extends RuntimeException {
    public IncorrectSavedBookFormatException(){
        super("The saved book does not have a single author");
    }
}
