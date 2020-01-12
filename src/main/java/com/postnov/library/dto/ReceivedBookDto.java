package com.postnov.library.dto;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

public class ReceivedBookDto {

    @Temporal(TemporalType.DATE)
    private Date dateOfBookReceiving;

    @Temporal(TemporalType.DATE)
    private Date dateOfBookReturn;

    private BookDto book;

    private LibraryCardDto libraryCard;

    public Date getDateOfBookReceiving() {
        return dateOfBookReceiving;
    }

    public void setDateOfBookReceiving(Date dateOfBookReceiving) {
        this.dateOfBookReceiving = dateOfBookReceiving;
    }

    public Date getDateOfBookReturn() {
        return dateOfBookReturn;
    }

    public void setDateOfBookReturn(Date dateOfBookReturn) {
        this.dateOfBookReturn = dateOfBookReturn;
    }

    public BookDto getBook() {
        return book;
    }

    public void setBook(BookDto book) {
        this.book = book;
    }

    public LibraryCardDto getLibraryCard() {
        return libraryCard;
    }

    public void setLibraryCard(LibraryCardDto libraryCard) {
        this.libraryCard = libraryCard;
    }
}
