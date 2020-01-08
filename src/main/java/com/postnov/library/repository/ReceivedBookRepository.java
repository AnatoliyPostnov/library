package com.postnov.library.repository;

import com.postnov.library.model.Client;
import com.postnov.library.model.ReceivedBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface ReceivedBookRepository extends JpaRepository<ReceivedBook, Long> {

    @Query(value = "SELECT * FROM received_book as rb " +
            "WHERE rb.book_id = :book_id and " +
            "rb.library_card_id = :library_card_id",
            nativeQuery = true)
    Optional<ReceivedBook> findByReceivedBook(@Param("book_id") Long book_id,
                                  @Param("library_card_id") Long library_card_id
    );
}
