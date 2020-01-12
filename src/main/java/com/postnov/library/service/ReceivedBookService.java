package com.postnov.library.service;

import com.postnov.library.dto.ReceivedBookDto;
import com.postnov.library.model.Book;
import com.postnov.library.model.LibraryCard;
import com.postnov.library.model.Passport;
import com.postnov.library.model.ReceivedBook;

import java.util.List;

public interface ReceivedBookService {

    void save(ReceivedBook receivedBook);

    Boolean existenceOfTheReceivedBook(ReceivedBook receivedBook);

    ReceivedBook getReceivedBookByReceivedBook(ReceivedBook inputReceivedBook);

    ReceivedBook getReceivedBookByLibraryCardAndBook(LibraryCard libraryCard, Book book);

    List<ReceivedBook> getReceivedBooksByLibraryCard(LibraryCard libraryCard);

    void receivedBook(LibraryCard libraryCard, Book book);

    void returnBook( LibraryCard libraryCard, Book book);

    List<ReceivedBook> findAll();

    Integer returnBooksFromLibraryCard(LibraryCard libraryCard, List<Book> books);

    List<ReceivedBookDto> convertToListReceivedBooksDto(List<ReceivedBook> receivedBook);

    List<ReceivedBook> getHistoryReceivedBooksByLibraryCard(LibraryCard libraryCard);

    List<ReceivedBook> getAllReceivedBook();
}
