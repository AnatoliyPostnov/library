package com.postnov.library.exceptions;

public class IncorrectSavedClientFormatException extends RuntimeException {
    public IncorrectSavedClientFormatException() {
        super("The saved client does not have a passport");
    }
}
