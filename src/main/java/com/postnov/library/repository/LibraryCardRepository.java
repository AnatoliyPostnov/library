package com.postnov.library.repository;

import com.postnov.library.model.Client;
import com.postnov.library.model.LibraryCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LibraryCardRepository extends JpaRepository<LibraryCard, Long> {
    @Query(value = "SELECT * FROM library_card as lc " +
            "WHERE lc.client_id = :client_id_library_card",
            nativeQuery = true)
    Optional<LibraryCard> findByClientId(@Param("client_id_library_card") Long client_id_library_card);
}
