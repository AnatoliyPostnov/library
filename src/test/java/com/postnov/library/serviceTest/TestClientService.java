package com.postnov.library.serviceTest;


import com.postnov.library.applicationLibrary;
import com.postnov.library.model.Client;
import com.postnov.library.model.Passport;
import com.postnov.library.service.ClientService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = applicationLibrary.class)
public class TestClientService {

    private GenericApplicationContext ctx;
    private ClientService clientService;

    @Before
    public void init(){
        ctx = new AnnotationConfigApplicationContext(applicationLibrary.class);
        clientService = ctx.getBean(ClientService.class);
        saveThreeClient();
    }

    @Test
    public void saveTest(){
        assertEquals(3, clientService.findAll().size());
    }

    @Test
    public void deleteTest(){
        Passport passport = new Passport(
                "Федор",
                "Бондарчук",
                new Date(1960, 06, 05),
                "1234",
                "12233445",
                "Piter",
                new Date(1990, 05,05)
        );
        Client client = new Client("89533576500", "postnov-90@mail.ru", passport);
        clientService.delete(client);
        assertEquals(2, clientService.findAll().size());
        clientService.save(client);
        assertEquals(3, clientService.findAll().size());
    }

    private void saveThreeClient(){
        Passport passport1 = new Passport(
                "Федор",
                "Бондарчук",
                new Date(1960, 06, 05),
                "1234",
                "12233445",
                "Piter",
                new Date(1990, 05,05)
        );

        Passport passport2 = new Passport(
                "Петя",
                "Бубликов",
                new Date(1913, 05, 05),
                "4567",
                "1553445",
                "Piter",
                new Date(1990, 05,05)
        );

        Passport passport3 = new Passport(
                "Елена",
                "Кузьмина",
                new Date(1993, 02, 13),
                "4204",
                "1029384",
                "Москва",
                new Date(1990, 06,05)
        );
        Client client1 = new Client("89533576500", "postnov-90@mail.ru", passport1);
        Client client2 = new Client("89217554804", "lomidze111@mail.ru", passport2);
        Client client3 = new Client("89644465891", "email@e,ail.ru", passport3);
        System.out.println(client2.toString());
        clientService.save(client1);
        clientService.save(client2);
        clientService.save(client3);
    }
}
