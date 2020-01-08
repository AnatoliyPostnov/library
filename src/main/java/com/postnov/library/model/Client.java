package com.postnov.library.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "client")
public class Client implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String phone;

    @Column
    private String email;

    @Column
    private Boolean deletedClient;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "passport_id")
    private Passport passport;

    @OneToOne(optional = false, mappedBy = "client")
    private LibraryCard libraryCard;

    public Client(){}

    public Client(String phone, String email, Passport passport) {
        this.phone = phone;
        this.email = email;
        this.passport = passport;
        deletedClient = true;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Passport getPassport() {
        return passport;
    }

    public void setPassport(Passport passport) {
        this.passport = passport;
    }

    public Boolean getDeletedClient() {
        return deletedClient;
    }

    public void setDeletedClient(Boolean deletedClient) {
        this.deletedClient = deletedClient;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Client - id: " + id + " phone: "
                + phone + " email: " + email
                + " passport:" + passport.toString()
                + " libraryCard: " + libraryCard.toString();
    }
}
