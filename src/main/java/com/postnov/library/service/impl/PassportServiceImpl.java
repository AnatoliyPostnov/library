package com.postnov.library.service.impl;

import com.postnov.library.model.Passport;
import com.postnov.library.repository.PassportRepository;
import com.postnov.library.service.PassportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PassportServiceImpl implements PassportService {

    @Autowired
    private PassportRepository passportRepository;

    @Override
    public void save(Passport passport) {
        if (!existenceOfThePassport(passport)){
            passportRepository.save(passport);
        }
    }

    @Override
    public Boolean existenceOfThePassport(Passport passport) {
        return passportRepository.findByPassport(
                passport.getNumber(),
                passport.getSeries(),
                passport.getDeletedPassport()
        ).isPresent();
    }

    @Override
    public Passport findById(Long id) {
        Passport passport = passportRepository.findById(id).orElse(null);
        if (passport == null){
            throw new RuntimeException("Passport with id: " + id + " is not exist");
        }else if(passport.getDeletedPassport()){
            return passport;
        }
        throw new RuntimeException("Passport with id: " + id + " is deleted");
    }

    @Override
    public List<Passport> findAll() {
        List<Passport> passports = new ArrayList<>();
        for (Passport passport : passportRepository.findAll()){
            if (passport.getDeletedPassport()){
                passports.add(passport);
            }
        }
        return passports;
    }

    @Override
    public void delete(Passport passport) {
        if(existenceOfThePassport(passport)){
            Passport deletedPassport = findByPassport(passport);
            deletedPassport.setDeletedPassport(false);
        }
    }

    @Override
    public Passport findByPassport(Passport passport) {
        if (passport != null) {
            Passport resultPassport =  passportRepository.findByPassport(
                    passport.getNumber(),
                    passport.getSeries(),
                    passport.getDeletedPassport()).orElse(null);
            if(resultPassport == null){
                throw new RuntimeException("Passport is not found in database");
            }
            return resultPassport;
        }
        throw new RuntimeException("Passport = NULL");
    }
}
