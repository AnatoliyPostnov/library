package com.postnov.library.controller;

import com.postnov.library.dto.BookDto;
import com.postnov.library.dto.ErrorDto;
import com.postnov.library.exceptions.BookAlreadyExistException;
import com.postnov.library.model.Author;
import com.postnov.library.model.Book;
import com.postnov.library.service.BookService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/book")
public class BookController {

    private static Logger logger = LoggerFactory.getLogger(BookController.class);

    @Autowired
    private BookService bookService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public List<BookDto> getBooks() {
        List<Book> books = bookService.findAll();
        List<BookDto> booksDto = convertToListDto(books);
        return booksDto;
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping
    public void addBook(@RequestBody BookDto bookDto) {
        Book book = modelMapper.map(bookDto, Book.class);
        List<Book> books = new ArrayList<>();
        books.add(book);
        listBooksWithAuthor(books);
        bookService.save(book);
    }

    private List<BookDto> convertToListDto(List<Book> books) {
        List<BookDto> booksDto = new ArrayList<>();
        for (Book book : books){
            booksDto.add(modelMapper.map(book, BookDto.class));
        }
        return booksDto;
    }

    private Book convertToEntity(BookDto bookDto) {
        return modelMapper.map(bookDto, Book.class);
    }

    @ExceptionHandler(BookAlreadyExistException.class)
    public ErrorDto BookAlreadyExistExceptionHandler(BookAlreadyExistException ex) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setNameError(ex.getClass().getName());
        errorDto.setMessageError(ex.getMessage());
        return errorDto;
    }

    private void listBooksWithAuthor(List<Book> books) {
        logger.info("----------Listing books with authors-------------");
        for (Book book : books) {
            logger.info(book.toString());
            if(book.getAuthors() != null){
                for(Author author : book.getAuthors()){
                    logger.info("\t" + author.toString());
                }
            }
        }
    }
}
