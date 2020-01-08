package com.postnov.library.repository;

import com.postnov.library.model.Author;
import com.postnov.library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query(value = "SELECT * FROM Book as b " +
            "WHERE b.name = :name_book and " +
            "b.volume = :volume_book and " +
            "b.date_of_publishing = :date_of_publishing_book and " +
            "b.date_of_writing = :date_of_writing_book and " +
            "b.rating = :rating_book and " +
            "b.deleted_book = :deleted_book",
            nativeQuery = true)
    Optional<Book> finedByBook(@Param("name_book") String name_book,
                     @Param("volume_book") Integer volume_book,
                     @Param("date_of_publishing_book") Date date_of_publishing_book,
                     @Param("date_of_writing_book") Date date_of_writing_book,
                     @Param("rating_book") Integer rating_book,
                     @Param("deleted_book") Boolean deleted_book
    );

}
