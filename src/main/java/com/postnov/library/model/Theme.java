package com.postnov.library.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "theme")
public class Theme implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String descriptionTheme;

    @ManyToMany
    @JoinTable(name = "book_theme",
            joinColumns = @JoinColumn(name = "theme_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id"))
    private Set<Book> books = new HashSet<>();

    public Theme(){}

    public Theme(String descriptionTheme) {
        this.descriptionTheme = descriptionTheme;
    }

    public String getDescriptionTheme() {
        return descriptionTheme;
    }

    public void setDescriptionTheme(String descriptionTheme) {
        this.descriptionTheme = descriptionTheme;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }
}
