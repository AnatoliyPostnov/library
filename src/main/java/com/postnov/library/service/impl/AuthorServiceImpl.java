package com.postnov.library.service.impl;

import com.postnov.library.model.Author;
import com.postnov.library.model.Book;
import com.postnov.library.repository.AuthorRepository;
import com.postnov.library.service.AuthorService;
import com.postnov.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class AuthorServiceImpl implements AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookService bookService;

    public AuthorServiceImpl(){}

    @Override
    public void saveSetAuthors(Set<Author> authors){
        for(Author author : authors) {
            save(author);
        }
    }

    @Override
    public void save(Author author) {
        if(!existenceOfTheAuthor(author)) {
            bookService.saveSetBooks(author.getBooks());
            authorRepository.save(author);
        }
        else {
            Author authorTmp = authorRepository.finedByAuthor(
                    author.getName(),
                    author.getSurname(),
                    author.getBirthday(),
                    author.getDeletedAuthor()).get();
            for(Book book : author.getBooks()) {
                authorTmp.addBook(book);
            }

            for (Book book : authorTmp.getBooks()){
                if(!bookService.existenceOfTheBook(book)){
                    bookService.save(book);
                }
            }
            authorRepository.save(authorTmp);
        }
    }

    @Override
    public Boolean existenceOfTheAuthor(Author author) {
        return authorRepository.finedByAuthor(
                author.getName(),
                author.getSurname(),
                author.getBirthday(),
                author.getDeletedAuthor()).isPresent();
    }

    @Override
    public List<Author> findAll() {
        List<Author> authors = new ArrayList<>();
        for (Author author : authorRepository.findAll()){
            if (author.getDeletedAuthor()){
                authors.add(author);
            }
        }
        return authors;
    }

    @Override
    public Author finedById(Long id) {
        Author author = authorRepository.findById(id).orElse(null);
        if (author == null) {
            throw new RuntimeException("Author with id: " + id + " is not exist");
        }
        else if(author.getDeletedAuthor()){
            return author;
        }
        throw new RuntimeException("Author with id: " + id + " is deleted");
    }

    @Override
    public void delete(Author author) {
        Author deletedAuthor = authorRepository.finedByAuthor(
                author.getName(),
                author.getSurname(),
                author.getBirthday(),
                author.getDeletedAuthor()
        ).orElse(null);

        if(deletedAuthor == null){
            throw new RuntimeException(author.toString() + " is not exist");
        }

        deletedAuthor.setDeletedAuthor(false);
        Set<Book> books = deletedAuthor.getBooks();
        for (Book book : books) {
            book.setDeletedBook(false);
            Set<Author> authors = book.getAuthors();
            for (Author authorCurrentBook: authors){
                Set<Book> currentBooks = authorCurrentBook.getBooks();
                if (currentBooks.size() == 1) {
                    authorCurrentBook.setDeletedAuthor(false);
                }
            }
        }
    }
}
