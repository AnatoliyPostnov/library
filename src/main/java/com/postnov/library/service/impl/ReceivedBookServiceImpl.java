package com.postnov.library.service.impl;

import com.postnov.library.dto.ReceivedBookDto;
import com.postnov.library.model.Book;
import com.postnov.library.model.LibraryCard;
import com.postnov.library.model.ReceivedBook;
import com.postnov.library.repository.ReceivedBookRepository;
import com.postnov.library.service.BookService;
import com.postnov.library.service.ReceivedBookService;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@EnableScheduling
public class ReceivedBookServiceImpl implements ReceivedBookService {

    private static final Long MILLISECONDS_MONTH = 2592000000L;

    private final BookService bookService;

    private final ReceivedBookRepository receivedBookRepository;

    private final ModelMapper modelMapper;

    private final MailSender ms;

    public ReceivedBookServiceImpl(BookService bookService, ReceivedBookRepository receivedBookRepository, ModelMapper modelMapper, MailSender ms) {
        this.bookService = bookService;
        this.receivedBookRepository = receivedBookRepository;
        this.modelMapper = modelMapper;
        this.ms = ms;
    }

    @Override
    public void save(ReceivedBook receivedBook) {
        if (!existenceOfTheReceivedBook(receivedBook)) {
            receivedBookRepository.save(receivedBook);
        }
    }

    @Override
    public Boolean existenceOfTheReceivedBook(ReceivedBook receivedBook) {
        return receivedBookRepository.findByReceivedBook(
                receivedBook.getBook().getId(),
                receivedBook.getLibraryCard().getId()
        ).isPresent();
    }

    @Override
    public ReceivedBook getReceivedBookByReceivedBook(ReceivedBook inputReceivedBook) {
        ReceivedBook receivedBook = receivedBookRepository.findByReceivedBook(
                inputReceivedBook.getBook().getId(),
                inputReceivedBook.getLibraryCard().getId()).orElse(null);
        if (receivedBook == null) {
            throw new RuntimeException(
                    "ReceivedBook: " + inputReceivedBook.toString() + " is not exist");
        }
        return receivedBook;
    }

    @Override
    public ReceivedBook getReceivedBookByLibraryCardAndBook(LibraryCard libraryCard, Book book) {
        ReceivedBook receivedBookTmp = new ReceivedBook(book, libraryCard);
        return getReceivedBookByReceivedBook(receivedBookTmp);
    }

    @Override
    public List<ReceivedBook> getReceivedBooksByLibraryCard(LibraryCard libraryCard) {
        return receivedBookRepository.findReceivedBooksByLibraryCard(libraryCard.getId());
    }

    @Override
    public List<ReceivedBook> findAll() {
        List<ReceivedBook> receivedBooks = new ArrayList<>();
        for (ReceivedBook receivedBook : receivedBookRepository.findAll()) {
            if (receivedBook.getDateOfBookReturn() == null) {
                receivedBooks.add(receivedBook);
            }
        }
        return receivedBooks;
    }

    @Override
    public Integer returnBooksFromLibraryCard(LibraryCard libraryCard, List<Book> books) {
        Integer count = 0;
        for (Book book : books) {
            Optional<ReceivedBook> receivedBook = receivedBookRepository.findByReceivedBook(
                    book.getId(),
                    libraryCard.getId());
            if (receivedBook.isPresent() &&
                    receivedBook.orElse(null).getDateOfBookReturn() == null) {
                returnBook(libraryCard, book);
                ++count;
            }
        }
        return count;
    }

    @Override
    public List<ReceivedBookDto> convertToListReceivedBooksDto(List<ReceivedBook> receivedBooks) {
        List<ReceivedBookDto> receivedBooksDto = new ArrayList<>();
        for (ReceivedBook receivedBook : receivedBooks) {
            receivedBooksDto.add(modelMapper.map(receivedBook, ReceivedBookDto.class));
        }
        return receivedBooksDto;
    }

    @Override
    public List<ReceivedBook> getHistoryReceivedBooksByLibraryCard(LibraryCard libraryCard) {
        return receivedBookRepository.findHistoryReceivedBooksByLibraryCard(libraryCard.getId());
    }

    @Override
    public List<ReceivedBook> getAllReceivedBook() {
        return receivedBookRepository.findAllReceivedBook();
    }


    @Override
    public void receivedBook(LibraryCard libraryCard, Book book) {
        if (bookService.getIsReceivedBook(book)) {

            Book receivedBook = bookService.findByBook(book);
            bookService.receivedBook(book);

            ReceivedBook objectReceivedBook = new ReceivedBook();

            objectReceivedBook.setBook(receivedBook);
            objectReceivedBook.setLibraryCard(libraryCard);
            objectReceivedBook.setDateOfBookReceiving(new Date());

            receivedBookRepository.save(objectReceivedBook);

        }
    }

    @Override
    public void returnBook(LibraryCard libraryCard, Book book) {
        if (!bookService.getIsReceivedBook(book)) {

            Book receivedBook = bookService.findByBook(book);
            ReceivedBook objectReceivedBook = getReceivedBookByReceivedBook(
                    new ReceivedBook(receivedBook, libraryCard));

            bookService.returnBook(book);
            objectReceivedBook.setDateOfBookReturn(new Date());
        }
    }

    @Scheduled(fixedRate = 10000)
    public void executeTask() {
        List<LibraryCard> libraryCards = new ArrayList<>();

        for (ReceivedBook receivedBook : getAllReceivedBook()) {
            if (!libraryCards.contains(receivedBook.getLibraryCard())) {
                libraryCards.add(receivedBook.getLibraryCard());
            }
        }

        for (LibraryCard libraryCard : libraryCards) {
            List<ReceivedBook> receivedBooks = new ArrayList<>();
//            Строчка для тестирования
//            List<ReceivedBook> receivedBooks = getReceivedBooksByLibraryCard(libraryCard);
            for (ReceivedBook receivedBook : getReceivedBooksByLibraryCard(libraryCard)) {
                Date date = new Date();
                if (date.getTime() - receivedBook.getDateOfBookReceiving().getTime() >=
                        MILLISECONDS_MONTH) {
                    receivedBooks.add(receivedBook);
                }
            }

            if (receivedBooks.isEmpty()) {
                continue;
            }

            StringBuilder message = new StringBuilder("Уважаемый " +
                    libraryCard.getClient().getPassport().getName() + " " +
                    libraryCard.getClient().getPassport().getSurname() + " " +
                    "Вы должны библиотеке следующие книжки: ");
            for (ReceivedBook receivedBook : receivedBooks) {
                message
                        .append(receivedBook.toString())
                        .append(" ")
                        .append("Дата взятия книжки: ")
                        .append(receivedBook.getDateOfBookReceiving())
                        .append(". Большая просьба, верните книжки");
            }

            ms.send(
                    libraryCard.getClient().getEmail(),
                    "From library",
                    message.toString());

        }
    }
}
