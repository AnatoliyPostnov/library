package com.postnov.library.controller;

import com.postnov.library.dto.AuthorDto;
import com.postnov.library.model.Author;
import com.postnov.library.service.AuthorService;
import com.postnov.library.service.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class AuthorController {

    private final AuthorService authorService;

    private final ModelMapper modelMapper;

    public AuthorController(AuthorService authorService,
                            ModelMapper modelMapper) {
        this.authorService = authorService;
        this.modelMapper = modelMapper;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/get/authors")
    public List<AuthorDto> getAuthors(){
        List<Author> authors = authorService.findAll();
        return authorService.convertToListDto(authors);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/delete/author")
    public void deletedAuthor(@RequestBody AuthorDto authorDto){
        Author author = modelMapper.map(authorDto, Author.class);
        authorService.delete(author);
    }
}
