package com.mjc.school.repository;

import com.mjc.school.repository.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;


public interface AuthorRepository extends JpaRepository<Author,Long>, JpaSpecificationExecutor<Author> {
    Optional<Author> findByNewsId(Long newsId);
    boolean existsByNameIgnoreCase(String name);
}
