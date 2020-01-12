package com.postnov.library.controllerTest;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.*;

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
        if(bookService.findAll().size() < 4){
            addBooksInDbFromCreateFunc("/add/book", "createBookWithTreeAuthors");
        }
    }

    @Test
    public void addBookTest() throws Exception {
        List<Book> books = bookService.findAll();

        Assertions.assertEquals(4, books.size());
    }

    @Test
    public void addBooksTest() throws Exception {
        String uri = "/get/books";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_UTF8)).andReturn();

        assertBooks(200, mvcResult);
    }

    @Test
    public void getBooksTest() throws Exception {
        String uri = "/get/books";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_UTF8)).andReturn();

        assertBooks(200, mvcResult);
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

        assertBooks(200, mvcResult);
    }

    @Test
    public void findBooksByAuthorNameAndSurnameTest() throws Exception {
        String uri = "/find/books/by/author/name/and/surname?name=Неизвестный&surname=Автор";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_UTF8)).andReturn();

        assertBooks(200, mvcResult);
    }

    @Test
    public void findBooksByBookSName() throws Exception {
        String uri = "/find/books/by/books/name?name=Spring 5 для профессионалов";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_UTF8)).andReturn();

        assertBooks(200, mvcResult);
    }

    @Test
    public void getAuthorsTest() throws Exception {
        String uri = "/get/authors";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_UTF8)).andReturn();

        assertAuthors(200, mvcResult);
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

        assertBooks(200, mvcResult);
    }

    private void assertAuthors(int statusTest, MvcResult mvcResult) throws IOException {

        int status = mvcResult.getResponse().getStatus();

        assertEquals(statusTest, status);

        String content = mvcResult.getResponse().getContentAsString();
        content = content.substring(1, content.length() - 1);

        String[] contents = content.split("]},");

        for (int i = 0; i < contents.length - 1; ++i){
            contents[i] += "]}";
        }

        List<AuthorDto> authorsDtoTest = new ArrayList<>(createBookWithTreeAuthors().getAuthors());
        List<BookDto> booksDto = createSomeBooksInDb();
        for(BookDto bookDto : booksDto) {
            authorsDtoTest.addAll(bookDto.getAuthors());
        }

        List<Author> authorsTest = new ArrayList<>();
        for (AuthorDto authorDto : authorsDtoTest){
            authorsTest.add(modelMapper.map(authorDto, Author.class));
        }

        for (int i = 0; i < contents.length - 1; ++i) {
            AuthorDto authorDto = mapFromJson(contents[i], AuthorDto.class);
            Author author = modelMapper.map(authorDto, Author.class);
            assert(authorsTest.contains(author));
        }
    }

    private void assertBooks(int statusTest, MvcResult mvcResult) throws IOException {

        int status = mvcResult.getResponse().getStatus();

        assertEquals(statusTest, status);

        String content = mvcResult.getResponse().getContentAsString();
        content = content.substring(1, content.length() - 1);

        String[] contents = content.split("]},");

        for (int i = 0; i < contents.length - 1; ++i){
            contents[i] += "]}";
        }

        List<BookDto> booksDtoTest = new ArrayList<>();
        booksDtoTest.add(createBookWithTreeAuthors());
        booksDtoTest.addAll(createSomeBooksInDb());

        List<Book> booksTest = new ArrayList<>();
        for (BookDto bookDto : booksDtoTest){
            booksTest.add(modelMapper.map(bookDto, Book.class));
        }

        for (int i = 0; i < contents.length - 1; ++i) {
            BookDto bookDto = mapFromJson(contents[i], BookDto.class);
            Book book = modelMapper.map(bookDto, Book.class);
            assert(booksTest.contains(book));
        }
    }

    private String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    private <T> T mapFromJson(String json, Class<T> clazz)
            throws JsonParseException, JsonMappingException, IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, clazz);
    }

    private void addBooksInDbFromCreateFunc(String uri, String funcName) throws Exception {
        if(funcName.equals("createBookWithTreeAuthors")) {
            List<BookDto> booksDto = new ArrayList<>();
            booksDto.add(createBookWithTreeAuthors());
            addBooksInDb(uri, booksDto);
        }else if(funcName.equals("createSomeBooksInDb")){
            addBooksInDb(uri, createSomeBooksInDb());
        }else{
            throw new RuntimeException("Func: " + funcName + " is not exist");
        }
    }

    private void addBooksInDb(String uri, List<BookDto> booksDto) throws Exception {
        StringBuilder bookDtoJsonBuilder = new StringBuilder();
        String bookDtoJson;
        for(BookDto bookDto : booksDto) {
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
        }else{
            bookDtoJson = bookDtoJsonBuilder.toString();
        }
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                    .contentType(MediaType.APPLICATION_JSON_VALUE).content(bookDtoJson)).andReturn();
    }

    private List<BookDto> createSomeBooksInDb(){
        AuthorDto authorDto1 = new AuthorDto();
        authorDto1.setName("Татьяна");
        authorDto1.setSurname("Журина");
        authorDto1.setBirthday(new Date(1964 - 1900, 6, 17));

        AuthorDto authorDto2 = new AuthorDto();
        authorDto2.setName("Неизвестный");
        authorDto2.setSurname("Автор");
        authorDto2.setBirthday(new Date(1999 - 1900, 6, 17));

        Set<AuthorDto> authorsDto1 = new HashSet<>();
        authorsDto1.add(authorDto1);
        authorsDto1.add(authorDto2);

        BookDto bookDto1 = new BookDto();
        bookDto1.setName("55 устных тем по английскому языку");
        bookDto1.setVolume(155);
        bookDto1.setDateOfPublishing(new Date(2003 - 1900, 5, 11));
        bookDto1.setDateOfWriting(new Date(2002 - 1900, 5, 05));
        bookDto1.setAuthors(authorsDto1);


        AuthorDto authorDto3 = new AuthorDto();
        authorDto3.setName("Герберт");
        authorDto3.setSurname("Шилдт");
        authorDto3.setBirthday(new Date(1977 - 1900, 6, 17));

        Set<AuthorDto> authorsDto2 = new HashSet<>();
        authorsDto2.add(authorDto3);

        BookDto bookDto2 = new BookDto();
        bookDto2.setName("Java. Полное руководство");
        bookDto2.setVolume(1486);
        bookDto2.setDateOfPublishing(new Date(2019 - 1900, 9, 16));
        bookDto2.setDateOfWriting(new Date(2002 - 1900, 5, 05));
        bookDto2.setAuthors(authorsDto2);


        Set<AuthorDto> authorsDto3 = new HashSet<>();
        authorsDto3.add(authorDto2);

        BookDto bookDto3 = new BookDto();
        bookDto3.setName("Неизвестная книга");
        bookDto3.setVolume(10);
        bookDto3.setDateOfPublishing(new Date(2019 - 1900, 9, 16));
        bookDto3.setDateOfWriting(new Date(2002 - 1900, 5, 05));
        bookDto3.setAuthors(authorsDto3);

        List<BookDto> booksDto = new ArrayList<>();
        booksDto.add(bookDto1);
        booksDto.add(bookDto2);
        booksDto.add(bookDto3);

        return booksDto;
    }

    private BookDto createBookWithTreeAuthors(){
        AuthorDto authorDto1 = new AuthorDto();
        authorDto1.setName("Юлиана");
        authorDto1.setSurname("Кузьмина");
        authorDto1.setBirthday(new Date(1964 - 1900, 6, 15));

        AuthorDto authorDto2 = new AuthorDto();
        authorDto2.setName("Роб");
        authorDto2.setSurname("Харроп");
        authorDto2.setBirthday(new Date(1964 - 1900, 6, 15));

        AuthorDto authorDto3 = new AuthorDto();
        authorDto3.setName("Крис");
        authorDto3.setSurname("Шедер");
        authorDto3.setBirthday(new Date(1964 - 1900, 6, 15));

        Set<AuthorDto> authorsDto = new HashSet<>();
        authorsDto.add(authorDto1);
        authorsDto.add(authorDto2);
        authorsDto.add(authorDto3);

        BookDto bookDto = new BookDto();
        bookDto.setName("Spring 5 для профессионалов");
        bookDto.setVolume(1120);
        bookDto.setDateOfPublishing(new Date(2019 - 1900, 5, 11));
        bookDto.setDateOfWriting(new Date(2018 - 1900, 5, 05));
        bookDto.setAuthors(authorsDto);
        return bookDto;
    }
}