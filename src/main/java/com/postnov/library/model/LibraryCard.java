package com.postnov.library.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "libraryCard")
public class LibraryCard implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private Boolean deletedLibraryCard;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToMany(mappedBy = "libraryCard", fetch = FetchType.EAGER)
    private Set<ReceivedBook> receivedBook;

    public LibraryCard() {
        deletedLibraryCard = true;
    }

    public LibraryCard(Client client) {
        this.client = client;
        deletedLibraryCard = true;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Boolean getDeletedLibraryCard() {
        return deletedLibraryCard;
    }

    public void setDeletedLibraryCard(Boolean deletedLibraryCard) {
        this.deletedLibraryCard = deletedLibraryCard;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "library card - id: " + id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LibraryCard that = (LibraryCard) o;
        return Objects.equals(client, that.client);
    }

    @Override
    public int hashCode() {
        return Objects.hash(client);
    }
}
