package com.postnov.library.service.impl;

import com.postnov.library.exceptions.ClientAlreadyExistException;
import com.postnov.library.exceptions.IncorrectSavedClientFormatException;
import com.postnov.library.model.Client;
import com.postnov.library.model.Passport;
import com.postnov.library.repository.ClientRepository;
import com.postnov.library.service.ClientService;
import com.postnov.library.service.PassportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    PassportService passportService;

    @Override
    public void save(Client client) {
        if(client.getPassport() != null && !existenceOfTheClient(client)){
            clientRepository.save(client);
        }else if (client.getPassport() == null){
            throw new IncorrectSavedClientFormatException();
        }else{
            throw new ClientAlreadyExistException();
        }
    }

    @Override
    public Boolean existenceOfTheClient(Client client) {
        return passportService.existenceOfThePassport(client.getPassport());
    }

    @Override
    public Client findById(Long id) {
        Client client = clientRepository.findById(id).orElse(null);
        if(client == null){
            throw new RuntimeException("Client with id: " + id + " is not exist");
        }else if(client.getDeletedClient()){
            return client;
        }
        throw new RuntimeException("Client with id: " + id + " is deleted");
    }

    @Override
    public List<Client> findAll() {
        List<Client> clients = new ArrayList<>();
        for (Client client : clientRepository.findAll()){
            if (client.getDeletedClient()){
                clients.add(client);
            }
        }
        return clients;
    }

    @Override
    public void delete(Client client) {
        if (passportService.existenceOfThePassport(client.getPassport())){
            Client deletedClient = findByClient(client);
            passportService.delete(client.getPassport());
            deletedClient.setDeletedClient(false);
        }
    }

    @Override
    public Client findByClient(Client client) {
        Client resultClient = clientRepository.findByClient(
                client.getPhone(),
                client.getEmail(),
                passportService.findByPassport(client.getPassport()).getId()
        ).orElse(null);
        if(resultClient == null){
            throw new RuntimeException(
                    "Client: "
                            + client.toString()
                            + " wasn't found"
            );
        }
        return resultClient;
    }

    @Override
    public Client findByPassport(Passport passport){
        Client client = clientRepository.findByPassport(
                passportService.findByPassport(passport).getId()
        ).orElse(null);
        if(client == null) {
            throw new RuntimeException(
                    "Client with passport: "
                            + passport.toString()
                            + " wasn't found"
            );
        }
        return client;
    }

    @Override
    public Client findByNumberAndSeries(String number, String series) {
        Passport passport = new Passport();
        passport.setNumber(number);
        passport.setSeries(series);
        return findByPassport(passportService.findByPassport(passport));
    }
}
