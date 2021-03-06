package com.postnov.library.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.Set;


@Entity
@Table(name = "received_book")
public class ReceivedBook implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Temporal(TemporalType.DATE)
    @Column
    private Date dateOfBookReceiving;

    @Temporal(TemporalType.DATE)
    @Column
    private Date dateOfBookReturn;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "library_card_id")
    private LibraryCard libraryCard;

    public ReceivedBook() {
    }

    public ReceivedBook(Book book, LibraryCard libraryCard) {
        this.book = book;
        this.libraryCard = libraryCard;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public LibraryCard getLibraryCard() {
        return libraryCard;
    }

    public void setLibraryCard(LibraryCard libraryCard) {
        this.libraryCard = libraryCard;
    }

    @Override
    public String toString() {
        return book.toString() + " "
                + libraryCard.toString() + " "
                + dateOfBookReceiving + " "
                + dateOfBookReturn;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReceivedBook that = (ReceivedBook) o;
        if (this.dateOfBookReturn == null && ((ReceivedBook) o).dateOfBookReturn == null) {
            return Objects.equals(dateOfBookReceiving.getYear(), that.dateOfBookReceiving.getYear()) &&
                    Objects.equals(dateOfBookReceiving.getDay(), that.dateOfBookReceiving.getDay()) &&
                    Objects.equals(dateOfBookReceiving.getMonth(), that.dateOfBookReceiving.getMonth()) &&
                    Objects.equals(book, that.book) &&
                    Objects.equals(libraryCard, that.libraryCard);
        } else if (this.dateOfBookReturn != null && ((ReceivedBook) o).dateOfBookReturn != null) {
            return Objects.equals(dateOfBookReceiving.getYear(), that.dateOfBookReceiving.getYear()) &&
                    Objects.equals(dateOfBookReceiving.getDay(), that.dateOfBookReceiving.getDay()) &&
                    Objects.equals(dateOfBookReceiving.getMonth(), that.dateOfBookReceiving.getMonth()) &&
                    Objects.equals(dateOfBookReturn, that.dateOfBookReturn) &&
                    Objects.equals(book, that.book) &&
                    Objects.equals(libraryCard, that.libraryCard);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                dateOfBookReceiving.getYear(),
                dateOfBookReceiving.getMonth(),
                dateOfBookReceiving.getDay(),
                book, libraryCard);
    }
}
