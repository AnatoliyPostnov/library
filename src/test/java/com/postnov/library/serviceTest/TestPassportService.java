package com.postnov.library.serviceTest;

import com.postnov.library.applicationLibrary;
import com.postnov.library.model.Book;
import com.postnov.library.model.Passport;
import com.postnov.library.service.PassportService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = applicationLibrary.class)
public class TestPassportService {

    private GenericApplicationContext ctx;
    private PassportService passportService;

    @Before
    public void init(){
        ctx = new AnnotationConfigApplicationContext(applicationLibrary.class);
        passportService = ctx.getBean(PassportService.class);
        saveThreePassports();
    }

    @Test
    public void saveTest(){
        Passport passport = new Passport(
                "Плохое имя",
                "Не нужно ее сохранять",
                new Date(1960, 06, 05),
                "1234",
                "12233445",
                "Piter",
                new Date(1990, 05,05)
        );
        passportService.save(passport);
        List<Passport> passports = passportService.findAll();
        assertEquals(3, passports.size());
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
        passportService.delete(passport);
        List<Passport> passports = passportService.findAll();
        assertEquals(2, passports.size());
    }

    public void saveThreePassports(){

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

        passportService.save(passport1);
        passportService.save(passport2);
        passportService.save(passport3);
    }

}



