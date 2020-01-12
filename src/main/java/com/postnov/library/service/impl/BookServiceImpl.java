package com.postnov.library.service.impl;

import com.postnov.library.dto.BookDto;
import com.postnov.library.exceptions.BookAlreadyExistException;
import com.postnov.library.exceptions.IncorrectSavedBookFormatException;
import com.postnov.library.model.Author;
import com.postnov.library.model.Book;
import com.postnov.library.repository.BookRepository;
import com.postnov.library.service.AuthorService;
import com.postnov.library.service.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final AuthorService authorService;

    private final ModelMapper modelMapper;

    public BookServiceImpl(BookRepository bookRepository, AuthorService authorService, ModelMapper modelMapper){
        this.bookRepository = bookRepository;
        this.authorService = authorService;
        this.modelMapper = modelMapper;
    }

    @Override
    public void save(Book book) {
        if(!existenceOfTheBook(book) && !book.getAuthors().isEmpty()){
            bookRepository.save(book);
        }else if (book.getAuthors().isEmpty()){
            throw new IncorrectSavedBookFormatException();
        }else{
            throw new BookAlreadyExistException();
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
            author.setDeletedAuthor(false);
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
    public Boolean getIsReceivedBook(Book book){
        Book receivedBook = findByBook(book);
        return receivedBook.getReceivedBook();
    }

    @Override
    public List<Book> getIsReceivedBooks() {
        List<Book> allBooks = this.findAll();
        List<Book> receivedBooks = new ArrayList<>();
        for (Book book : allBooks){
            if (!this.getIsReceivedBook(book)){
                receivedBooks.add(book);
            }
        }
        return receivedBooks;
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

    @Override
    public List<BookDto> convertToListBooksDto(List<Book> books) {
        List<BookDto> booksDto = new ArrayList<>();
        for (Book book : books){
            booksDto.add(modelMapper.map(book, BookDto.class));
        }
        return booksDto;
    }

    @Override
    public List<Book> convertToListBooks(List<BookDto> booksDto) {
        List<Book> books = new ArrayList<>();
        for (BookDto bookDto : booksDto){
            books.add(modelMapper.map(bookDto, Book.class));
        }
        return books;
    }

    @Override
    public List<Book> findBooksByAuthors(List<Author> authors) {
        List<Book> books = new ArrayList<>();
        for (Author authorTmp : authors){
            books.addAll(authorTmp.getBooks());
        }
        return books;
    }

    @Override
    public List<Book> findBooksByAuthor(Author author) {
        return findBooksByAuthors(authorService.findAuthorByAuthor(author));
    }

    @Override
    public List<Book> findBooksByAuthorSNameAndSurname(String name, String surname) {
        return findBooksByAuthors(authorService.findAuthorsByAuthorSNameAndSurname(name,surname));
    }

    @Override
    public List<Book> findBooksByBookSName(String name) {
        return bookRepository.findBooksByBookSName(name);
    }
}
