package com.postnov.library.controllerTest;

import com.postnov.library.applicationLibrary;
import com.postnov.library.dto.AuthorDto;
import com.postnov.library.dto.BookDto;
import com.postnov.library.model.Author;
import com.postnov.library.model.Book;
import com.postnov.library.service.BookService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static com.postnov.library.controllerTest.TestSData.*;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = applicationLibrary.class)
public class BookAndAuthorControllerTest {

    private MockMvc mvc;

    @Autowired
    private BookService bookService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void init() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        addBooksInDbFromCreateFunc("/add/book", "createBookWithTreeAuthors");
        addBooksInDbFromCreateFunc("/add/books", "createSomeBooksInDb");
    }

    @After
    public void destroy() throws Exception {
        if (bookService.findAll().size() < 4) {
            addBooksInDbFromCreateFunc("/add/book", "createBookWithTreeAuthors");
        }
    }

    @Test()
    public void addBookTest() {
        List<Book> books = bookService.findAll();

        Assertions.assertEquals(4, books.size());
    }

    @Test
    public void addBooksTest() throws Exception {
        String uri = "/get/books";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_UTF8)).andReturn();

        assertBooks(mvcResult);
    }

    @Test
    public void getBooksTest() throws Exception {
        String uri = "/get/books";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_UTF8)).andReturn();

        assertBooks(mvcResult);
    }

    @Test
    public void deleteBookTest() throws Exception {
        String uri = "/delete/book";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapToJson(createBookWithTreeAuthors()))).andReturn();

        int status = mvcResult.getResponse().getStatus();

        assertEquals(204, status);
    }

    @Test
    public void findBooksByAuthorTest() throws Exception {
        String uri = "/find/books/by/author";
        AuthorDto author = new AuthorDto();
        author.setName("Неизвестный");
        author.setSurname("Автор");
        author.setBirthday(new Date(1999 - 1900, 6, 17));
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .content(mapToJson(author))).andReturn();

        assertBooks(mvcResult);
    }

    @Test
    public void findBooksByAuthorNameAndSurnameTest() throws Exception {
        String uri = "/find/books/by/author/name/and/surname?name=Неизвестный&surname=Автор";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_UTF8)).andReturn();

        assertBooks(mvcResult);
    }

    @Test
    public void findBooksByBookSName() throws Exception {
        String uri = "/find/books/by/books/name?name=Spring 5 для профессионалов";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_UTF8)).andReturn();

        assertBooks(mvcResult);
    }

    @Test
    public void getAuthorsTest() throws Exception {
        String uri = "/get/authors";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_UTF8)).andReturn();

        assertAuthors(mvcResult);
    }

    @Test
    public void deletedAuthorTest() throws Exception {
        String uri = "/delete/author";
        AuthorDto author = new AuthorDto();
        author.setName("Юлиана");
        author.setSurname("Кузьмина");
        author.setBirthday(new Date(1964 - 1900, 6, 15));
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapToJson(author))).andReturn();
        int status = mvcResult.getResponse().getStatus();

        assertEquals(204, status);
    }

    @Test
    public void getReceivedBooksTest() throws Exception {
        String uri = "/get/received/books";
        List<Book> book = bookService.findBooksByBookSName("Spring 5 для профессионалов");
        Iterator<Book> iter = book.iterator();
        iter.next().setReceivedBook(false);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_UTF8)).andReturn();

        assertBooks(mvcResult);
    }

    private void assertAuthors(MvcResult mvcResult) throws IOException {

        assertStatus(mvcResult);

        String[] contents = countContents(mvcResult);

        List<AuthorDto> authorsDtoTest = new ArrayList<>(
                createBookWithTreeAuthors().getAuthors());
        List<BookDto> booksDto = createSomeBooksInDb();

        for (BookDto bookDto : booksDto) {
            authorsDtoTest.addAll(bookDto.getAuthors());
        }

        List<Author> authorsTest = new ArrayList<>();
        for (AuthorDto authorDto : authorsDtoTest) {
            authorsTest.add(modelMapper.map(authorDto, Author.class));
        }

        for (int i = 0; i < contents.length - 1; ++i) {
            AuthorDto authorDto = mapFromJson(contents[i], AuthorDto.class);
            Author author = modelMapper.map(authorDto, Author.class);
            assert (authorsTest.contains(author));
        }

    }

    private void assertBooks(MvcResult mvcResult) throws IOException {

        assertStatus(mvcResult);

        String[] contents = countContents(mvcResult);

        List<BookDto> booksDtoTest = new ArrayList<>();
        booksDtoTest.add(createBookWithTreeAuthors());
        booksDtoTest.addAll(createSomeBooksInDb());

        List<Book> booksTest = new ArrayList<>();
        for (BookDto bookDto : booksDtoTest) {
            booksTest.add(modelMapper.map(bookDto, Book.class));
        }

        for (int i = 0; i < contents.length - 1; ++i) {
            BookDto bookDto = mapFromJson(contents[i], BookDto.class);
            Book book = modelMapper.map(bookDto, Book.class);
            assert (booksTest.contains(book));
        }
    }

    private void addBooksInDbFromCreateFunc(String uri, String funcName) throws Exception {
        if (funcName.equals("createBookWithTreeAuthors")) {
            List<BookDto> booksDto = new ArrayList<>();
            booksDto.add(createBookWithTreeAuthors());
            addBooksInDb(uri, booksDto);
        } else if (funcName.equals("createSomeBooksInDb")) {
            addBooksInDb(uri, createSomeBooksInDb());
        } else {
            throw new RuntimeException("Func: " + funcName + " is not exist");
        }
    }

    private void addBooksInDb(String uri, List<BookDto> booksDto) throws Exception {
        StringBuilder bookDtoJsonBuilder = new StringBuilder();
        String bookDtoJson;
        for (BookDto bookDto : booksDto) {
            bookDtoJsonBuilder.append(mapToJson(bookDto));
            bookDtoJsonBuilder.append(',');
        }
        if (booksDto.size() > 1) {
            bookDtoJson = "["
                    + bookDtoJsonBuilder.toString()
                    .substring(
                            0,
                            bookDtoJsonBuilder.length() - 1
                    )
                    + "]";
        } else {
            bookDtoJson = bookDtoJsonBuilder.toString();
        }
        mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(bookDtoJson)).andReturn();
    }
}
