package com.postnov.library.dto;

import java.io.Serializable;

public class ClientDto implements Serializable {

    private String phone;

    private String email;

    private PassportDto passport;

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

    public PassportDto getPassport() {
        return passport;
    }

    public void setPassport(PassportDto passport) {
        this.passport = passport;
    }
}
