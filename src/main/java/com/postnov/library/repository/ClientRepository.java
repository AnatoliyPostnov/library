package com.postnov.library.repository;

import com.postnov.library.model.Book;
import com.postnov.library.model.Client;
import com.postnov.library.model.Passport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    @Query(value = "SELECT * FROM Client as cl " +
            "WHERE cl.phone = :phone_client and " +
            "cl.email = :email_client and " +
            "cl.passport_id = :passport_id_client",
            nativeQuery = true)
    Optional<Client> findByClient(@Param("phone_client") String phone_client,
                                  @Param("email_client") String email_client,
                                  @Param("passport_id_client") Long passport_id_client
    );

    @Query(value = "SELECT * FROM Client as cl WHERE cl.passport_id = :passport_id_client",
            nativeQuery = true)
    Optional<Client> findByPassport(@Param("passport_id_client") Long passport_id_client);
}
