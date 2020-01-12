package com.postnov.library.controller;

import com.postnov.library.dto.AuthorDto;
import com.postnov.library.dto.BookDto;
import com.postnov.library.model.Author;
import com.postnov.library.model.Book;
import com.postnov.library.service.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping
public class BookController {

    private final BookService bookService;

    private final ModelMapper modelMapper;

    public BookController(BookService bookService,
                          ModelMapper modelMapper) {
        this.bookService = bookService;
        this.modelMapper = modelMapper;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/get/books")
    public List<BookDto> getBooks() {
        List<Book> books = bookService.findAll();
        return bookService.convertToListBooksDto(books);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/get/received/books")
    public List<BookDto> getReceivedBook(){
        List<Book> books = bookService.getIsReceivedBooks();
        return bookService.convertToListBooksDto(books);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/add/book")
    public void addBook(@RequestBody BookDto bookDto) {
        Book book = modelMapper.map(bookDto, Book.class);
        List<Book> books = new ArrayList<>();
        books.add(book);
        bookService.save(book);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/add/books")
    public void addBooks(@RequestBody List<BookDto> bookDto) {
        List<Book> books = bookService.convertToListBooks(bookDto);

        for(Book book: books) {
            bookService.save(book);
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/delete/book")
    public void deletedBook(@RequestBody BookDto bookDto){
        Book book = modelMapper.map(bookDto, Book.class);
        bookService.delete(book);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/find/books/by/author")
    public List<BookDto> findBooksByAuthor(@RequestBody AuthorDto authorDto){
        Author author = modelMapper.map(authorDto, Author.class);
        return bookService.convertToListBooksDto(bookService.findBooksByAuthor(author));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/find/books/by/author/name/and/surname")
    public List<BookDto> findBooksByAuthorNameAndSurname(@RequestParam String name,
                                                         @RequestParam String surname){
        return bookService.convertToListBooksDto(bookService.findBooksByAuthorSNameAndSurname(name, surname));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/find/books/by/books/name")
    public List<BookDto> findBooksByBookSName(@RequestParam String name){
        return bookService.convertToListBooksDto(bookService.findBooksByBookSName(name));
    }
}
