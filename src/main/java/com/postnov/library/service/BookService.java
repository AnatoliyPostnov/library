package com.postnov.library.service;

import com.postnov.library.dto.BookDto;
import com.postnov.library.model.Author;
import com.postnov.library.model.Book;

import java.util.List;
import java.util.Set;

public interface BookService {

    void save(Book book);

    Boolean existenceOfTheBook(Book book);

    List<Book> findAll();

    Book findById(Long id);

    void delete(Book book);

    Book findByBook(Book book);

    void returnBook(Book book);

    Boolean getIsReceivedBook(Book receivedBook);

    List<Book> getIsReceivedBooks();

    void receivedBook(Book book);

    List<Book> findBooksByAuthor(Author author);

    List<Book> findBooksByAuthors(List<Author> authors);

    List<Book> findBooksByAuthorSNameAndSurname(String name, String surname);

    List<Book> findBooksByBookSName(String name);

    List<BookDto> convertToListBooksDto(List<Book> books);

    List<Book> convertToListBooks(List<BookDto> booksDto);

}
