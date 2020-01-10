package com.postnov.library.service;

import com.postnov.library.model.Client;
import com.postnov.library.model.LibraryCard;
import com.postnov.library.model.Passport;

import java.util.List;

public interface ClientService {

    void save(Client client);

    Boolean existenceOfTheClient(Client client);

    Client findById(Long id);

    List<Client> findAll();

    void delete(Client client);

    Client findByClient(Client client);

    Client findByPassport(Passport passport);

    Client findByNumberAndSeries(String number, String series);
}
