package com.postnov.library.service.impl;

import com.postnov.library.model.Book;
import com.postnov.library.model.LibraryCard;
import com.postnov.library.model.Passport;
import com.postnov.library.model.ReceivedBook;
import com.postnov.library.repository.ReceivedBookRepository;
import com.postnov.library.service.BookService;
import com.postnov.library.service.LibraryCardService;
import com.postnov.library.service.ReceivedBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@EnableScheduling
public class ReceivedBookServiceImpl implements ReceivedBookService {

    @Autowired
    BookService bookService;

    @Autowired
    ReceivedBookService receivedBookService;

    @Autowired
    LibraryCardService libraryCardService;

    @Autowired
    ReceivedBookRepository receivedBookRepository;

    @Override
    public void save(ReceivedBook receivedBook) {
        if(!existenceOfTheReceivedBook(receivedBook)){
            receivedBookRepository.save(receivedBook);
        }
    }

    @Override
    public Boolean existenceOfTheReceivedBook(ReceivedBook receivedBook) {
        return receivedBookRepository.findByReceivedBook(
                receivedBook.getBook().getId(),
                receivedBook.getLibraryCard().getId()
        ).isPresent();
    }

    @Override
    public ReceivedBook getReceivedBook(ReceivedBook inputReceivedBook){
        ReceivedBook receivedBook = receivedBookRepository.findByReceivedBook(
                inputReceivedBook.getBook().getId(),
                inputReceivedBook.getLibraryCard().getId()).orElse(null);
        if (receivedBook == null){
            throw new RuntimeException("ReceivedBook: " + receivedBook.toString() + " is not exist");
        }
        return receivedBook;
    }

    @Override
    public List<ReceivedBook> findAll() {
        List<ReceivedBook> receivedBooks = new ArrayList<>();
        for(ReceivedBook receivedBook : receivedBookRepository.findAll()){
            if(receivedBook.getDateOfBookReturn() == null){
                receivedBooks.add(receivedBook);
            }
        }
        return receivedBooks;
    }

    @Override
    public Boolean receivedBook(LibraryCard libraryCard, Book book) {
        if(bookService.getReceivedBook(book)){

            Book receivedBook = bookService.findByBook(book);
            bookService.receivedBook(book);

            ReceivedBook objectReceivedBook = new ReceivedBook();

            objectReceivedBook.setBook(receivedBook);
            objectReceivedBook.setLibraryCard(libraryCard);
            objectReceivedBook.setDateOfBookReceiving(new Date());

            receivedBookRepository.save(objectReceivedBook);

            return true;
        }
        return false;
    }

    @Override
    public Boolean returnBook(LibraryCard libraryCard, Book book) {
        if(!bookService.getReceivedBook(book)) {

            Book receivedBook = bookService.findByBook(book);
            ReceivedBook objectReceivedBook = getReceivedBook(
                    new ReceivedBook(receivedBook, libraryCard));

            bookService.returnBook(book);
            objectReceivedBook.setDateOfBookReturn(new Date());
            return true;
        }
        return false;
    }

    @Scheduled(fixedRate = 10000)
    public void executeTask1() {
        System.out.println("Scheduled work");
    }

}
