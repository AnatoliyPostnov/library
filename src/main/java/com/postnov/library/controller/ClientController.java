package com.postnov.library.controller;

import com.postnov.library.dto.ClientDto;
import com.postnov.library.model.Client;
import com.postnov.library.service.ClientService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class ClientController {

    private static Logger logger = LoggerFactory.getLogger(BookController.class);

    private final ClientService clientService;

    private final ModelMapper modelMapper;

    public ClientController(ClientService clientService,
                            ModelMapper modelMapper) {
        this.clientService = clientService;
        this.modelMapper = modelMapper;
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/add/client")
    public void addClient(@RequestBody ClientDto clientDto){
        Client client = modelMapper.map(clientDto, Client.class);
        clientService.save(client);
        logger.info(client.toString());
    }

}
