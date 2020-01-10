package com.postnov.library.dto;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

public class PassportDto implements Serializable {

    private String name;

    private String surname;

    @Temporal(TemporalType.DATE)
    private Date birthday;

    private String number;

    private String series;

    private String authorityIssuer;

    @Temporal(TemporalType.DATE)
    private Date dateSigning;

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
}
