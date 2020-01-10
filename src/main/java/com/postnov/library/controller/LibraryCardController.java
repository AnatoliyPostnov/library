package com.postnov.library.controller;

import com.postnov.library.dto.LibraryCardDto;
import com.postnov.library.model.LibraryCard;
import com.postnov.library.service.ClientService;
import com.postnov.library.service.LibraryCardService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class LibraryCardController {

    private static Logger logger = LoggerFactory.getLogger(BookController.class);

    private final LibraryCardService libraryCardService;

    private final ModelMapper modelMapper;

    public LibraryCardController(LibraryCardService libraryCardService,
                                 ClientService clientService, ModelMapper modelMapper) {
        this.libraryCardService = libraryCardService;
        this.modelMapper = modelMapper;
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/add/libraryCard")
    public void addLibraryCard(@RequestBody LibraryCardDto libraryCardDto){
        LibraryCard libraryCard = modelMapper.map(libraryCardDto, LibraryCard.class);
        libraryCardService.save(libraryCard);
        logger.info(libraryCard.toString());
    }

    @GetMapping("/get/libraryCards")
    public List<LibraryCardDto> getLibraryCards(){
        List<LibraryCard> libraryCards = libraryCardService.findAll();
        return libraryCardService.convertToLibraryCardDto(libraryCards);
    }

    @GetMapping("/get/libraryCard")
    public LibraryCardDto getLibraryCard(@RequestParam String number, @RequestParam String series){
        LibraryCard libraryCard = libraryCardService.findByNumberAndSeries(number, series);
        return modelMapper.map(libraryCard, LibraryCardDto.class);
    }
}
