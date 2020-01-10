package com.postnov.library.dto;

import java.io.Serializable;

public class LibraryCardDto implements Serializable {

    private ClientDto client;

    public ClientDto getClient() {
        return client;
    }

    public void setClient(ClientDto client) {
        this.client = client;
    }
}
