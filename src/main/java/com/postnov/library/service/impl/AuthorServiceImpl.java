package com.postnov.library.service.impl;

import com.postnov.library.dto.AuthorDto;
import com.postnov.library.model.Author;
import com.postnov.library.model.Book;
import com.postnov.library.repository.AuthorRepository;
import com.postnov.library.service.AuthorService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    private final ModelMapper modelMapper;

    public AuthorServiceImpl(AuthorRepository authorRepository, ModelMapper modelMapper){
        this.authorRepository = authorRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<Author> findAll() {
        List<Author> authors = new ArrayList<>();
        for (Author author : authorRepository.findAll()){
            if (author.getDeletedAuthor() &&
                    existenceOfTheAuthor(authors, author)){
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
        List<Author> deletedAuthors = authorRepository.findByAuthor(
                author.getName(),
                author.getSurname(),
                author.getBirthday(),
                author.getDeletedAuthor()
        );

        if(deletedAuthors.isEmpty()){
            throw new RuntimeException(author.toString() + " is not exist");
        }

        for (Author deletedAuthor : deletedAuthors){
            deletedAuthor.setDeletedAuthor(false);
            Set<Book> books = deletedAuthor.getBooks();
            for (Book book : books){
                book.setDeletedBook(false);
            }
        }
    }

    @Override
    public List<Author> findAuthorByAuthor(Author author) {
        return authorRepository.findByAuthor(
                author.getName(),
                author.getSurname(),
                author.getBirthday(),
                author.getDeletedAuthor());
    }

    @Override
    public List<Author> findAuthorsByAuthorSNameAndSurname(String name, String surname) {
        return authorRepository.findAuthorsByAuthorSNameAndSurname(
                name,
                surname);
    }

    @Override
    public List<AuthorDto> convertToListDto(List<Author> authors) {
        List<AuthorDto> authorsDto = new ArrayList<>();
        for (Author author : authors){
            authorsDto.add(modelMapper.map(author, AuthorDto.class));
        }
        return authorsDto;
    }

    private Boolean existenceOfTheAuthor(List<Author> authors, Author author){
        for(Author authorTmp : authors){
            if (authorTmp.equals(author)){
                return false;
            }
        }
        return true;
    }

}
