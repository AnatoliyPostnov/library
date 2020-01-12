package com.postnov.library.controllerTest;


import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.postnov.library.applicationLibrary;
import com.postnov.library.dto.AuthorDto;
import com.postnov.library.dto.BookDto;
import com.postnov.library.dto.LibraryCardDto;
import com.postnov.library.model.Client;
import com.postnov.library.model.LibraryCard;
import com.postnov.library.model.Passport;
import com.postnov.library.service.ReceivedBookService;
import org.junit.Before;
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

@RunWith(SpringRunner.class)
@SpringBootTest(classes = applicationLibrary.class)
public class ReceivedBookControllerTest {

    private MockMvc mvc;

    @Autowired
    private ReceivedBookService receivedBookService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void init() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        addLibraryCardsInDbFromCreateFunc("/add/libraryCard", "createLibraryCard1");
        addLibraryCardsInDbFromCreateFunc("/add/libraryCard", "createLibraryCard2");
        addLibraryCardsInDbFromCreateFunc("/add/libraryCard", "createLibraryCard3");
        addBooksInDbFromCreateFunc("/add/book", "createBookWithTreeAuthors");
        addBooksInDbFromCreateFunc("/add/books", "createSomeBooksInDb");
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

    private void addLibraryCardsInDbFromCreateFunc(String uri, String funcName) throws Exception {
        switch (funcName) {
            case "createLibraryCard1":
                addBooksInDb(uri, createLibraryCard1());
                break;
            case "createLibraryCard2":
                addBooksInDb(uri, createLibraryCard2());
                break;
            case "createLibraryCard3":
                addBooksInDb(uri, createLibraryCard3());
                break;
            default:
                throw new RuntimeException("Func: " + funcName + " is not exist");
        }
    }

    private void addBooksInDb(String uri, LibraryCardDto libraryCardDto) throws Exception {

        String libraryCardJson = mapToJson(libraryCardDto);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(libraryCardJson)).andReturn();
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


    public LibraryCardDto createLibraryCard1(){
        Passport passport = new Passport();
        passport.setName("Петя");
        passport.setSurname("Бубликов");
        passport.setNumber("4567");
        passport.setSeries("1553445");
        passport.setBirthday(new Date(1964 - 1900, 6, 15));
        passport.setDateSigning(new Date(1990 - 1900, 5, 5));
        passport.setAuthorityIssuer("Piter");
        Client client = new Client();
        client.setEmail("postnov-90@mail.ru");
        client.setPhone("89533576500");
        client.setPassport(passport);
        LibraryCard libraryCard = new LibraryCard();
        libraryCard.setClient(client);
        return modelMapper.map(libraryCard, LibraryCardDto.class);
    }

    public LibraryCardDto createLibraryCard2(){
        Passport passport = new Passport();
        passport.setName("Вася");
        passport.setSurname("Молодец");
        passport.setNumber("7891");
        passport.setSeries("1512345");
        passport.setBirthday(new Date(1973 - 1900, 8, 7));
        passport.setDateSigning(new Date(1995 - 1900, 5, 5));
        passport.setAuthorityIssuer("Moskva");
        Client client = new Client();
        client.setEmail("Vasia@mail.ru");
        client.setPhone("12345678987");
        client.setPassport(passport);
        LibraryCard libraryCard = new LibraryCard();
        libraryCard.setClient(client);
        return modelMapper.map(libraryCard, LibraryCardDto.class);
    }

    public LibraryCardDto createLibraryCard3(){
        Passport passport = new Passport();
        passport.setName("Гарик");
        passport.setSurname("Бульдог");
        passport.setNumber("1357");
        passport.setSeries("1357428");
        passport.setBirthday(new Date(1980 - 1900, 6, 15));
        passport.setDateSigning(new Date(1999 - 1900, 5, 5));
        passport.setAuthorityIssuer("Moskva");
        Client client = new Client();
        client.setEmail("Buldog@mail.ru");
        client.setPhone("98765432198");
        client.setPassport(passport);
        LibraryCard libraryCard = new LibraryCard();
        libraryCard.setClient(client);
        return modelMapper.map(libraryCard, LibraryCardDto.class);
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
