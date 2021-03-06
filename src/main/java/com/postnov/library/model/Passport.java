package com.postnov.library.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "passport")
public class Passport implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String name;

    @Column
    private String surname;

    @Temporal(TemporalType.DATE)
    @Column
    private Date birthday;

    @Column
    private String number;

    @Column
    private String series;

    @Column
    private String authorityIssuer;

    @Column
    private Boolean deletedPassport;

    @Temporal(TemporalType.DATE)
    @Column
    private Date dateSigning;

    public Passport() {
        deletedPassport = true;
    }

    public Passport(String name,
                    String surname,
                    Date birthday,
                    String number,
                    String series,
                    String authorityIssuer,
                    Date dateSigning) {
        this.name = name;
        this.surname = surname;
        this.birthday = birthday;
        this.number = number;
        this.series = series;
        this.authorityIssuer = authorityIssuer;
        this.dateSigning = dateSigning;
        deletedPassport = true;
    }

    public Long getId() {
        return id;
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getAuthorityIssuer() {
        return authorityIssuer;
    }

    public void setAuthorityIssuer(String authorityIssuer) {
        this.authorityIssuer = authorityIssuer;
    }

    public Date getDateSigning() {
        return dateSigning;
    }

    public void setDateSigning(Date dateSigning) {
        this.dateSigning = dateSigning;
    }

    public Boolean getDeletedPassport() {
        return deletedPassport;
    }

    public void setDeletedPassport(Boolean deletedPassport) {
        this.deletedPassport = deletedPassport;
    }

    @Override
    public String toString() {
        return "passport: id: " + id + " name: "
                + name + " surname:" + surname
                + " birthday: " + birthday + " number: "
                + number + " series: " + series
                + " authorityIssuer: " + authorityIssuer +
                " dateSigning: " + dateSigning;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Passport passport = (Passport) o;
        return Objects.equals(number, passport.number) &&
                Objects.equals(series, passport.series);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, surname, birthday, number, series, authorityIssuer, deletedPassport, dateSigning);
    }
}
