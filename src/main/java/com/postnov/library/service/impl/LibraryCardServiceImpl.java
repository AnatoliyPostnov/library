package com.postnov.library.service.impl;

import com.postnov.library.model.LibraryCard;
import com.postnov.library.model.Passport;
import com.postnov.library.repository.LibraryCardRepository;
import com.postnov.library.service.ClientService;
import com.postnov.library.service.LibraryCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LibraryCardServiceImpl implements LibraryCardService {

    @Autowired
    LibraryCardRepository libraryCardRepository;

    @Autowired
    ClientService clientService;

    @Override
    public void save(LibraryCard libraryCard) {
        if(!existenceOfTheLibraryCard(libraryCard)){
            clientService.save(libraryCard.getClient());
            libraryCardRepository.save(libraryCard);
        }
    }

    @Override
    public void delete(LibraryCard libraryCard) {
        if (clientService.existenceOfTheClient(libraryCard.getClient())){
            Optional<LibraryCard> deletedLibraryCard = libraryCardRepository.findByClientId(
                    clientService.findByClient(libraryCard.getClient()).getId()
            );
            clientService.delete(libraryCard.getClient());
            deletedLibraryCard.orElse(null).setDeletedLibraryCard(false);
        }
    }

    @Override
    public Boolean existenceOfTheLibraryCard(LibraryCard libraryCard) {
        return clientService.existenceOfTheClient(libraryCard.getClient());
    }

    @Override
    public Optional<LibraryCard> findById(Long id) {
        Optional<LibraryCard> optionalLibraryCard = libraryCardRepository.findById(id);
        if (optionalLibraryCard.isPresent() && optionalLibraryCard.orElse(null).getDeletedLibraryCard()){
            return optionalLibraryCard;
        }
        return null;
    }

    @Override
    public List<LibraryCard> findAll() {
        List<LibraryCard> libraryCards = new ArrayList<>();
        for(LibraryCard libraryCard : libraryCardRepository.findAll()){
            if (libraryCard.getDeletedLibraryCard()){
                libraryCards.add(libraryCard);
            }
        }
        return libraryCards;
    }

    @Override
    public LibraryCard getLibraryCard(Passport passport){
        LibraryCard libraryCard = libraryCardRepository
                .findByClientId(
                        clientService.findByPassport(passport).getId()).orElse(null);
        if (libraryCard == null){
            throw new RuntimeException(
                    "libraryCard for client with passport: "
                            + passport.toString()
                            + " is not exist");
        }
        return libraryCard;
    }
}
