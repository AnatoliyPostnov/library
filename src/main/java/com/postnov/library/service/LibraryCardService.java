package com.postnov.library.service;

import com.postnov.library.dto.LibraryCardDto;
import com.postnov.library.model.Client;
import com.postnov.library.model.LibraryCard;
import com.postnov.library.model.Passport;

import java.util.List;

public interface LibraryCardService {

    void save(LibraryCard libraryCard);

    void delete(LibraryCard libraryCard);

    Boolean existenceOfTheLibraryCard(LibraryCard libraryCard);

    LibraryCard findById(Long id);

    List<LibraryCard> findAll();

    LibraryCard getLibraryCard(Passport passport);

    List<LibraryCardDto> convertToLibraryCardDto(List<LibraryCard> libraryCards);

    LibraryCard findByPassportSNumberAndSeries(String number, String series);

    LibraryCard findByClient(Client clint);
}
