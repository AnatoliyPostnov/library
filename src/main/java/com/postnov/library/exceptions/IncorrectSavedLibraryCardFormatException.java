package com.postnov.library.exceptions;

public class IncorrectSavedLibraryCardFormatException extends RuntimeException {
    public IncorrectSavedLibraryCardFormatException(){
        super("The saved library card does not have a client");
    }
}
