package com.postnov.library.serviceTest;

import com.postnov.library.applicationLibrary;
import com.postnov.library.model.Author;
import com.postnov.library.model.Book;
import com.postnov.library.service.AuthorService;
import com.postnov.library.service.BookService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = applicationLibrary.class)
public class TestAuthorService {

    private static Logger logger = LoggerFactory.getLogger(TestBookService.class);
    private GenericApplicationContext ctx;
    private AuthorService authorService;
    private BookService bookService;

    @Before
    public void init(){
        ctx = new AnnotationConfigApplicationContext(applicationLibrary.class);
        authorService = ctx.getBean(AuthorService.class);
        bookService = ctx.getBean(BookService.class);
        saveAuthorWithThreeBooks();
        saveBookWithThreeAuthors();
    }

    @Test
    public void saveAuthorTest(){
        List<Author> authors = authorService.findAll();
        assertEquals(3, authors.size());
        listAuthorWithBooks(authors);
    }

    @Test
    public void findByIdAuthorTest(){
        Author author = authorService.finedById(4L);
        assertEquals(true, author != null);
    }

    @Test
    public void deleteBookTest(){
        Author author = new Author("Снежана", "Михайловна", new Date(15, 07, 1970));

        authorService.delete(author);
        List<Book> books = bookService.findAll();
        List<Author> authors = authorService.findAll();
        assertEquals(0, books.size());
        assertEquals(0, authors.size());
    }

    private void saveAuthorWithThreeBooks(){
        Author author = new Author("Снежана", "Михайловна", new Date(15, 07, 1970));

        Book book1 = new Book("Английский для чайников",
                300, new Date(5, 8, 2019),
                new Date(1,7, 2011));
        Book book2 = new Book("Английский для среднего уровня",
                456, new Date(5, 3, 2019),
                new Date(1,2, 2013));
        Book book3 = new Book("Английский для продвинутых",
                634, new Date(5, 3, 2019),
                new Date(7,2, 2013));

        Set<Book> books = new HashSet<>();
        books.add(book1);
        books.add(book2);
        books.add(book3);

        author.setBooks(books);
        authorService.save(author);

        listAuthorWithBooks(authorService.findAll());
    }

    private void saveBookWithThreeAuthors(){
        Book book = new Book("Английский для чайников",
                300, new Date(5, 8, 2019),
                new Date(1,7, 2011));

        Author author1 = new Author("Снежана", "Михайловна", new Date(15, 07, 1970));
        Author author2 = new Author("Роб", "Харроп", new Date(15, 06, 1964));
        Author author3 = new Author("Крис", "Шефер", new Date(15, 06, 1964));

        Set<Author> authors = new HashSet<>();
        authors.add(author1);
        authors.add(author2);
        authors.add(author3);

        book.setAuthors(authors);
        bookService.save(book);

        listAuthorWithBooks(authorService.findAll());
    }

    private void listAuthorWithBooks(List<Author> authors){
        logger.info("----------Listing authors with books-------------");
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
