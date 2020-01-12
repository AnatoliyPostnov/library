package com.postnov.library.controller;

import com.postnov.library.dto.LibraryCardDto;
import com.postnov.library.model.LibraryCard;
import com.postnov.library.service.LibraryCardService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class LibraryCardController {

    private final LibraryCardService libraryCardService;

    private final ModelMapper modelMapper;

    public LibraryCardController(LibraryCardService libraryCardService, ModelMapper modelMapper) {
        this.libraryCardService = libraryCardService;
        this.modelMapper = modelMapper;
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/add/libraryCard")
    public void addLibraryCard(@RequestBody LibraryCardDto libraryCardDto){
        LibraryCard libraryCard = modelMapper.map(libraryCardDto, LibraryCard.class);
        libraryCardService.save(libraryCard);
    }

    @GetMapping("/get/libraryCards")
    public List<LibraryCardDto> getLibraryCards(){
        List<LibraryCard> libraryCards = libraryCardService.findAll();
        return libraryCardService.convertToLibraryCardDto(libraryCards);
    }

    @GetMapping("/get/libraryCard")
    public LibraryCardDto getLibraryCard(@RequestParam String number, @RequestParam String series){
        LibraryCard libraryCard = libraryCardService.findByPassportSNumberAndSeries(number, series);
        return modelMapper.map(libraryCard, LibraryCardDto.class);
    }
}
