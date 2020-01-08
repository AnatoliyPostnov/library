package com.postnov.library.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "book")
public class Book implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String name;

    @Column
    private Integer volume;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_of_publishing")
    private Date dateOfPublishing;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_of_writing")
    private Date dateOfWriting;

    @Column
    private Integer rating;

    @Column
    private Boolean deletedBook;

    @Column
    private Boolean isReceivedBook;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name = "book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    private Set<Author> authors = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "book_genre",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private Set<Genre> genres = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "book_theme",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "theme_id"))
    private Set<Theme> themes = new HashSet<>();

    @OneToOne(mappedBy = "book")
    private ReceivedBook receivedBook;

    public Book(){
        this.rating = 0;
        deletedBook = true;
        isReceivedBook = true;
    }

    public Book(String name, Integer volume, Date dateOfPublishing, Date dateOfWriting) {
        this.name = name;
        this.volume = volume;
        this.dateOfPublishing = dateOfPublishing;
        this.dateOfWriting = dateOfWriting;
        this.rating = 0;
        deletedBook = true;
        isReceivedBook = true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public Date getDateOfPublishing() {
        return dateOfPublishing;
    }

    public void setDateOfPublishing(Date dateOfPublishing) {
        this.dateOfPublishing = dateOfPublishing;
    }

    public Date getDateOfWriting() {
        return dateOfWriting;
    }

    public void setDateOfWriting(Date dateOfWriting) {
        this.dateOfWriting = dateOfWriting;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public Set<Theme> getThemes() {
        return themes;
    }

    public void setThemes(Set<Theme> themes) {
        this.themes = themes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Boolean getDeletedBook() {
        return deletedBook;
    }

    public void setDeletedBook(Boolean deletedBook) {
        this.deletedBook = deletedBook;
    }

    public Boolean getReceivedBook() {
        return isReceivedBook;
    }

    public void setReceivedBook(Boolean isReceivedBook) {
        this.isReceivedBook = isReceivedBook;
    }

    public void addAuthor(Author savedAuthor){
        for(Author author: authors){
            if(author.equals(savedAuthor)){
                return;
            }
        }
        authors.add(savedAuthor);
    }

    @Override
    public String toString() {
        return "Book - id: " + id + ", name: " + name + ", volume: "
                + volume + ", dateOfPublishing: "
                + dateOfPublishing + ", dateOfWriting: " + dateOfWriting;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return  name.equals(book.name) &&
                volume.equals(book.volume) &&
                dateOfPublishing.getYear() == book.dateOfPublishing.getYear() &&
                dateOfWriting.getYear() == book.dateOfWriting.getYear() &&
                deletedBook.equals(book.getDeletedBook());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, volume, dateOfPublishing, dateOfWriting, rating);
    }
}
