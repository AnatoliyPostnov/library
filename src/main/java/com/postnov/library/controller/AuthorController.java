package com.postnov.library.controller;

import com.postnov.library.dto.AuthorDto;
import com.postnov.library.dto.ErrorDto;
import com.postnov.library.exceptions.BookAlreadyExistException;
import com.postnov.library.model.Author;
import com.postnov.library.model.Book;
import com.postnov.library.service.AuthorService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/author")
public class AuthorController {

    private static Logger logger = LoggerFactory.getLogger(AuthorController.class);

    @Autowired
    private AuthorService authorService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public List<AuthorDto> getAuthors(){
        List<Author> authors = authorService.findAll();
        List<AuthorDto> authorsDto = convertToListDto(authors);
        return authorsDto;
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping
    public void addAuthor(@RequestBody AuthorDto authorDto) {
        Author author = modelMapper.map(authorDto, Author.class);
        List<Author> authors = new ArrayList<>();
        authors.add(author);
        listBooksWithAuthor(authors);
        authorService.save(author);
    }

    private List<AuthorDto> convertToListDto(List<Author> authors) {
        List<AuthorDto> authorsDto = new ArrayList<>();
        for (Author author : authors){
            authorsDto.add(modelMapper.map(author, AuthorDto.class));
        }
        return authorsDto;
    }

    private Author convertToEntity(AuthorDto authorDto){
        return modelMapper.map(authorDto, Author.class);
    }

    @ExceptionHandler(BookAlreadyExistException.class)
    public ErrorDto BookAlreadyExistExceptionHandler(BookAlreadyExistException ex){
        ErrorDto errorDto = new ErrorDto();
        errorDto.setNameError(ex.getClass().getName());
        errorDto.setMessageError(ex.getMessage());
        return errorDto;
    }

    private void listBooksWithAuthor(List<Author> authors) {
        logger.info("----------Listing books with authors-------------");
        for (Author author : authors) {
            logger.info(author.toString());
            if(author.getBooks() != null){
                for(Book book : author.getBooks()){
                    logger.info("\t" + book.toString());
                }
            }
        }
    }
}
