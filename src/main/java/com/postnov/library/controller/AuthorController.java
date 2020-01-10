package com.postnov.library.controller;

import com.postnov.library.dto.AuthorDto;
import com.postnov.library.model.Author;
import com.postnov.library.model.Book;
import com.postnov.library.service.AuthorService;
import com.postnov.library.service.BookService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class AuthorController {

    private static Logger logger = LoggerFactory.getLogger(AuthorController.class);

    private final AuthorService authorService;

    private final BookService bookService;

    private final ModelMapper modelMapper;

    public AuthorController(AuthorService authorService,
                            BookService bookService,
                            ModelMapper modelMapper) {
        this.authorService = authorService;
        this.bookService = bookService;
        this.modelMapper = modelMapper;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/get/authors")
    public List<AuthorDto> getAuthors(){
        List<Author> authors = authorService.findAll();
        listAuthorsWithBook(authors);
        return authorService.convertToListDto(authors);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/delete/author")
    public void deletedAuthor(@RequestBody AuthorDto authorDto){
        Author author = modelMapper.map(authorDto, Author.class);
        authorService.delete(author);
        listAuthorsWithBook(authorService.findAll());
    }

    private void listAuthorsWithBook(List<Author> authors) {
        logger.info("----------Listing books with authors-------------");
        for (Author author : authors) {
            logger.info(author.toString());
            List<Book> books = bookService.findBooksByAuthor(author);
            for(Book book : books){
                logger.info("\t" + book.toString());
            }
        }
    }
}
