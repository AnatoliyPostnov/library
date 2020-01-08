package com.postnov.library.service;

import com.postnov.library.model.Author;

import java.util.List;
import java.util.Set;

public interface AuthorService {

    void saveSetAuthors(Set<Author> authors);

    void save(Author author);

    Boolean existenceOfTheAuthor(Author author);

    List<Author> findAll();

    Author finedById(Long id);

    void delete(Author author);
}
