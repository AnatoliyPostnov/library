package com.postnov.library.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "genre")
public class Genre implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String descriptionGenre;

    @ManyToMany
    @JoinTable(name = "book_genre",
            joinColumns = @JoinColumn(name = "genre_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id"))
    private Set<Book> books = new HashSet<>();

    public Genre() {
    }

    public Genre(String descriptionGenre) {
        this.descriptionGenre = descriptionGenre;
    }

    public String getDescriptionGenre() {
        return descriptionGenre;
    }

    public void setDescriptionGenre(String descriptionGenre) {
        this.descriptionGenre = descriptionGenre;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }
}
