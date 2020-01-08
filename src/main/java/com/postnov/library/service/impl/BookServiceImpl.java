package com.postnov.library.service.impl;

import com.postnov.library.exceptions.BookAlreadyExistException;
import com.postnov.library.model.Author;
import com.postnov.library.model.Book;
import com.postnov.library.repository.BookRepository;
import com.postnov.library.service.AuthorService;
import com.postnov.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorService authorService;

    public BookServiceImpl(){}

    @Override
    public void saveSetBooks(Set<Book> books) {
        for(Book book: books){
            save(book);
        }
    }

    @Override
    public void save(Book book) {
        if(!existenceOfTheBook(book)){
            authorService.saveSetAuthors(book.getAuthors());
            bookRepository.save(book);
        } else{
            Book bookTmp = bookRepository.finedByBook(
                    book.getName(),
                    book.getVolume(),
                    book.getDateOfPublishing(),
                    book.getDateOfWriting(),
                    book.getRating(),
                    book.getDeletedBook()
            ).get();
            for (Author author : book.getAuthors()){
                bookTmp.addAuthor(author);
            }
//            try {
//                for (Author author : bookTmp.getAuthors()) {
//                    if (!authorService.existenceOfTheAuthor(author)) {
//                        authorService.save(author);
//                    }
//                }
//                bookRepository.save(bookTmp);
//            }catch (IncorrectResultSizeDataAccessException ex){
//                throw new BookAlreadyExistException(ex.getMessage());
//            }
        }
    }

    @Override
    public Boolean existenceOfTheBook(Book book) {
        return bookRepository.finedByBook(book.getName(),
                book.getVolume(),
                book.getDateOfPublishing(),
                book.getDateOfWriting(),
                book.getRating(),
                book.getDeletedBook()
        ).isPresent();
    }

    @Override
    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();
        for(Book book : bookRepository.findAll()){
            if (book.getDeletedBook()){
                books.add(book);
            }
        }
        return books;
    }

    @Override
    public Book findById(Long id) {
        Book book = bookRepository.findById(id).orElse(null);
        if(book == null){
            throw new RuntimeException("book with id: " + id + "is not exist");
        }else if(book.getDeletedBook()){
            return book;
        }
        throw new RuntimeException("book with id: " + id + "is deleted");
    }

    @Override
    public void delete(Book book) {
        Book deletedBook = findByBook(book);
        findByBook(book).setDeletedBook(false);
        Set<Author> authors = deletedBook.getAuthors();
        for (Author author : authors) {
            Set<Book> books = author.getBooks();
            if (books.size() == 1) {
                author.setDeletedAuthor(false);
            }
        }
    }

    @Override
    public Book findByBook(Book book) {
        Book resultBook = bookRepository.finedByBook(
                book.getName(),
                book.getVolume(),
                book.getDateOfPublishing(),
                book.getDateOfWriting(),
                book.getRating(),
                book.getDeletedBook()
        ).orElse(null);
        if (resultBook == null){
            throw new RuntimeException(book.toString() + " is not exist");
        }
        return resultBook;
    }

    @Override
    public Boolean getReceivedBook(Book book){
        Book receivedBook = findByBook(book);
        return receivedBook.getReceivedBook();
    }

    @Override
    public void receivedBook(Book book){
        Book receivedBook = findByBook(book);
        receivedBook.setReceivedBook(false);
    }

    @Override
    public void returnBook(Book book){
        Book returnBook = findByBook(book);
        returnBook.setReceivedBook(true);
    }
}
