package com.postnov.library.controller;

import com.postnov.library.dto.BookDto;
import com.postnov.library.dto.ReceivedBookDto;
import com.postnov.library.model.Book;
import com.postnov.library.model.LibraryCard;
import com.postnov.library.service.BookService;
import com.postnov.library.service.LibraryCardService;
import com.postnov.library.service.ReceivedBookService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class ReceivedBookController {

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
    @PostMapping("/received/book/by/book")
    public void receivedBookByBook(
            @RequestParam String number,
            @RequestParam String series,
            @RequestBody BookDto bookDto
    ){
        Book book = bookService.findByBook(modelMapper.map(bookDto, Book.class));
        LibraryCard libraryCard = libraryCardService.findByPassportSNumberAndSeries(number, series);
        receivedBookService.receivedBook(libraryCard, book);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/return/books/by/book/name")
    public Integer returnBooksByBookName(
            @RequestParam String number,
            @RequestParam String series,
            @RequestParam String name){
        LibraryCard libraryCard = libraryCardService.findByPassportSNumberAndSeries(number, series);
        List<Book> books = bookService.findBooksByBookSName(name);
        return receivedBookService.returnBooksFromLibraryCard(libraryCard, books);
    }


    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/get/received/books/by/passportS/number/and/series")
    public List<ReceivedBookDto> getReceivedBooksByPassportSNumberAndSeries(
            @RequestParam String number,
            @RequestParam String series){
        LibraryCard libraryCard = libraryCardService.findByPassportSNumberAndSeries(number, series);
        return receivedBookService.convertToListReceivedBooksDto(
                receivedBookService.getReceivedBooksByLibraryCard(libraryCard));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/get/history/received/books/by/passportS/number/and/series")
    public List<ReceivedBookDto> getHistoryReceivedBooksByPassportSNumberAndSeries(
            @RequestParam String number,
            @RequestParam String series){
        LibraryCard libraryCard = libraryCardService.findByPassportSNumberAndSeries(number, series);
        return receivedBookService.convertToListReceivedBooksDto(
                receivedBookService.getHistoryReceivedBooksByLibraryCard(libraryCard));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/get/all/received/books")
    public List<ReceivedBookDto> getAllReceivedBooks(){
        return receivedBookService.convertToListReceivedBooksDto(
          receivedBookService.getAllReceivedBook()
        );
    }

}
