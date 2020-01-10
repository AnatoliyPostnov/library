package com.postnov.library.controller;

import com.postnov.library.dto.BookDto;
import com.postnov.library.model.Book;
import com.postnov.library.model.LibraryCard;
import com.postnov.library.service.BookService;
import com.postnov.library.service.LibraryCardService;
import com.postnov.library.service.ReceivedBookService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class ReceivedBookController {

    private static Logger logger = LoggerFactory.getLogger(AuthorController.class);

    private final BookService bookService;

    private final LibraryCardService libraryCardService;

    private final ReceivedBookService receivedBookService;

    private final ModelMapper modelMapper;

    public ReceivedBookController(BookService bookService,
                                  LibraryCardService libraryCardService,
                                  ReceivedBookService receivedBookService,
                                  ModelMapper modelMapper) {
        this.bookService = bookService;
        this.libraryCardService = libraryCardService;
        this.receivedBookService = receivedBookService;
        this.modelMapper = modelMapper;
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/received/book")
    public Boolean receivedBookByBook(
            @RequestParam String number,
            @RequestParam String series,
            @RequestBody BookDto bookDto
    ){
        Book book = bookService.findByBook(modelMapper.map(bookDto, Book.class));
        LibraryCard libraryCard = libraryCardService.findByNumberAndSeries(number, series);
        return receivedBookService.receivedBook(libraryCard, book);
    }
}
