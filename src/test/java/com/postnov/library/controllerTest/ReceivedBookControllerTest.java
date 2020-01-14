package com.postnov.library.controllerTest;


import com.postnov.library.applicationLibrary;
import com.postnov.library.dto.BookDto;
import com.postnov.library.dto.LibraryCardDto;
import com.postnov.library.dto.ReceivedBookDto;
import com.postnov.library.model.LibraryCard;
import com.postnov.library.model.ReceivedBook;
import com.postnov.library.repository.ReceivedBookRepository;
import com.postnov.library.service.LibraryCardService;
import com.postnov.library.service.ReceivedBookService;
import org.junit.Before;
import org.junit.Test;
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
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.postnov.library.controllerTest.TestSData.*;
import static org.junit.Assert.assertEquals;

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

    @Autowired
    private LibraryCardService libraryCardService;

    @Autowired
    private ReceivedBookRepository receivedBookRepository;

    @Before
    public void init() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        addLibraryCardsInDbFromCreateFunc("createLibraryCard1");
        addLibraryCardsInDbFromCreateFunc("createLibraryCard2");
        addLibraryCardsInDbFromCreateFunc("createLibraryCard3");
        addBooksInDbFromCreateFunc("/add/book", "createBookWithTreeAuthors");
        addBooksInDbFromCreateFunc("/add/books", "createSomeBooksInDb");
        receivedBookByBookAndAddInDb();
    }

    @Test
    public void receivedBookByBookTest() {
        List<ReceivedBook> receivedBooks = receivedBookService.getAllReceivedBook();

        assertEquals(1, receivedBooks.size());
    }

    @Test
    public void returnBooksByBookNameTest()
            throws Exception {
        returnBookByBookName(
        );
        List<ReceivedBook> receivedBooks = receivedBookService.getAllReceivedBook();

        assertEquals(0, receivedBooks.size());
    }

    @Test
    public void getReceivedBooksByPassportSNumberAndSeriesTest() throws Exception {
        LibraryCardDto libraryCard = createLibraryCard1();
        String url = "/get/received/books/by/passportS/number/and/series?number=" +
                libraryCard.getClient().getPassport().getNumber() +
                "&series=" +
                libraryCard.getClient().getPassport().getSeries();
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
        assertReceivedBook(mvcResult);
    }

    @Test
    public void getHistoryReceivedBooksByPassportSNumberAndSeriesTest() throws Exception {
        int count = receivedBookRepository.findAll().size();
        returnBookByBookName(
        );
        receivedBookByBookAndAddInDb();
        LibraryCard libraryCardTmp = modelMapper.map(createLibraryCard1(), LibraryCard.class);
        LibraryCard libraryCard = libraryCardService.findByPassportSNumberAndSeries(
                libraryCardTmp.getClient().getPassport().getNumber(),
                libraryCardTmp.getClient().getPassport().getSeries()
        );
        List<ReceivedBook> receivedBooks =
                receivedBookService.getHistoryReceivedBooksByLibraryCard(libraryCard);
        assertEquals(count + 1,
                receivedBookService.convertToListReceivedBooksDto(receivedBooks).size());
    }

    @Test
    public void getReceivedBookTest() throws Exception {
        returnBookByBookName(
        );
        receivedBookByBookAndAddInDb();
        assertEquals(1, receivedBookService.convertToListReceivedBooksDto(
                receivedBookService.getAllReceivedBook()).size());
    }

    private void assertReceivedBook(MvcResult mvcResult) throws IOException {

        assertStatus(mvcResult);

        String content = mvcResult.getResponse().getContentAsString();

        ReceivedBookDto receivedBookDto = new ReceivedBookDto();
        receivedBookDto.setBook(createBookWithTreeAuthors());
        receivedBookDto.setLibraryCard(createLibraryCard1());
        receivedBookDto.setDateOfBookReceiving(new Date());

        ReceivedBook receivedBook = modelMapper.map(receivedBookDto, ReceivedBook.class);

        ReceivedBookDto receivedBookDtoTmp = mapFromJson(
                content.substring(1, content.length() - 1), ReceivedBookDto.class);
        ReceivedBook receivedBookTmp = modelMapper.map(receivedBookDtoTmp, ReceivedBook.class);
        assertEquals(receivedBookTmp, receivedBook);
    }

    private void returnBookByBookName()
            throws Exception {
        TestSData testSData = new TestSData();

        Method createLibraryCardMethod = testSData.getClass().getMethod("createLibraryCard1");

        LibraryCardDto libraryCardDto = (LibraryCardDto) createLibraryCardMethod.invoke(null);

        String number = libraryCardDto.getClient().getPassport().getNumber();
        String series = libraryCardDto.getClient().getPassport().getSeries();

        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append("/return/books/by/book/name?number=")
                .append(number)
                .append("&series=")
                .append(series)
                .append("&name=")
                .append("Spring 5 для профессионалов");

        mvc.perform(MockMvcRequestBuilders.post(urlBuilder.toString())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }


    private void receivedBookByBookAndAddInDb()
            throws Exception {
        TestSData testSData = new TestSData();

        Method createLibraryCardMethod = testSData.getClass().getMethod("createLibraryCard1");

        LibraryCardDto libraryCardDto = (LibraryCardDto) createLibraryCardMethod.invoke(null);

        BookDto bookDto = createBookWithTreeAuthors();

        String number = libraryCardDto.getClient().getPassport().getNumber();
        String series = libraryCardDto.getClient().getPassport().getSeries();
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append("/received/book/by/book?number=")
                .append(number)
                .append("&series=")
                .append(series);

        mvc.perform(MockMvcRequestBuilders.post(urlBuilder.toString())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapToJson(bookDto))
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    private void addLibraryCardsInDbFromCreateFunc(String funcName) throws Exception {
        switch (funcName) {
            case "createLibraryCard1":
                addLibraryCardsInDb(createLibraryCard1());
                break;
            case "createLibraryCard2":
                addLibraryCardsInDb(createLibraryCard2());
                break;
            case "createLibraryCard3":
                addLibraryCardsInDb(createLibraryCard3());
                break;
            default:
                throw new RuntimeException("Func: " + funcName + " is not exist");
        }
    }

    private void addLibraryCardsInDb(LibraryCardDto libraryCardDto) throws Exception {

        String libraryCardJson = mapToJson(libraryCardDto);

        mvc.perform(MockMvcRequestBuilders.post("/add/libraryCard")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(libraryCardJson)).andReturn();
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