package com.postnov.library.controllerTest;

import com.postnov.library.applicationLibrary;
import com.postnov.library.dto.LibraryCardDto;
import com.postnov.library.model.LibraryCard;
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
import java.util.List;

import static com.postnov.library.controllerTest.TestSData.*;

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
        addLibraryCardsInDbFromCreateFunc("createLibraryCard1");
        addLibraryCardsInDbFromCreateFunc("createLibraryCard2");
        addLibraryCardsInDbFromCreateFunc("createLibraryCard3");
    }

    @Test
    public void addLibraryCard() {
        List<LibraryCard> libraryCards = libraryCardService.findAll();

        Assertions.assertEquals(3, libraryCards.size());
    }

    @Test
    public void getLibraryCardsTest() throws Exception {
        String uri = "/get/libraryCards";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_UTF8)).andReturn();

        assertLibraryCards(mvcResult);
    }

    @Test
    public void getLibraryCardTest() throws Exception {
        String uri = "/get/libraryCard?number=4567&series=1553445";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_UTF8)).andReturn();

        assertLibraryCards(mvcResult);
    }

    private void assertLibraryCards(MvcResult mvcResult) throws IOException {

        assertStatus(mvcResult);

        String[] contents = countContents(mvcResult);

        for (int i = 0; i < contents.length - 1; ++i) {
            contents[i] += "]}";
        }

        List<LibraryCardDto> libraryCardsDtoTest = new ArrayList<>();
        libraryCardsDtoTest.add(createLibraryCard1());
        libraryCardsDtoTest.add(createLibraryCard2());
        libraryCardsDtoTest.add(createLibraryCard3());

        List<LibraryCard> libraryCardsTest = new ArrayList<>();
        for (LibraryCardDto libraryCardDto : libraryCardsDtoTest) {
            libraryCardsTest.add(modelMapper.map(libraryCardDto, LibraryCard.class));
        }

        for (int i = 0; i < contents.length - 1; ++i) {
            LibraryCardDto libraryCardDto = mapFromJson(contents[i], LibraryCardDto.class);
            LibraryCard libraryCard = modelMapper.map(libraryCardDto, LibraryCard.class);
            assert (libraryCardsTest.contains(libraryCard));
        }
    }

    private void addLibraryCardsInDbFromCreateFunc(String funcName) throws Exception {
        switch (funcName) {
            case "createLibraryCard1":
                addBooksInDb(createLibraryCard1());
                break;
            case "createLibraryCard2":
                addBooksInDb(createLibraryCard2());
                break;
            case "createLibraryCard3":
                addBooksInDb(createLibraryCard3());
                break;
            default:
                throw new RuntimeException("Func: " + funcName + " is not exist");
        }
    }

    private void addBooksInDb(LibraryCardDto libraryCardDto) throws Exception {

        String libraryCardJson = mapToJson(libraryCardDto);

        mvc.perform(MockMvcRequestBuilders.post("/add/libraryCard")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(libraryCardJson)).andReturn();
    }
}
