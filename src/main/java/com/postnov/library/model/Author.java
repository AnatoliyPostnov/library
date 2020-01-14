package com.postnov.library.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "author")
public class Author implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String name;

    @Column
    private String surname;

    @Temporal(TemporalType.DATE)
    @Column
    private Date birthday;

    @Column
    private Boolean deletedAuthor;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name = "book_author",
            joinColumns = @JoinColumn(name = "author_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id"))
    private Set<Book> books = new HashSet<>();

    public Author() {
        this.deletedAuthor = true;
    }

    public Author(String name, String surname, Date birthday) {
        this.name = name;
        this.surname = surname;
        this.birthday = birthday;
        this.deletedAuthor = true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    public Boolean getDeletedAuthor() {
        return deletedAuthor;
    }

    public void setDeletedAuthor(Boolean deletedAuthor) {
        this.deletedAuthor = deletedAuthor;
    }

    public void addBook(Book savedBook) {
        for (Book book : books) {
            if (book.equals(savedBook)) {
                return;
            }
        }
        books.add(savedBook);
    }

    @Override
    public String toString() {
        return "Author - Id: " + id + ", name author: " + name
                + ", surname author: " + surname + ", birthday: " + birthday;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return name.equals(author.name) &&
                surname.equals(author.surname) &&
                birthday.getYear() == author.birthday.getYear() &&
                deletedAuthor.equals(author.getDeletedAuthor());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, surname, birthday, deletedAuthor);
    }
}
