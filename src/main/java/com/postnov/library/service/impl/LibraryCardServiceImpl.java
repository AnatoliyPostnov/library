package com.postnov.library.service.impl;

import com.postnov.library.dto.LibraryCardDto;
import com.postnov.library.exceptions.IncorrectSavedClientFormatException;
import com.postnov.library.exceptions.IncorrectSavedLibraryCardFormatException;
import com.postnov.library.exceptions.LibraryCardAlreadyExistException;
import com.postnov.library.model.Client;
import com.postnov.library.model.LibraryCard;
import com.postnov.library.model.Passport;
import com.postnov.library.repository.LibraryCardRepository;
import com.postnov.library.service.ClientService;
import com.postnov.library.service.LibraryCardService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LibraryCardServiceImpl implements LibraryCardService {

    private final LibraryCardRepository libraryCardRepository;

    private final ClientService clientService;

    private final ModelMapper modelMapper;

    public LibraryCardServiceImpl(LibraryCardRepository libraryCardRepository, ClientService clientService, ModelMapper modelMapper) {
        this.libraryCardRepository = libraryCardRepository;
        this.clientService = clientService;
        this.modelMapper = modelMapper;
    }

    @Override
    public void save(LibraryCard libraryCard) {
        if(!existenceOfTheLibraryCard(libraryCard)){
            libraryCardRepository.save(libraryCard);
        }else{
            throw new LibraryCardAlreadyExistException();
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
        if(libraryCard.getClient() == null){
            throw new IncorrectSavedLibraryCardFormatException();
        }
        if (libraryCard.getClient().getPassport() == null){
            throw new IncorrectSavedClientFormatException();
        }
        return clientService.existenceOfTheClient(libraryCard.getClient());
    }

    @Override
    public LibraryCard findById(Long id) {
        Optional<LibraryCard> optionalLibraryCard = libraryCardRepository.findById(id);
        if(!optionalLibraryCard.isPresent()){
            throw new RuntimeException(
                    "libraryCard with id: "
                            + id
                            + " is not exist");
        }else if (optionalLibraryCard.orElse(null).getDeletedLibraryCard()){
            return optionalLibraryCard.orElse(null);
        }
        throw new RuntimeException("library card with id: " + id + "is deleted");
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

    @Override
    public List<LibraryCardDto> convertToLibraryCardDto(List<LibraryCard> libraryCards) {
        List<LibraryCardDto> libraryCardsDto = new ArrayList<>();
        for(LibraryCard libraryCard : libraryCards){
            libraryCardsDto.add(modelMapper.map(libraryCard, LibraryCardDto.class));
        }
        return libraryCardsDto;
    }

    @Override
    public LibraryCard findByPassportSNumberAndSeries(String number, String series) {
        return findByClient(clientService.findByPassportSNumberAndSeries(number, series));
    }

    @Override
    public LibraryCard findByClient(Client client) {
        Optional<LibraryCard> optionalLibraryCard = libraryCardRepository.findByClientId(client.getId());
        if(optionalLibraryCard.isPresent()){
            return libraryCardRepository.findByClientId(client.getId()).orElse(null);
        }
        throw new RuntimeException("Library card with client id: " + client.getId() + " is not exist");
    }
}
