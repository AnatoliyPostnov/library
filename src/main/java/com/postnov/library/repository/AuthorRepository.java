package com.postnov.library.repository;

import com.postnov.library.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Query(value = "SELECT a.id, a.name, a.surname, a.birthday, a.deleted_author " +
            "FROM Author as a " +
            "WHERE a.name = :name_author and " +
            "a.surname = :surname_author and " +
            "a.birthday = :birthday_author and " +
            "a.deleted_author = :deleted_author",
            nativeQuery = true)
    Optional<Author> finedByAuthor(
                     @Param("name_author") String name_author,
                     @Param("surname_author") String surname_author,
                     @Param("birthday_author") Date birthday_author,
                     @Param("deleted_author") Boolean deleted_author
    );

}
