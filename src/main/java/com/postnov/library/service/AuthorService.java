package com.postnov.library.service;

import com.postnov.library.dto.AuthorDto;
import com.postnov.library.model.Author;

import java.util.List;

public interface AuthorService {

    List<Author> findAll();

    Author finedById(Long id);

    void delete(Author author);

    List<Author> findAuthorByAuthor(Author author);

    List<Author> findAuthorsByAuthorSNameAndSurname(String name, String surname);

    List<AuthorDto> convertToListDto(List<Author> authors);

}
