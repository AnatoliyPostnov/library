package com.postnov.library.serviceTest;

import com.postnov.library.applicationLibrary;
import com.postnov.library.model.*;
import com.postnov.library.service.AuthorService;
import com.postnov.library.service.BookService;
import com.postnov.library.service.LibraryCardService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = applicationLibrary.class)
public class TestLibraryCardService {

    private GenericApplicationContext ctx;
    private LibraryCardService libraryCardService;
    private BookService bookService;
    private AuthorService authorService;

    @Before
    public void init(){
        ctx = new AnnotationConfigApplicationContext(applicationLibrary.class);
        libraryCardService = ctx.getBean(LibraryCardService.class);
        bookService = ctx.getBean(BookService.class);
        authorService = ctx.getBean(AuthorService.class);
        saveThreeLibraryCards();
        saveBookWithThreeAuthors();
        saveAuthorWithThreeBooks();
    }

    @Test
    public void saveLibraryCardTest(){
        assertEquals(3, libraryCardService.findAll().size());
    }

    @Test
    public void deleteLibraryCardTest(){
        Passport passport1 = new Passport(
                "Федор",
                "Бондарчук",
                new Date(1960, 06, 05),
                "1234",
                "12233445",
                "Piter",
                new Date(1990, 05,05)
        );
        Client client1 = new Client("89533576500", "postnov-90@mail.ru", passport1);
        LibraryCard libraryCard1 = new LibraryCard(client1);
        libraryCardService.delete(libraryCard1);
        assertEquals(2, libraryCardService.findAll().size());
        libraryCardService.save(libraryCard1);
        assertEquals(3, libraryCardService.findAll().size());
    }

    private void saveThreeLibraryCards(){
        Passport passport1 = new Passport(
                "Федор",
                "Бондарчук",
                new Date(1960, 06, 05),
                "1234",
                "12233445",
                "Piter",
                new Date(1990, 05,05)
        );

        Passport passport2 = new Passport(
                "Петя",
                "Бубликов",
                new Date(1913, 05, 05),
                "4567",
                "1553445",
                "Piter",
                new Date(1990, 05,05)
        );

        Passport passport3 = new Passport(
                "Елена",
                "Кузьмина",
                new Date(1993, 02, 13),
                "4204",
                "1029384",
                "Москва",
                new Date(1990, 06,05)
        );
        Client client1 = new Client("89533576500", "postnov-90@mail.ru", passport1);
        Client client2 = new Client("89217554804", "lomidze111@mail.ru", passport2);
        Client client3 = new Client("89644465891", "email@e,ail.ru", passport3);
        LibraryCard libraryCard1 = new LibraryCard(client1);
        LibraryCard libraryCard2 = new LibraryCard(client2);
        LibraryCard libraryCard3 = new LibraryCard(client3);
        libraryCardService.save(libraryCard1);
        libraryCardService.save(libraryCard2);
        libraryCardService.save(libraryCard3);
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
}
