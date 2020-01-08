package com.postnov.library.service;

import com.postnov.library.model.Passport;

import java.util.List;

public interface PassportService {

    void save(Passport passport);

    Boolean existenceOfThePassport(Passport passport);

    Passport findById(Long id);

    List<Passport> findAll();

    void delete(Passport passport);

    Passport findByPassport(Passport passport);
}
