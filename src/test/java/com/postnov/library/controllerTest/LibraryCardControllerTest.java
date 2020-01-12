package com.postnov.library.controllerTest;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.postnov.library.applicationLibrary;
import com.postnov.library.dto.LibraryCardDto;
import com.postnov.library.model.Client;
import com.postnov.library.model.LibraryCard;
import com.postnov.library.model.Passport;
import com.postnov.library.service.LibraryCardService;
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
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = applicationLibrary.class)
public class LibraryCardControllerTest {
    private MockMvc mvc;

    @Autowired
    private LibraryCardService libraryCardService;

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
    }

    @Test
    public void addLibraryCard(){
        List<LibraryCard> libraryCards = libraryCardService.findAll();

        Assertions.assertEquals(3, libraryCards.size());
    }

    @Test
    public void getLibraryCardsTest() throws Exception {
        String uri = "/get/libraryCards";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_UTF8)).andReturn();

        assertLibraryCards(200, mvcResult);
    }

    @Test
    public void getLibraryCardTest() throws Exception {
        String uri = "/get/libraryCard?number=4567&series=1553445";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_UTF8)).andReturn();

        assertLibraryCards(200, mvcResult);
    }

    private void assertLibraryCards(int statusTest, MvcResult mvcResult) throws IOException {

        int status = mvcResult.getResponse().getStatus();

        assertEquals(statusTest, status);

        String content = mvcResult.getResponse().getContentAsString();
        content = content.substring(1, content.length() - 1);

        String[] contents = content.split("]},");

        for (int i = 0; i < contents.length - 1; ++i){
            contents[i] += "]}";
        }

        List<LibraryCardDto> libraryCardsDtoTest = new ArrayList<>();
        libraryCardsDtoTest.add(createLibraryCard1());
        libraryCardsDtoTest.add(createLibraryCard2());
        libraryCardsDtoTest.add(createLibraryCard3());

        List<LibraryCard> libraryCardsTest = new ArrayList<>();
        for (LibraryCardDto libraryCardDto : libraryCardsDtoTest){
            libraryCardsTest.add(modelMapper.map(libraryCardDto, LibraryCard.class));
        }

        for (int i = 0; i < contents.length - 1; ++i) {
            LibraryCardDto libraryCardDto = mapFromJson(contents[i], LibraryCardDto.class);
            LibraryCard libraryCard = modelMapper.map(libraryCardDto, LibraryCard.class);
            assert(libraryCardsTest.contains(libraryCard));
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
}
