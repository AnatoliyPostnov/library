package com.postnov.library.repository;

import com.postnov.library.model.Book;
import com.postnov.library.model.Passport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface PassportRepository extends JpaRepository<Passport, Long> {

    @Query(value = "SELECT * FROM Passport as p " +
            "WHERE p.number = :number and " +
            "p.series = :series and " +
            "p.deleted_passport = :deleted_passport",
            nativeQuery = true)
    Optional<Passport> findByPassport(@Param("number") String number,
                                      @Param("series") String  series,
                                      @Param("deleted_passport") Boolean deleted_passport
    );

}
