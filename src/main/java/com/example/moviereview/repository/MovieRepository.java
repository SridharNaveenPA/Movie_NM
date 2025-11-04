package com.example.moviereview.repository;

import com.example.moviereview.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    Page<Movie> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    Page<Movie> findByGenreIgnoreCase(String genre, Pageable pageable);
    Page<Movie> findByReleaseYear(Integer releaseYear, Pageable pageable);
}


