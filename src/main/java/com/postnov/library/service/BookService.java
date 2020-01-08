package com.postnov.library.service;

import com.postnov.library.model.Book;

import java.util.List;
import java.util.Set;

public interface BookService {

    void saveSetBooks(Set<Book> books);

    void save(Book book);

    Boolean existenceOfTheBook(Book book);

    List<Book> findAll();

    Book findById(Long id);

    void delete(Book book);

    Book findByBook(Book book);

    void returnBook(Book book);

    Boolean getReceivedBook(Book receivedBook);

    void receivedBook(Book book);
}
