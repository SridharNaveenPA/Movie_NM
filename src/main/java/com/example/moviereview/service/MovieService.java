package com.example.moviereview.service;

import com.example.moviereview.model.Movie;
import com.example.moviereview.repository.MovieRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {
    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Movie save(Movie movie) { return movieRepository.save(movie); }
    public void deleteById(Long id) { movieRepository.deleteById(id); }
    public Optional<Movie> findById(Long id) { return movieRepository.findById(id); }
    public List<Movie> findAll() { return movieRepository.findAll(); }

    public Page<Movie> search(String title, String genre, Integer releaseYear, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        if (title != null && !title.isBlank()) {
            return movieRepository.findByTitleContainingIgnoreCase(title, pageable);
        } else if (genre != null && !genre.isBlank()) {
            return movieRepository.findByGenreIgnoreCase(genre, pageable);
        } else if (releaseYear != null) {
            return movieRepository.findByReleaseYear(releaseYear, pageable);
        }
        return movieRepository.findAll(pageable);
    }
}


