package com.postnov.library.controller;

import com.postnov.library.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@ControllerAdvice
@RequestMapping
public class ExceptionController {

    @ExceptionHandler({
            ClientAlreadyExistException.class,
            IncorrectSavedClientFormatException.class,
            BookAlreadyExistException.class,
            IncorrectSavedBookFormatException.class,
            LibraryCardAlreadyExistException.class,
            IncorrectSavedLibraryCardFormatException.class,
            PassportAlreadyExistException.class
    })
    protected ResponseEntity<RuntimeException> applicationException(RuntimeException ex) {
        return new ResponseEntity<>(ex, HttpStatus.BAD_REQUEST);
    }

}
