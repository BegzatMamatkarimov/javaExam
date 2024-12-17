package com.arbek.repositories;

import com.arbek.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepositoty extends JpaRepository<Movie, Long> {
    List<Movie> findByTitleContainingIgnoreCase(String title);

    List<Movie> findByGenresContaining(String genre);
}
