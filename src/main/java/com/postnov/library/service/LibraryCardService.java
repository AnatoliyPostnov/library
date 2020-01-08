package com.postnov.library.service;
import com.postnov.library.model.LibraryCard;
import com.postnov.library.model.Passport;

import java.util.List;
import java.util.Optional;

public interface LibraryCardService {

    void save(LibraryCard libraryCard);

    void delete(LibraryCard libraryCard);

    Boolean existenceOfTheLibraryCard(LibraryCard libraryCard);

    Optional<LibraryCard> findById(Long id);

    List<LibraryCard> findAll();

    LibraryCard getLibraryCard(Passport passport);
}
