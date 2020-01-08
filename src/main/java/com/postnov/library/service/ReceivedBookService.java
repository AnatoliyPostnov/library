package com.postnov.library.service;

import com.postnov.library.model.Book;
import com.postnov.library.model.Passport;
import com.postnov.library.model.ReceivedBook;

import java.util.List;

public interface ReceivedBookService {
    void save(ReceivedBook receivedBook);

    Boolean existenceOfTheReceivedBook(ReceivedBook receivedBook);

    ReceivedBook getReceivedBook(ReceivedBook inputReceivedBook);

    Boolean receivedBook(Passport passport, Book book);

    Boolean returnBook(Passport passport, Book book);

    List<ReceivedBook> findAll();
}
