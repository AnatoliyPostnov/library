package com.postnov.library.serviceTest;

import com.postnov.library.applicationLibrary;
import com.postnov.library.model.Author;
import com.postnov.library.model.Book;
import com.postnov.library.service.AuthorService;
import com.postnov.library.service.BookService;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = applicationLibrary.class)
public class TestBookService {
    private static Logger logger = LoggerFactory.getLogger(TestBookService.class);
    private GenericApplicationContext ctx;
    private BookService bookService;
    private AuthorService authorService;

    @Before
    public void init(){
        ctx = new AnnotationConfigApplicationContext(applicationLibrary.class);
        bookService = ctx.getBean(BookService.class);
        authorService = ctx.getBean(AuthorService.class);
        saveBookWithThreeAuthors();
//        saveAuthorWithThreeBooks();
    }

    @Test
    public void saveBookTest(){
        List<Book> books = bookService.findAll();
        assertEquals(3, books.size());
        listBooksWithAuthor(books);
    }

    @Test
    public void findByIdTest(){
        Book book = bookService.findById(4L);
        assertEquals(true, book != null);
    }

    @Test
    public void deleteBookTest(){
        Book book = new Book("Spring 5 для профессионалов",
                1120, new Date(5, 11, 2019),
                new Date(5,5, 2018));

        bookService.delete(book);
        List<Book> books = bookService.findAll();
        List<Author> authors = authorService.findAll();
        assertEquals(2, books.size());
        assertEquals(1, authors.size());
    }

    public void saveBookWithThreeAuthors(){
        Book book = new Book("Spring 5 для профессионалов",
                1120, new Date(5, 11, 2019),
                new Date(5,5, 2018));

        Author author1 = new Author("Юлиана", "Кузьмина", new Date(15, 06, 1964));
        Author author2 = new Author("Роб", "Харроп", new Date(15, 06, 1964));
        Author author3 = new Author("Крис", "Шефер", new Date(15, 06, 1964));

        Set<Author> authors = new HashSet<>();
        authors.add(author1);
        authors.add(author2);
        authors.add(author3);

        book.setAuthors(authors);
        bookService.save(book);
    }

    public void saveAuthorWithThreeBooks(){
        Author author = new Author("Юлиана", "Кузьмина", new Date(15, 06, 1964));

        Book book1 = new Book("Spring 5 для профессионалов",
                1120, new Date(5, 11, 2019),
                new Date(5,5, 2018));

        Book book2 = new Book("Хорошо в деревне летом",
                154, new Date(13, 4, 2016),
                new Date(5,5, 2012));

        Book book3 = new Book("Как полюбить программирование",
                154, new Date(11, 4, 2011),
                new Date(5,5, 2010));

        Set<Book> books = new HashSet<>();

        books.add(book1);
        books.add(book2);
        books.add(book3);

        author.setBooks(books);
        authorService.save(author);
    }

    private void listBooksWithAuthor(List<Book> books) {
        logger.info("----------Listing books with authors-------------");
        for (Book book : books) {
            logger.info(book.toString());
            if(book.getAuthors() != null){
                for(Author author : book.getAuthors()){
                    logger.info("\t" + author.toString());
                }
            }
        }
    }
}
